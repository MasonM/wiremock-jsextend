package com.github.masonm.wiremock.model;

import com.github.masonm.wiremock.extension.*;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ContentTypes;
import com.github.tomakehurst.wiremock.common.Urls;
import com.github.tomakehurst.wiremock.http.Response;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.matching.*;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.google.common.collect.Lists;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.net.URI;
import java.util.List;

public class JsExtensionFactory {
    // Cache ScriptEngineManager for performance
    private ScriptEngineManager scriptEngineManager = null;

    public JsExtension createNew(String name, String type, String javascript) throws ScriptException {
        final ScriptEngine engine = getScriptEngine();
        engine.eval(javascript);
        final JsExtensionSpec spec = new JsExtensionSpec(name, javascript, (Invocable) engine);

        switch(type) {
            case "RequestMatcherExtension":
                return new JsRequestMatcherExtension(spec);
            case "ResponseTransformer":
                return new JsResponseTransformer(spec);
            case "ResponseDefinitionTransformer":
                return new JsResponseDefinitionTransformer(spec);
            case "StubMappingTransformer":
                return new JsStubMappingTransformer(spec);
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
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
                RequestPatternUpdater.class,
                RequestPatternBuilder.class,
                ResponseDefinition.class,
                ResponseDefinitionBuilder.class,
                Response.class,
                StubMapping.class,
                WireMock.class,
                Urls.class,
                URI.class,
                UrlPattern.class,
                ContentTypes.class
            );
            for (Class importClass : globalImports) {
                engine.eval("var " + importClass.getSimpleName() + " = Java.type('" + importClass.getName() + "');");
            }
        }

        return engine;
    }
}