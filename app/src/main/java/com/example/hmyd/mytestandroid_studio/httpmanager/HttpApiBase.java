package com.example.hmyd.mytestandroid_studio.httpmanager;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author kongdy
 * @date 2016-08-03 23:28
 * @TIME 23:28
 **/


public class HttpApiBase {

    private static final String BASE_URL="";


    public static ApiService.Api getMyRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.Api.class);
    }

}
