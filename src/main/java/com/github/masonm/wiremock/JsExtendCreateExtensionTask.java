package com.github.masonm.wiremock;

import com.github.tomakehurst.wiremock.admin.AdminTask;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.matching.MatchResult;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static java.net.HttpURLConnection.*;

public class JsExtendCreateExtensionTask implements AdminTask {
    @Override
    public ResponseDefinition execute(Admin admin, Request request, PathParams pathParams) {
        JsExtendCreateExtensionSpec spec = Json.read(request.getBodyAsString(), JsExtendCreateExtensionSpec.class);

        ScriptEngine engine = getScriptEngine();
        try {
            engine.eval(spec.getJavascript());
        } catch (ScriptException ex) {
            return ResponseDefinitionBuilder.jsonResponse("Error: " + ex.getMessage(), HTTP_BAD_REQUEST);
        }

        JsExtendUserExtension extension;
        try {
            Class<? extends JsExtendUserExtension> extensionBaseClass = getExtensionClassForType(spec.getType());
            Constructor constructor = extensionBaseClass.getDeclaredConstructor(String.class, String.class, Invocable.class);
            extension = (JsExtendUserExtension) constructor.newInstance(spec.getName(), spec.getJavascript(), (Invocable) engine);
        } catch (NoSuchMethodException|InstantiationException|InvocationTargetException|IllegalAccessException ex) {
            return ResponseDefinitionBuilder.jsonResponse("Server error: " + ex.getMessage(), HTTP_INTERNAL_ERROR);
        }

        JsExtendExtensionRegistry.getInstance().addExtension(spec.getName(), extension);

        return ResponseDefinitionBuilder.jsonResponse(extension, HTTP_OK);
    }

    private ScriptEngine getScriptEngine() {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        if (engine == null) {
            //java 7 fallback
            engine = manager.getEngineByName("JavaScript");
        }

        engine.put("RequestMatcherExtension", JsExtendRequestMatcherExtension.class);
        engine.put("ResponseTransformer", JsExtendResponseTransformerExtension.class);
        engine.put("ResponseDefinitionTransformer", JsExtendResponseDefinitionTransformerExtension.class);

        engine.put("ResponseDefinition", ResponseDefinition.class);
        engine.put("ResponseDefinitionBuilder", ResponseDefinitionBuilder.class);
        engine.put("Response", Response.class);
        engine.put("MatchResult", MatchResult.class);

        return engine;
    }

    private Class<? extends JsExtendUserExtension> getExtensionClassForType(String type) {
        switch(type) {
            case "RequestMatcher":
                return JsExtendRequestMatcherExtension.class;
            case "ResponseTransformer":
                return JsExtendResponseTransformerExtension.class;
            case "ResponseDefinitionTransformer":
                return JsExtendResponseDefinitionTransformerExtension.class;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }
}
