package com.evolveum.midscribe.generator;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.Options;
import org.asciidoctor.SafeMode;

import java.io.File;
import java.io.IOException;

/**
 * Created by Viliam Repan (lazyman).
 */
public class HtmlExporter extends ExporterBase {

    // todo figure out templates

    @Override
    public String getDefaultExtension() {
        return "html";
    }

    @Override
    public void export(File adocFile, File output) throws IOException {
        File dir = output.getAbsoluteFile().getParentFile();
        File file = new File(output.getName());

        Options options = Options.builder()
                .safe(SafeMode.UNSAFE)
                .toDir(dir)
                .toFile(file)
                .headerFooter(true)
                .attributes(AttributesBuilder.attributes().styleSheetName("/Users/matejglombik/IdeaProjects/midscribe/midscribe-core/src/main/resources/css/style.css"))
                .build();
//                .templateDir(new File("./src/test/resources/css"));

        Asciidoctor doctor = createAsciidoctor();

        doctor.convertFile(adocFile, options);
    }
}
