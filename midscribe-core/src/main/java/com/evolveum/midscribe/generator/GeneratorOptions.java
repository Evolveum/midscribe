package com.evolveum.midscribe.generator;

import com.evolveum.midscribe.generator.export.ExportFormat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Viliam Repan (lazyman).
 */
public class GeneratorOptions {

    private List<File> sources;

    private List<File> additionalSources;

    private List<String> include;

    private List<String> exclude;

    private ExportFormat exportFormat;

    private File template;

    private File adocOutput;

    private File exportOutput;

    private Properties properties;

    private boolean expand;

    private File expanderProperties;

    private Class<? extends TemplateEngineContextBuilder> templateEngineContextBuilder;

    public File getTemplate() {
        return template;
    }

    public File getAdocOutput() {
        return adocOutput;
    }

    public void setAdocOutput(File adocOutput) {
        this.adocOutput = adocOutput;
    }

    public File getExportOutput() {
        return exportOutput;
    }

    public ExportFormat getExportFormat() {
        return exportFormat;
    }

    public void setExportFormat(ExportFormat exportFormat) {
        this.exportFormat = exportFormat;
    }

    public void setTemplate(File template) {
        this.template = template;
    }

    public void setExportOutput(File exportOutput) {
        this.exportOutput = exportOutput;
    }

    public List<File> getAdditionalSources() {
        return additionalSources;
    }

    public void setAdditionalSources(List<File> additionalSources) {
        this.additionalSources = additionalSources;
    }

    public List<File> getSources() {
        return sources;
    }

    public void setSources(List<File> sources) {
        this.sources = sources;
    }

    public void setInclude(List<String> include) {
        this.include = include;
    }

    public void setExclude(List<String> exclude) {
        this.exclude = exclude;
    }

    public Class<? extends TemplateEngineContextBuilder> getTemplateEngineContextBuilder() {
        return templateEngineContextBuilder;
    }

    public void setTemplateEngineContextBuilder(Class<? extends TemplateEngineContextBuilder> templateEngineContextBuilder) {
        this.templateEngineContextBuilder = templateEngineContextBuilder;
    }

    public List<String> getInclude() {
        if (include == null) {
            include = new ArrayList<>();
        }
        return include;
    }

    public List<String> getExclude() {
        if (exclude == null) {
            exclude = new ArrayList<>();
        }

        return exclude;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public File getExpanderProperties() {
        return expanderProperties;
    }

    public void setExpanderProperties(File expanderProperties) {
        this.expanderProperties = expanderProperties;
    }
}
