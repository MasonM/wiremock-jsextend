package com.github.masonm.wiremock;

import com.github.tomakehurst.wiremock.admin.Router;
import com.github.tomakehurst.wiremock.extension.AdminApiExtension;

import static com.github.tomakehurst.wiremock.http.RequestMethod.DELETE;
import static com.github.tomakehurst.wiremock.http.RequestMethod.GET;
import static com.github.tomakehurst.wiremock.http.RequestMethod.POST;

public class JsExtendApiExtension implements AdminApiExtension {

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public void contributeAdminApiRoutes(Router router) {
        router.add(POST, "/extensions", JsExtendCreateExtensionTask.class);
        router.add(DELETE, "/extensions", JsExtendDeleteExtensionsTask.class);
        router.add(GET, "/extensions", JsExtendGetAllExtensionsTask.class);
    }
}
