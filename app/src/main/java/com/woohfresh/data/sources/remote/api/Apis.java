package com.woohfresh.data.sources.remote.api;

import com.woohfresh.BuildConfig;

/**
 * Created by creatorbe on 22/02/18.
 */

public class Apis {
    public static final String BASE_URL_REGION = "http://dev.farizdotid.com/api/daerahindonesia/";
    public static final String URL_REGION_STATE = BASE_URL_REGION + "provinsi";
    public static final String BASE_URL = BuildConfig.SERVER_URL;
    public static final String BASE_URL_API = BASE_URL + "api/";
    public static final String BASE_URL_OAUTH = BASE_URL + "oauth/";
    public static final String URL_PRODUCTS = BASE_URL_API + "products";
    public static final String URL_SUBCATEGORIES = BASE_URL_API + "subcategories";
    public static final String URL_STATE = BASE_URL_API + "states";
    public static final String URL_CITY = BASE_URL_API + "cities/state/";
    public static final String REGION_CITY_SHOW = BASE_URL_API + "cities/";
    public static final String URL_PRODUCTS_STATE = BASE_URL_API + "products/state/";
    public static final String URL_SEARCH_STATE = BASE_URL_API + "states/search/";
    public static final String URL_SEARCH = BASE_URL_API + "products/search/";
//    public static final String GET_INGREDIENTS = BASE_URL_API + "ingredients";
    public static final String GET_RECIPES = BASE_URL_API + "recipes";
    public static final String GET_USER = BASE_URL_API + "user";
    public static final String GET_USER_BY_ID = BASE_URL_API + "users/";
    public static final String BIODATA_POST = BASE_URL_API + "biodatas";

    public static final String URL_STORAGE = "http://wohfresh.ardata.co.id/storage/";

    public static ApiService getAPIService() {
        return ApiClient.getClient(BASE_URL_API).create(ApiService.class);
    }
}
