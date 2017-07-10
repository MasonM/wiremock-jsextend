package com.github.masonm.wiremock.model;

import com.github.tomakehurst.wiremock.extension.Extension;

import java.util.*;

// This is a horrible hack until https://github.com/tomakehurst/wiremock/pull/682 is released.
// Once it is, this class will be removed and replaced with calls to admin.getOptions().getExtensionsOfType()
public class JsExtendExtensionRegistry {
    private final Map<UUID, JsExtendUserExtension> extensions = new LinkedHashMap<>();
    private static JsExtendExtensionRegistry instance = null;

    public static JsExtendExtensionRegistry getInstance() {
        if (instance == null) {
            instance = new JsExtendExtensionRegistry();
        }
        return instance;
    }

    public void addExtension(JsExtendUserExtension extension) {
        extensions.put(extension.getId(), extension);
    }

    public Collection<JsExtendUserExtension> getExtensions() {
        return extensions.values();
    }

    public JsExtendUserExtension getExtension(UUID id){
        return extensions.get(id);
    }

    public JsExtendUserExtension removeExtension(UUID id) {
        return extensions.remove(id);
    }

    public void deleteExtensions() {
        extensions.clear();
    }

    public List<JsExtendUserExtension> getExtensionsOfType(Class<? extends Extension> extensionType) {
        final List<JsExtendUserExtension> extensionsOfType = new ArrayList<>();
        for (JsExtendUserExtension extension : extensions.values()) {
            if (extension.getType().equals(extensionType)) {
                extensionsOfType.add(extension);
            }
        }
        return extensionsOfType;
    }
}
