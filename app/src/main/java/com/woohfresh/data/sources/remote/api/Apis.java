package com.woohfresh.data.sources.remote.api;

import com.woohfresh.BuildConfig;

/**
 * Created by creatorbe on 22/02/18.
 */

public class Apis {
    public static final String BASE_URL = BuildConfig.SERVER_URL;
    public static final String BASE_URL_API = BASE_URL + "api/";
    public static final String BASE_URL_OAUTH = BASE_URL + "oauth/";

    public static ApiService getAPIService() {
        return ApiClient.getClient(BASE_URL_API).create(ApiService.class);
    }
}
