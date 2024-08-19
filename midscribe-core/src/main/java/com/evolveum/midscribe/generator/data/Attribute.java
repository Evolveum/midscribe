package com.evolveum.midscribe.generator.data;

/**
 * Created by Viliam Repan (lazyman).
 */
public class Attribute {

    private String name;
    private String description;
    private Object value;

    private String type;

    public Attribute(String name, Object value) {
        this.name = name;
        this.value = value;
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
