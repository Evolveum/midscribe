package com.evolveum.midscribe.generator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Viliam Repan (lazyman).
 */
public final class GeneratorProperties {

    public static final String VELOCITY_START_TEMPLATE = "velocity.start.template";

    public static final String VELOCITY_START_TEMPLATE_DEFAULT = "/template/documentation.vm";

    public static final String VELOCITY_ADDITIONAL_VARIABLES = "velocity.additional.variables";

    public static final Map<String, Object> VELOCITY_ADDITIONAL_VARIABLES_DEFAULT = new HashMap<>();
}
