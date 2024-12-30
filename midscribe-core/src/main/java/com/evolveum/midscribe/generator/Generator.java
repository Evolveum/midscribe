package com.evolveum.midscribe.generator;

import com.evolveum.midpoint.prism.PrismContext;
import com.evolveum.midpoint.xml.ns._public.common.common_3.SchemaType;
import com.evolveum.midscribe.generator.export.ExportFormat;
import com.evolveum.midscribe.generator.export.Exporter;
import com.evolveum.midscribe.generator.export.HtmlExporter;
import com.evolveum.midscribe.generator.export.PdfExporter;
import com.evolveum.midscribe.generator.store.DefaultObjectStore;
import com.evolveum.midscribe.generator.store.GetOptions;
import com.evolveum.midscribe.generator.store.InMemoryObjectStoreFactory;
import com.evolveum.midscribe.generator.store.ObjectStore;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by Viliam Repan (lazyman).
 */
public class Generator {

    private static final Logger LOG = LoggerFactory.getLogger(Generator.class);

    private static final String ADOC_EXTENSION = ".adoc";

    private static final Map<ExportFormat, Class<? extends Exporter>> EXPORTERS;

    static {
        EXPORTERS = Map.ofEntries(
                Map.entry(ExportFormat.PDF, PdfExporter.class),
                Map.entry(ExportFormat.HTML, HtmlExporter.class));
    }

    private final GeneratorOptions options;

    private LogListener logListener;

    private ObjectStoreFactory objectStoreFactory = new InMemoryObjectStoreFactory();

    public Generator(@NotNull GeneratorOptions options) {
        this.options = options;
    }

    public void setObjectStoreFactory(ObjectStoreFactory objectStoreFactory) {
        this.objectStoreFactory = objectStoreFactory;
    }

    public LogListener getLogListener() {
        return logListener;
    }

    public void setLogListener(LogListener logListener) {
        this.logListener = logListener;
    }

    public void generate() throws Exception {
        generate(new Properties());
    }

    public void generate(Properties properties) throws Exception {
        PrismContext prismContext = GeneratorUtils.createPrismContext();
        ObjectStore store = createObjectStore(prismContext);

        registerSchemaObjects(prismContext, store);

        File adocFile = createAdocFile();

        try (Writer output = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(adocFile), StandardCharsets.UTF_8))) {

            VelocityGeneratorProcessor processor = new VelocityGeneratorProcessor(options, properties);

            GeneratorContext ctx = new GeneratorContext(options, prismContext, store);
            processor.process(output, ctx);
        }

        LOG.info("Asciidoc file '{}' generated for all objects", adocFile.getPath());

        if (options.getExportFormat() == null || options.getExportFormat() == ExportFormat.ASCIIDOC) {
            return;
        }

        Exporter exporter = createExporter(options.getExportFormat());
        if (exporter == null) {
            LOG.info("No exporter defined, finishing");
            return;
        }

        File exportFile = createExportFile(exporter);
        LOG.debug("Preparing export from adoc {} to {}", adocFile, exportFile);

        exporter.export(adocFile, exportFile);
    }

    private void registerSchemaObjects(PrismContext context, ObjectStore objectStore) {
        List<SchemaType> schemas = objectStore.list(SchemaType.class, GetOptions.createIncludeAdditionalSources());
        if (schemas.isEmpty()) {
            return;
        }

        Map<String, Element> map = schemas.stream()
                .collect(
                        Collectors.toMap(
                                s -> "extension schema object '" + s.getName() + "'",
                                s -> s.getDefinition().getSchema())
                );

        LOG.info("Registering {} schema objects", schemas.size());

        try {
            context.getSchemaRegistry().registerDynamicSchemaExtensions(map);
        } catch (Exception ex) {
            LOG.debug("Couldn't register schema objects", ex);
        }
    }

    private Exporter createExporter(ExportFormat format) {
        Class<? extends Exporter> clazz = EXPORTERS.get(format);
        if (clazz == null) {
            return null;
        }

        try {
            Exporter exporter = clazz.getDeclaredConstructor().newInstance();
            exporter.setLogListener(logListener);

            return exporter;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException ex) {
            LOG.error("Couldn't create formatter of type {}", clazz);
            return null;
        }
    }

    private File createExportFile(Exporter exporter) throws IOException {
        File adocOutput = options.getAdocOutput();
        File exportOutput = options.getExportOutput();


        if (exportOutput == null) {
            exportOutput = new File(adocOutput.getParent(), adocOutput.getName() + "." + exporter.getDefaultExtension());

        }

        return createFile(exportOutput);
    }

    private File createAdocFile() throws IOException {
        File adocOutput = options.getAdocOutput();
        File exportOutput = options.getExportOutput();
        LOG.info("ExportOutput creation started");
        if (adocOutput == null) {
            LOG.info("ExportOutput trying to create");
            adocOutput = new File(exportOutput.getParent(), exportOutput.getName() + ADOC_EXTENSION);
            LOG.info("ExportOutput creation finished");
        }

        LOG.info("ExportOutput creation skipped");
        return createFile(adocOutput);
    }

    private File createFile(File file) throws IOException {
        LOG.debug("Creating file {}", file.getAbsolutePath());

        if (file.exists()) {
            file.delete();
        }

        file.createNewFile();

        return file;
    }

    private ObjectStore createObjectStore(PrismContext prismContext) {
        ObjectStore objects = objectStoreFactory.createObjectStore(options, prismContext, false);
        ObjectStore additionalObjects = objectStoreFactory.createObjectStore(options, prismContext, true);

        return new DefaultObjectStore(objects, additionalObjects);
    }
}
