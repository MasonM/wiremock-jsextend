package com.github.masonm.wiremock.extension;

import com.github.masonm.wiremock.tasks.*;
import com.github.tomakehurst.wiremock.admin.Router;
import com.github.tomakehurst.wiremock.extension.AdminApiExtension;

import static com.github.tomakehurst.wiremock.http.RequestMethod.*;

public class JsExtendApiExtension implements AdminApiExtension {

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public void contributeAdminApiRoutes(Router router) {
        router.add(POST, "/extensions", JsExtendCreateExtensionTask.class);
        router.add(DELETE, "/extensions", JsExtendResetExtensionsTask.class);
        router.add(GET, "/extensions", JsExtendGetAllExtensionsTask.class);

        router.add(GET, "/extensions/{id}", JsExtendGetExtensionTask.class);
        router.add(PUT, "/extensions/{id}", JsExtendGetExtensionTask.class);
        router.add(DELETE, "/extensions/{id}", JsExtendRemoveExtensionTask.class);
    }
}
