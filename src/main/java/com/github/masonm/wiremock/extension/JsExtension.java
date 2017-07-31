package com.github.masonm.wiremock.extension;

import com.fasterxml.jackson.annotation.JsonValue;
import com.github.masonm.wiremock.model.JsExtensionSpec;
import com.github.tomakehurst.wiremock.extension.Extension;

public interface JsExtension extends Extension {
    @JsonValue
    JsExtensionSpec getSpec();
}
