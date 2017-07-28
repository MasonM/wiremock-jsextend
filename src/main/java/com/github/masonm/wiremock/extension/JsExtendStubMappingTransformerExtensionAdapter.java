package com.github.masonm.wiremock.extension;

import com.github.masonm.wiremock.model.JsExtendExtensionRegistry;
import com.github.masonm.wiremock.model.JsExtendUserExtension;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.StubMappingTransformer;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import javax.script.ScriptException;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;

public class JsExtendStubMappingTransformerExtensionAdapter extends StubMappingTransformer {
    @Override
    public String getName() {
        return "jsextend-stubmapping";
    }

    @Override
    public StubMapping transform(StubMapping stubMapping, FileSource files, Parameters parameters) {
        Iterable<JsExtendUserExtension> extensions = JsExtendExtensionRegistry
            .getInstance()
            .getExtensionsOfType(StubMappingTransformer.class);

        for (JsExtendUserExtension extension : extensions) {
            try {
                stubMapping = (StubMapping) extension
                    .getInvocable()
                    .invokeFunction("transform", stubMapping, files, parameters);
            } catch (ScriptException |NoSuchMethodException e) {
                return throwUnchecked(e, StubMapping.class);
            }
        }
        return stubMapping;
    }
}
