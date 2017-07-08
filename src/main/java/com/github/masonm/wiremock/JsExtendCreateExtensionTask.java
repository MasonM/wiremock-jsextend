package com.github.masonm.wiremock;

import com.github.tomakehurst.wiremock.admin.AdminTask;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.extension.Extension;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.Request;
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

import static java.net.HttpURLConnection.*;

public class JsExtendCreateExtensionTask implements AdminTask {
    @Override
    public ResponseDefinition execute(Admin admin, Request request, PathParams pathParams) {
        JsExtendCreateExtensionSpec spec = Json.read(request.getBodyAsString(), JsExtendCreateExtensionSpec.class);

        ScriptEngine engine;
        try {
            engine = getScriptEngine();
            engine.eval(spec.getJavascript());
        } catch (ScriptException ex) {
            return ResponseDefinitionBuilder.jsonResponse("Error: " + ex.getMessage(), HTTP_BAD_REQUEST);
        }

        JsExtendUserExtension extension = new JsExtendUserExtension(
            getExtensionClassForType(spec.getType()),
            spec.getJavascript(),
            (Invocable) engine
        );

        JsExtendExtensionRegistry.getInstance().addExtension(extension);

        return ResponseDefinitionBuilder.jsonResponse(extension, HTTP_OK);
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
            case "RequestMatcher":
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
