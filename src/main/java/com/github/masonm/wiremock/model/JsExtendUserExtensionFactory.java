package com.github.masonm.wiremock.model;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.extension.Extension;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.extension.StubMappingTransformer;
import com.github.tomakehurst.wiremock.http.Response;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.RequestMatcherExtension;
import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.google.common.collect.Lists;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;

public class JsExtendUserExtensionFactory {
    // Cache ScriptEngineManager for performance
    private ScriptEngineManager scriptEngineManager = null;

    public JsExtendUserExtension createNew(JsExtendExtensionSpec spec) throws ScriptException {
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
        if (this.scriptEngineManager == null) {
            this.scriptEngineManager = new ScriptEngineManager();
        }
        ScriptEngine engine = this.scriptEngineManager.getEngineByName("nashorn");
        if (engine == null) {
            //java 7 fallback
            engine = this.scriptEngineManager.getEngineByName("JavaScript");
        } else {
            final List<Class<?>> globalImports = Lists.newArrayList(
                MatchResult.class,
                RequestMatcherExtension.class,
                RequestPattern.class,
                RequestPatternBuilder.class,
                ResponseDefinition.class,
                ResponseDefinitionBuilder.class,
                Response.class,
                StubMapping.class
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
            case "StubMappingTransformer":
                return StubMappingTransformer.class;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }
}