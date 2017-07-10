package com.github.masonm.wiremock.model;

import com.github.tomakehurst.wiremock.extension.Extension;

import java.util.ArrayList;
import java.util.List;

// This is a horrible hack until https://github.com/tomakehurst/wiremock/pull/682 is released
public class JsExtendExtensionRegistry {
    private final List<JsExtendUserExtension> extensions = new ArrayList<>();
    private static JsExtendExtensionRegistry instance = null;

    public static JsExtendExtensionRegistry getInstance() {
        if (instance == null) {
            instance = new JsExtendExtensionRegistry();
        }
        return instance;
    }

    public void addExtension(JsExtendUserExtension extension) {
        extensions.add(extension);
    }

    public List<JsExtendUserExtension> getExtensions() {
        return extensions;
    }

    public List<JsExtendUserExtension> getExtensionsOfType(Class<? extends Extension> extensionType) {
        final List<JsExtendUserExtension> extensionsOfType = new ArrayList<>();
        for (JsExtendUserExtension extension : extensions) {
            if (extension.getType().equals(extensionType)) {
                extensionsOfType.add(extension);
            }
        }
        return extensionsOfType;
    }

    public void deleteExtensions() {
        extensions.clear();
    }
}
