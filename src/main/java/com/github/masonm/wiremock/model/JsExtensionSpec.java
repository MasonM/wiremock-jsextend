package com.github.masonm.wiremock.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.script.Invocable;
import java.util.UUID;

public class JsExtensionSpec {
    private final UUID id;
    private final String javascript;
    private final Invocable invocable;

    public JsExtensionSpec(UUID id, String javascript, Invocable invocable) {
        this.id = id;
        this.javascript = javascript;
        this.invocable = invocable;
    }

    public JsExtensionSpec(String javascript, Invocable invocable) {
        this(UUID.randomUUID(), javascript, invocable);
    }

    public UUID getId() {
        return id;
    }

    @JsonValue
    public String getJavascript() {
        return javascript;
    }

    public Invocable getInvocable() {
        return invocable;
    }
}
