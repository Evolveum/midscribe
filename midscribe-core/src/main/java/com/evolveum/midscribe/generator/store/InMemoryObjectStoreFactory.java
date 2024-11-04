package com.evolveum.midscribe.generator.store;

import com.evolveum.midpoint.prism.*;
import com.evolveum.midpoint.xml.ns._public.common.common_3.ObjectType;
import com.evolveum.midscribe.generator.GeneratorOptions;
import com.evolveum.midscribe.generator.ObjectStoreFactory;
import com.evolveum.midscribe.util.Expander;
import com.evolveum.midscribe.util.InMemoryFileFilter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class InMemoryObjectStoreFactory implements ObjectStoreFactory {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryObjectStoreFactory.class);

    @Override
    public ObjectStore createObjectStore(GeneratorOptions options, PrismContext prismContext, boolean additional) {
        Properties expanderProperties = new Properties();
        if (options.isExpand()) {
            File file = options.getExpanderProperties();
            if (file == null || !file.isFile() || !file.canRead()) {
                LOG.error("Expander properties file doesn't exist or can't be read '{}'", file);
            } else {
                try (InputStream is = new FileInputStream(options.getExpanderProperties())) {
                    expanderProperties.load(new InputStreamReader(is, StandardCharsets.UTF_8));
                } catch (IOException ex) {
                    LOG.error("Couldn't load midscribe.properties from classpath", ex);
                }
            }
        }

        Expander expander = new Expander(expanderProperties);

        ParsingContext parsingContext = prismContext.createParsingContextForCompatibilityMode();

        Map<Class<? extends ObjectType>, List<ObjectType>> result = new HashMap<>();

        List<File> sources = additional ? options.getAdditionalSources() : options.getSources();
        if (sources == null) {
            sources = new ArrayList<>();
        }
        for (File source : sources) {
            Collection<File> files = FileUtils.listFiles(source,
                    new InMemoryFileFilter(source, options.getInclude(), options.getExclude()), TrueFileFilter.INSTANCE);

            for (File file : files) {
                LOG.debug("Loading {}", file);

                try (InputStream is = new FileInputStream(file)) {
                    InputStream stream = options.isExpand() ? expander.expand(is, StandardCharsets.UTF_8) : is;
                    PrismParser parser = prismContext.parserFor(stream).language(PrismContext.LANG_XML).context(parsingContext);

                    List<PrismObject<? extends Objectable>> objects = parser.parseObjects();
                    for (PrismObject<? extends Objectable> object : objects) {
                        ObjectType obj = (ObjectType) object.asObjectable();

                        List<ObjectType> list = result.computeIfAbsent(obj.getClass(), k -> new ArrayList<>());
                        list.add(obj);
                    }
                } catch (Exception ex) {
                    LOG.warn("Couldn't load file {}, reason: {}", file.getPath(), ex.getMessage());
                }
            }
        }

        return new InMemoryObjectStore(result);
    }
}
