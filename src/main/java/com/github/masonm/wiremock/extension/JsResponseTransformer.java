package com.github.masonm.wiremock.extension;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.masonm.wiremock.model.JsExtensionSpec;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;

import javax.script.ScriptException;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;

public class JsResponseTransformer extends ResponseTransformer implements JsExtension {
    @JsonUnwrapped
    private final JsExtensionSpec spec;

    public JsResponseTransformer(JsExtensionSpec spec) {
        this.spec = spec;
    }

    @Override
    public JsExtensionSpec getSpec() {
        return spec;
    }

    @Override
    public String getName() {
        return spec.getName();
    }

    @Override
    public Response transform(Request request, Response response, FileSource files, Parameters parameters) {
        try {
            return (Response) spec.getInvocable().invokeFunction("transform", request, response, files, parameters);
        } catch (ScriptException |NoSuchMethodException e) {
            return throwUnchecked(e, Response.class);
        }
    }
}
