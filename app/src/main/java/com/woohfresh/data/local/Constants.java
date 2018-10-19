package com.woohfresh.data.local;

import com.pixplicity.easyprefs.library.Prefs;

public class Constants {

    public static final String IS_LOGIN = "IS_LOGIN";
    public static final String IS_LANGUAGE = "IS_LANGUAGE";
    public static final String APP_LANG = "APP_LANG";
    public static final String APP_CLIENT_ID = "APP_CLIENT_ID";
    public static final String APP_CLIENT_SECRET = "APP_CLIENT_SECRET";
    public static final String APP_SOSMED_ID = "APP_SOSMED_ID";
    public static final String APP_SOSMED_TOKEN = "APP_SOSMED_TOKEN";
    public static final String OAUTH_TOKEN_TYPE = "OAUTH_TOKEN_TYPE";
    public static final String OAUTH_EXPIRES_IN = "OAUTH_EXPIRES_IN";
    public static final String OAUTH_ACCESS_TOKEN = "OAUTH_ACCESS_TOKEN";
    public static final String OAUTH_REFRESH_TOKEN = "OAUTH_REFRESH_TOKEN";
//    public static final String USER_ROLE_ID = "USER_ROLE_ID";
//    public static final String USER_NAME = "USER_NAME";
//    public static final String USER_EMAIL = "USER_EMAIL";
//    public static final String USER_ID = "USER_ID";

    //gUser
    public static final String G_USER_ID = "G_USER_ID";
    public static final String G_BIO_ID = "G_BIO_ID";
    public static final String G_ROLE_ID = "G_ROLE_ID";
    public static final String G_NAME= "G_USERNAME";
    public static final String G_EMAIL = "G_EMAIL";
    public static final String G_AVATAR = "G_AVATAR";
    public static final String G_FACEBOOK_ID = "G_FACEBOOK_ID";
    public static final String G_GOOGLE_ID = "G_GOOGLE_ID";
    public static final String G_LAST_LOGIN = "G_LAST_LOGIN";
    public static final String G_IS_ACTIVE = "G_IS_ACTIVE";
    public static final String G_CREATED_AT = "G_CREATED_AT";
    public static final String G_UPDATED_AT = "G_UPDATED_AT";
    public static final String G_PASSWORD = "G_PASSWORD";
    public static final String G_state_id = "G_state_id";
    public static final String G_city_id = "G_city_id";
    public static final String G_subdistrict_id = "G_subdistrict_id";

    //temp
    public static final String TEMP_EMAIL = "TEMP_EMAIL";
    public static final String TEMP_PASS = "TEMP_PASS";

//    proddet
    public static final String proddet_title = "proddet_title";
    public static final String proddet_desc = "proddet_desc";
    public static final String proddet_weight = "proddet_weight";
    public static final String proddet_rating = "proddet_rating";
    public static final String proddet_badge = "proddet_badge";
    public static final String proddet_price = "proddet_price";
    public static final String proddet_key = "proddet_key";
    public static final String proddet_about = "proddet_about";
    public static final String proddet_handling = "proddet_handling";
    public static final String proddet_img = "proddet_img";
    public static final String proddet_position = "proddet_position";

    //recipes
    public static final String RECIPES_STATUS = "RECIPES_STATUS";
    public static final String RECIPES_ID = "RECIPES_ID";
    public static final String RECIPES_USER_ID = "RECIPES_USER_ID";
    public static final String RECIPES_NAME = "RECIPES_NAME";
    public static final String RECIPES_DESCRIPTION = "RECIPES_DESCRIPTION";
    public static final String RECIPES_TUTORIAL = "RECIPES_TUTORIAL";
    public static final String RECIPES_DIFFICULTY_LEVEL = "RECIPES_DIFFICULTY_LEVEL";
    public static final String RECIPES_PREPARATION_TIME = "RECIPES_PREPARATION_TIME";
    public static final String RECIPES_PORTION_PER_SERVE = "RECIPES_PORTION_PER_SERVE";


//    val
    public static final String VAL_AUTH = "Bearer " + Prefs.getString(Constants.OAUTH_ACCESS_TOKEN, "");

}
