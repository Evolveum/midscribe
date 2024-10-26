package com.evolveum.midscribe.cmd.action;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.evolveum.midscribe.cmd.util.EnumConverterValidator;
import com.evolveum.midscribe.cmd.util.FileConverter;

import java.io.File;
import java.util.List;

@Parameters(resourceBundle = "messages", commandDescriptionKey = "generate")
public class GenerateActionOptions {

    public static class OutputFormatConverterValidator extends EnumConverterValidator<OutputFormat> {

        public OutputFormatConverterValidator() {
            super(OutputFormat.class);
        }
    }

    public static final String P_SOURCE = "-s";
    public static final String P_SOURCE_LONG = "--source";

    public static final String P_ADDITIONAL_SOURCE = "-as";
    public static final String P_ADDITIONAL_SOURCE_LONG = "--additional-source";

    public static final String P_INCLUDE = "-i";
    public static final String P_INCLUDE_LONG = "--include";

    public static final String P_EXCLUDE = "-e";
    public static final String P_EXCLUDE_LONG = "--exclude";

    public static final String P_OUTPUT = "-o";
    public static final String P_OUTPUT_LONG = "--output";

    public static final String P_OUTPUT_FORMAT = "-of";
    public static final String P_OUTPUT_FORMAT_LONG = "--output-format";

    public static final String P_TEMPLATE = "-t";
    public static final String P_TEMPLATE_LONG = "--template";

    public static final String P_PROPERTIES = "-p";
    public static final String P_PROPERTIES_LONG = "--properties";

    @Parameter(names = {P_SOURCE, P_SOURCE_LONG}, descriptionKey = "generate.source", required = true,
            variableArity = true, converter = FileConverter.class)
    private List<File> source;

    @Parameter(names = {P_ADDITIONAL_SOURCE, P_ADDITIONAL_SOURCE_LONG}, descriptionKey = "generate.additionalSource",
            variableArity = true,            converter = FileConverter.class)
    private List<File> additionalSource;

    @Parameter(names = {P_INCLUDE, P_INCLUDE_LONG}, descriptionKey = "generate.include", variableArity = true)
    private List<String> include;

    @Parameter(names = {P_EXCLUDE, P_EXCLUDE_LONG}, descriptionKey = "generate.exclude", variableArity = true)
    private List<String> exclude;

    @Parameter(names = {P_OUTPUT, P_OUTPUT_LONG}, descriptionKey = "generate.output", converter = FileConverter.class)
    private File output;

    @Parameter(names = {P_OUTPUT_FORMAT, P_OUTPUT_FORMAT_LONG}, descriptionKey = "generate.outputFormat",
            converter = OutputFormatConverterValidator.class, validateWith = OutputFormatConverterValidator.class)
    private OutputFormat outputFormat = OutputFormat.ASCIIDOC;

    @Parameter(names = {P_TEMPLATE, P_TEMPLATE_LONG}, descriptionKey = "generate.template",
            converter = FileConverter.class)
    private File template;

    @Parameter(names = {P_PROPERTIES, P_PROPERTIES_LONG}, descriptionKey = "generate.properties",
            converter = FileConverter.class)
    private File properties;

    public List<String> getExclude() {
        return exclude;
    }

    public void setExclude(List<String> exclude) {
        this.exclude = exclude;
    }

    public List<String> getInclude() {
        return include;
    }

    public void setInclude(List<String> include) {
        this.include = include;
    }

    public File getOutput() {
        return output;
    }

    public void setOutput(File output) {
        this.output = output;
    }

    public OutputFormat getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(OutputFormat outputFormat) {
        this.outputFormat = outputFormat;
    }

    public File getProperties() {
        return properties;
    }

    public void setProperties(File properties) {
        this.properties = properties;
    }

    public List<File> getSource() {
        return source;
    }

    public void setSource(List<File> source) {
        this.source = source;
    }

    public File getTemplate() {
        return template;
    }

    public void setTemplate(File template) {
        this.template = template;
    }

    public List<File> getAdditionalSource() {
        return additionalSource;
    }

    public void setAdditionalSource(List<File> additionalSource) {
        this.additionalSource = additionalSource;
    }
}
