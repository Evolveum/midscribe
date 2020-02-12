package com.evolveum.midpoint.philosopher;

import com.evolveum.midpoint.philosopher.generator.GenerateOptions;
import com.evolveum.midpoint.philosopher.generator.Generator;
import com.evolveum.midpoint.philosopher.generator.HtmlExporter;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Viliam Repan (lazyman).
 */
public class GeneratorTest {

    @Test
    public void generateLocalAdoc() throws Exception {
        GenerateOptions opts = new GenerateOptions();
        opts.setSourceDirectory(new File("/Users/lazyman/Work/monoted/projects/ek/git/midpoint-project/objects"));
        opts.getExclude().addAll(Arrays.asList(new String[]{"users/*.xml", "tasks/misc/*"}));
        File adoc = new File("./target/local.adoc");
        opts.setOutput(adoc);

        Generator generator = new Generator(opts);
        generator.generate();

        // export to html
        HtmlExporter exporter = new HtmlExporter();

        File html = new File("./target/local.html");
        exporter.export(adoc, html);
    }

    @Test
    public void generateAdocHtml() throws IOException {
        HtmlExporter exporter = new HtmlExporter();

        File adoc = new File("./src/test/resources/test.adoc");
        File html = new File("./target/test.html");
        exporter.export(adoc, html);
    }
}
