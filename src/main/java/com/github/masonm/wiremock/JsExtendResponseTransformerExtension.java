package com.github.masonm.wiremock;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;

import javax.script.Invocable;
import javax.script.ScriptException;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;

public class JsExtendResponseTransformerExtension extends ResponseTransformer implements JsExtendUserExtension {
    private String name;
    private String javascript;
    private Invocable transformer;

    public JsExtendResponseTransformerExtension(String name, String javascript, Invocable transformer) {
        this.name = name;
        this.javascript = javascript;
        this.transformer = transformer;
    }

    public Response transform(Request request, Response response, FileSource files, Parameters parameters) {
        try {
            return (Response) transformer.invokeFunction("transform", request, response, files, parameters);
        } catch (ScriptException |NoSuchMethodException e) {
            return throwUnchecked(e, Response.class);
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
