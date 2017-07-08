package com.github.masonm.wiremock;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;

import javax.script.ScriptException;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;

public class JsExtendResponseTransformerExtensionAdapter extends ResponseTransformer {
    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public Response transform(Request request, Response response, FileSource files, Parameters parameters) {
        Iterable<JsExtendUserExtension> extensions = JsExtendExtensionRegistry
            .getInstance()
            .getExtensionsOfType(ResponseTransformer.class);

        for (JsExtendUserExtension extension : extensions) {
            try {
                response = (Response) extension
                    .getInvocable()
                    .invokeFunction("transform", request, response, files, parameters);
            } catch (ScriptException|NoSuchMethodException e) {
                return throwUnchecked(e, Response.class);
            }
        }
        return response;
    }
}
