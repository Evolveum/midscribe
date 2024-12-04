package com.evolveum.midscribe.generator.data;

/**
 * Created by Viliam Repan (lazyman).
 */
public class Attribute {

    private final String name;

    private final String description;

    private final Object value;

    private String type;

    public Attribute(String name, Object value) {
        this(name, null, value);
    }

    public Attribute(String name, String description, Object value,String type) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Object getValue() {
        return value;
    }
}
