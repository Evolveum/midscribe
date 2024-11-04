package com.evolveum.midscribe.cmd.action;

import com.evolveum.midscribe.cmd.Action;
import com.evolveum.midscribe.cmd.ActionResult;
import com.evolveum.midscribe.generator.Generator;
import com.evolveum.midscribe.generator.GeneratorOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Viliam Repan (lazyman).
 */
public class GenerateAction implements Action<GenerateActionOptions, Void> {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateAction.class);

    private static final String SYSTEM_PROPERTY_PREFIX = "midscribe.";

    private GenerateActionOptions options;

    @Override
    public void init(GenerateActionOptions options) {
        this.options = options;
    }

    @Override
    public ActionResult<Void> execute() throws Exception {
        GeneratorOptions options = buildGeneratorOptions();

        Generator generator = new Generator(options);
        generator.generate();

        return new ActionResult<>(null, 0);
    }

    private GeneratorOptions buildGeneratorOptions() {
        GeneratorOptions opts = new GeneratorOptions();
        opts.setSources(options.getSource());
        opts.setAdditionalSources(options.getAdditionalSource());
        opts.setInclude(options.getInclude());
        opts.setExclude(options.getExclude());
        opts.setExportOutput(options.getOutput());
        opts.setExportFormat(options.getOutputFormat().getFormat());
        opts.setTemplate(options.getTemplate());

        Properties properties = buildProperties();
        opts.setProperties(properties);

        return opts;
    }

    private Properties buildProperties() {
        Properties properties = loadPropertiesFile();

        Set<Object> keys = System.getProperties().keySet().stream()
                .filter(key -> key.toString().startsWith(SYSTEM_PROPERTY_PREFIX))
                .collect(Collectors.toSet());

        if (keys.isEmpty()) {
            return properties;
        }

        keys.forEach(key -> {
            String keyStr = key.toString();

            properties.put(keyStr.substring(SYSTEM_PROPERTY_PREFIX.length()), System.getProperty(keyStr));
        });

        return properties;
    }

    // todo fix error handling
    private Properties loadPropertiesFile() {
        if (options.getProperties() == null) {
            return new Properties();
        }

        Properties properties = new Properties();

        File file = options.getProperties();
        if (!file.isFile() || !file.canRead()) {
            LOG.error("Can't read file (not a regular file, doesn't exist, or access rights issue");
        } else {
            try (InputStream is = new FileInputStream(file)) {
                properties.load(new InputStreamReader(is, StandardCharsets.UTF_8));
            } catch (IOException ex) {
                LOG.error("Couldn't load file {}, reason: {}", file, ex.getMessage());
            }
        }

        return properties;
    }
}
