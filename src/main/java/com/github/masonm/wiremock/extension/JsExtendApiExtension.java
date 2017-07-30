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
        router.add(GET, "/extensions/{type}", JsExtendGetAllExtensionsTask.class);
        router.add(DELETE, "/extensions/{type}", JsExtendResetExtensionsTask.class);

        router.add(GET, "/extensions/{type}/{name}", JsExtendGetExtensionTask.class);
        router.add(PUT, "/extensions/{type}/{name}", JsExtendPutExtensionTask.class);
        router.add(DELETE, "/extensions/{type}/{name}", JsExtendRemoveExtensionTask.class);
    }
}
