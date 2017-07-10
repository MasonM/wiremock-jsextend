package com.github.masonm.wiremock.model;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.extension.Extension;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.Response;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.RequestMatcherExtension;
import com.google.common.collect.Lists;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;

public class JsExtendUserExtensionFactory {
    private final JsExtendExtensionSpec spec;

    public JsExtendUserExtensionFactory(JsExtendExtensionSpec spec) {
        this.spec = spec;
    }

    public JsExtendUserExtension createNew() throws ScriptException {
        Class<? extends Extension> type = getExtensionClassForType(spec.getType());

        ScriptEngine engine = getScriptEngine();
        engine.eval(spec.getJavascript());

        if (spec.getId() != null) {
            return new JsExtendUserExtension(type, spec.getJavascript(), (Invocable) engine, spec.getId());
        } else {
            return new JsExtendUserExtension(type, spec.getJavascript(), (Invocable) engine);
        }
    }

    private ScriptEngine getScriptEngine() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        if (engine == null) {
            //java 7 fallback
            engine = manager.getEngineByName("JavaScript");
        } else {
            final List<Class<?>> globalImports = Lists.newArrayList(
                RequestMatcherExtension.class,
                ResponseTransformer.class,
                ResponseDefinitionTransformer.class,
                ResponseDefinition.class,
                ResponseDefinitionBuilder.class,
                Response.class,
                MatchResult.class
            );
            for (Class importClass : globalImports) {
                engine.eval("var " + importClass.getSimpleName() + " = Java.type('" + importClass.getName() + "');");
            }
        }

        return engine;
    }

    private Class<? extends Extension> getExtensionClassForType(String type) {
        switch(type) {
            case "RequestMatcherExtension":
                return RequestMatcherExtension.class;
            case "ResponseTransformer":
                return ResponseTransformer.class;
            case "ResponseDefinitionTransformer":
                return ResponseDefinitionTransformer.class;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }
}
