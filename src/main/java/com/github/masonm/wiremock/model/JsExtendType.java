package com.github.masonm.wiremock.model;

import com.github.masonm.wiremock.extension.*;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;

public enum JsExtendType {
    RESPONSE_TRANSFORMER(
        JsResponseTransformer.class,
        CompositeResponseTransformer.class
    ),
    RESPONSE_DEFINITION_TRANSFORMER(
        JsResponseDefinitionTransformer.class,
        CompositeResponseDefinitionTransformer.class
    ),
    STUB_MAPPING_TRANSFORMER(
        JsStubMappingTransformer.class,
        CompositeStubMappingTransformer.class
    ),
    REQUEST_MATCHER(
        JsRequestMatcherExtension.class,
        CompositeRequestMatcherExtension.class
    );

    private final Class<? extends JsExtension> jsExtensionClass;
    private final Class<? extends CompositeExtension> compositeExtensionClass;

    JsExtendType(Class<? extends JsExtension> jsExtensionClass, Class<? extends CompositeExtension> compositeExtensionClass) {
        this.jsExtensionClass = jsExtensionClass;
        this.compositeExtensionClass = compositeExtensionClass;
    }

    public JsExtension getJsExtension(JsExtensionSpec spec) {
        try {
            return jsExtensionClass.getConstructor(spec.getClass()).newInstance(spec);
        } catch (ReflectiveOperationException ex) {
            return throwUnchecked(ex, JsExtension.class);
        }
    }

    public CompositeExtension getCompositeExtension() {
        try {
            return compositeExtensionClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException ex) {
            return throwUnchecked(ex, CompositeExtension.class);
        }
    }
}
