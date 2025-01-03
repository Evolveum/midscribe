package com.evolveum.midscribe.generator.export;

import com.evolveum.midscribe.generator.LogListener;

import java.io.File;
import java.io.IOException;

/**
 * Created by Viliam Repan (lazyman).
 */
public interface Exporter {

    void setLogListener(LogListener listener);

    void export(File adocFile, File output) throws IOException;

    String getDefaultExtension();
}
