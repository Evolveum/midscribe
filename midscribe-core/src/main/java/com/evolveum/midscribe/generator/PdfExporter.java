package com.evolveum.midscribe.generator;
import org.jruby.util.log.Logger;
import org.jruby.util.log.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class PdfExporter extends ExporterBase {

    private static final Logger LOG = LoggerFactory.getLogger(PdfExporter.class);

    @Override
    public String getDefaultExtension() {
        return "pdf";
    }

    @Override
    public void export(File adocFile, File outputPdf) throws IOException {
        File tempHtmlFile = File.createTempFile("temp", ".html");

        try {
            HtmlExporter htmlExporter = new HtmlExporter();
            htmlExporter.export(adocFile, tempHtmlFile);

            String princeCommand = "prince " + tempHtmlFile.getAbsolutePath() + " -o " + outputPdf.getAbsolutePath();
            Process process = Runtime.getRuntime().exec(princeCommand);
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                LOG.error("PrinceXML conversion failed with exit code " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            LOG.error("Error converting AsciiDoc to PDF", e);
            throw new IOException("Conversion failed", e);
        } finally {
            // Delete temporary HTML file
            tempHtmlFile.delete();
        }
    }
}













