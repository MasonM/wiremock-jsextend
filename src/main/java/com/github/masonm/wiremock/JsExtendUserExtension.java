package com.github.masonm.wiremock;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.tomakehurst.wiremock.extension.Extension;

import javax.script.Invocable;
import java.util.UUID;

public class JsExtendUserExtension {
    private final Class<? extends Extension> type;
    private final String name;
    private final String javascript;
    private final Invocable invocable;
    private final UUID id;

    public JsExtendUserExtension(Class<? extends Extension> type, String name, String javascript, Invocable invocable) {
        this.type = type;
        this.name = name;
        this.javascript = javascript;
        this.invocable = invocable;
        this.id = UUID.randomUUID();
    }

    public Class getType() {
        return type;
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

    public UUID getId() {
        return id;
    }
}
