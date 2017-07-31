package com.github.masonm.wiremock.extension;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.masonm.wiremock.model.JsExtensionSpec;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.RequestMatcherExtension;

import javax.script.ScriptException;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;

public class JsRequestMatcherExtension extends RequestMatcherExtension implements JsExtension {
    private final JsExtensionSpec spec;

    public JsRequestMatcherExtension(JsExtensionSpec spec) {
        this.spec = spec;
    }

    @Override
    public JsExtensionSpec getSpec() {
        return spec;
    }

    @Override
    public MatchResult match(Request request, Parameters parameters) {
        try {
            return (MatchResult) spec.getInvocable().invokeFunction("match", request, parameters);
        } catch (ScriptException |NoSuchMethodException e) {
            return throwUnchecked(e, MatchResult.class);
        }
    }
}
