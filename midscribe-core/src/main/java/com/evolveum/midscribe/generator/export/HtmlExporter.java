package com.evolveum.midscribe.generator.export;

import org.asciidoctor.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by Viliam Repan (lazyman).
 */
public class HtmlExporter extends ExporterBase {

    // todo figure out templates

    // todo pass this probably via cmd options?
    private File cssFilePath;

    @Override
    public String getDefaultExtension() {
        return "html";
    }

    @Override
    public void export(File adocFile, File output) throws IOException {
        File dir = output.getAbsoluteFile().getParentFile();
        File file = new File(output.getName());

        AttributesBuilder builder = Attributes.builder();
        if (cssFilePath != null && cssFilePath.exists() && cssFilePath.length() > 0) {
            builder.styleSheetName(cssFilePath.getPath());
        }

        Options options = Options.builder()
                .safe(SafeMode.UNSAFE)
                .toDir(dir)
                .toFile(file)
                .standalone(true)
                .build();

        try (Asciidoctor doctor = createAsciidoctor()) {
            doctor.convertFile(adocFile, options);
        }
    }

    public void setCssFilePath(File cssFilePath) {
        this.cssFilePath = cssFilePath;
    }
}
