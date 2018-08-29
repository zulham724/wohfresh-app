package com.woohfresh.data.sources.remote.api;

import com.woohfresh.models.api.GSecret;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by creatorbe on 22/02/18.
 */

public interface ApiService {

    @GET("client")
    Call<GSecret> gSecret();

}
