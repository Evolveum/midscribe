package com.evolveum.midscribe.generator;

import com.evolveum.midpoint.prism.PrismContext;
import com.evolveum.midpoint.prism.util.PrismContextFactory;
import com.evolveum.midpoint.schema.MidPointPrismContextFactory;
import com.evolveum.midpoint.util.DOMUtilSettings;
import com.evolveum.midpoint.util.exception.SchemaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.IOException;

public class GeneratorUtils {

    private static final Logger LOG = LoggerFactory.getLogger(GeneratorUtils.class);

    public static PrismContext createPrismContext() throws SchemaException, IOException, SAXException {
        LOG.debug("Initializing prism context");

        DOMUtilSettings.setAddTransformerFactorySystemProperty(false);

        PrismContextFactory factory = new MidPointPrismContextFactory();
        PrismContext prismContext = factory.createPrismContext();
        prismContext.initialize();

        return prismContext;
    }
}
