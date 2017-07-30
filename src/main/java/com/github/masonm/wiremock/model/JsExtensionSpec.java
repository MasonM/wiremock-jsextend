package com.github.masonm.wiremock.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.script.Invocable;

public class JsExtensionSpec {
    private final String name;
    private final String javascript;
    private final Invocable invocable;

    public JsExtensionSpec(String name, String javascript, Invocable invocable) {
        this.name = name;
        this.javascript = javascript;
        this.invocable = invocable;
    }

    public String getName() {
        return name;
    }

    public String getJavascript() {
        return javascript;
    }

    @JsonIgnore
    public Invocable getInvocable() {
        return invocable;
    }
}
