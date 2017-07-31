package com.github.masonm.wiremock.extension;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.masonm.wiremock.model.JsExtensionSpec;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

import javax.script.ScriptException;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;

public class JsResponseDefinitionTransformer extends ResponseDefinitionTransformer implements JsExtension {
    private final JsExtensionSpec spec;

    public JsResponseDefinitionTransformer(JsExtensionSpec spec) {
        this.spec = spec;
    }

    @Override
    public JsExtensionSpec getSpec() {
        return spec;
    }

    @Override
    public String getName() {
        return spec.getId().toString();
    }

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files, Parameters parameters) {
        try {
            return (ResponseDefinition) spec.getInvocable().invokeFunction("transform", request, responseDefinition, files, parameters);
        } catch (ScriptException |NoSuchMethodException e) {
            return throwUnchecked(e, ResponseDefinition.class);
        }
    }
}
