package com.github.masonm.wiremock;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsExtendExtensionRegistry {
    private final Map<String, JsExtendUserExtension> extensions;
    private static JsExtendExtensionRegistry instance = null;

    protected JsExtendExtensionRegistry() {
        extensions = new LinkedHashMap<String, JsExtendUserExtension>();
    }

    public static JsExtendExtensionRegistry getInstance() {
        if (instance == null) {
            instance = new JsExtendExtensionRegistry();
        }
        return instance;
    }

    public void addExtension(String name, JsExtendUserExtension extension) {
        extensions.put(name, extension);
    }

    public Collection<JsExtendUserExtension> getExtensions() {
        return extensions.values();
    }

    @SuppressWarnings("unchecked")
    public <T extends JsExtendUserExtension> Collection<T> getExtensionsOfType(final Class<T> extensionType) {
        return (Collection<T>) Maps.filterEntries(extensions, valueAssignableFrom(extensionType)).values();
    }

    public static <T extends JsExtendUserExtension> Predicate<Map.Entry<String, JsExtendUserExtension>> valueAssignableFrom(final Class<T> extensionType) {
        return new Predicate<Map.Entry<String, JsExtendUserExtension>>() {
            public boolean apply(Map.Entry<String, JsExtendUserExtension> input) {
                return extensionType.isAssignableFrom(input.getValue().getClass());
            }
        };
    }

    public void deleteExtensions() {
        extensions.clear();
    }
}
