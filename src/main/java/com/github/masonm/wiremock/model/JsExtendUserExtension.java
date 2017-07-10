package com.github.masonm.wiremock.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.tomakehurst.wiremock.extension.Extension;

import javax.script.Invocable;
import java.util.UUID;

public class JsExtendUserExtension {
    private final Class<? extends Extension> type;
    private final String javascript;
    private final Invocable invocable;
    private final UUID id;

    public JsExtendUserExtension(Class<? extends Extension> type, String javascript, Invocable invocable, UUID id) {
        this.type = type;
        this.javascript = javascript;
        this.invocable = invocable;
        this.id = id;
    }

    public JsExtendUserExtension(Class<? extends Extension> type, String javascript, Invocable invocable) {
        this(type, javascript, invocable, UUID.randomUUID());
    }

    public Class getType() {
        return type;
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
