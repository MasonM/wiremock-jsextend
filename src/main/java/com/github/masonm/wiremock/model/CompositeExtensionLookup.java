package com.github.masonm.wiremock.model;

import com.github.masonm.wiremock.extension.*;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.extension.Extension;
import com.google.common.collect.ImmutableMap;

import java.util.*;

public class CompositeExtensionLookup {
    private static final ImmutableMap<String, Class<? extends CompositeExtension<? extends Extension>>> compositeExtensionMap = ImmutableMap.of(
        "RequestMatcherExtension", CompositeRequestMatcherExtension.class,
        "ResponseDefinitionTransformer", CompositeResponseDefinitionTransformer.class,
        "ResponseTransformer", CompositeResponseTransformer.class,
        "StubMappingTransformer", CompositeStubMappingTransformer.class
    );
    private final Options options;
    private final String type;

    public CompositeExtensionLookup(Options options, String type) {
        this.options = options;
        this.type = type;
    }

    public Map<String, JsExtension> getJsExtensions() throws ClassNotFoundException {
        if (!compositeExtensionMap.containsKey(type)) {
            throw new ClassNotFoundException("Invalid type: " + type);
        }
        CompositeExtension compositeExtension = getExtension(compositeExtensionMap.get(type));
        return compositeExtension.getExtensions();
    }

    public Map<String, Collection<JsExtension>> getJsExtensionsByType() {
        Map<String, Collection<JsExtension>> extensions = new HashMap<>();
        for (Map.Entry<String, Class<? extends CompositeExtension<? extends Extension>>> entry : compositeExtensionMap.entrySet()) {
            extensions.put(entry.getKey(), getExtension(entry.getValue()).getExtensions().values());
        }
        return extensions;
    }

    public List<CompositeExtension> getAllCompositeExtensions() {
        List<CompositeExtension> compositeExtensions = new ArrayList<>();
        for (Class<? extends CompositeExtension<? extends Extension>> compositeExtensionClass : compositeExtensionMap.values()) {
            compositeExtensions.add(getExtension(compositeExtensionClass));
        }
        return compositeExtensions;
    }

    private CompositeExtension getExtension(Class<? extends CompositeExtension<? extends Extension>> type) {
        Map<String, ? extends CompositeExtension> extensions = options.extensionsOfType(type);
        return extensions.values().iterator().next();
    }
}
