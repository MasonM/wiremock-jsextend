package com.github.masonm.wiremock;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

import javax.script.Invocable;
import javax.script.ScriptException;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;

public class JsExtendResponseDefinitionTransformerExtension extends ResponseDefinitionTransformer implements JsExtendUserExtension {
    private String name;
    private String javascript;
    private Invocable transformer;

    public JsExtendResponseDefinitionTransformerExtension(String name, String javascript, Invocable transformer) {
        this.name = name;
        this.javascript = javascript;
        this.transformer = transformer;
    }

    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files, Parameters parameters) {
        try {
            return (ResponseDefinition) transformer.invokeFunction("transform", request, responseDefinition, files, parameters);
        } catch (ScriptException |NoSuchMethodException e) {
            return throwUnchecked(e, ResponseDefinition.class);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getJavascript() {
        return javascript;
    }
}
