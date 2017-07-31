package com.github.masonm.wiremock.extension;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.github.masonm.wiremock.model.JsExtensionSpec;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.StubMappingTransformer;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import javax.script.ScriptException;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;

public class JsStubMappingTransformer extends StubMappingTransformer implements JsExtension {
    private final JsExtensionSpec spec;

    public JsStubMappingTransformer(JsExtensionSpec spec) {
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
    public StubMapping transform(StubMapping stubMapping, FileSource files, Parameters parameters) {
        try {
            return (StubMapping) spec.getInvocable().invokeFunction("transform", stubMapping, files, parameters);
        } catch (ScriptException |NoSuchMethodException e) {
            return throwUnchecked(e, StubMapping.class);
        }
    }
}
