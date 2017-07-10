package com.github.masonm.wiremock.model;

import com.github.masonm.wiremock.model.JsExtendExtensionRegistry;
import com.github.masonm.wiremock.model.JsExtendUserExtension;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

import javax.script.ScriptException;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;

public class JsExtendResponseDefinitionTransformerExtensionAdapter extends ResponseDefinitionTransformer {
    @Override
    public String getName() {
        return "jsextend-responsedefinition";
    }

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files, Parameters parameters) {
        Iterable<JsExtendUserExtension> extensions = JsExtendExtensionRegistry
            .getInstance()
            .getExtensionsOfType(ResponseDefinitionTransformer.class);

        for (JsExtendUserExtension extension : extensions) {
            try {
                responseDefinition = (ResponseDefinition) extension
                    .getInvocable()
                    .invokeFunction("transform", request, responseDefinition, files, parameters);
            } catch (ScriptException |NoSuchMethodException e) {
                return throwUnchecked(e, ResponseDefinition.class);
            }
        }
        return responseDefinition;
    }
}
