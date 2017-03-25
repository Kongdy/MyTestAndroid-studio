package com.example.hmyd.mytestandroid_studio.httpmanager;

import com.example.hmyd.mytestandroid_studio.model.TModel;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * @author kongdy
 * @date 2016-08-03 23:24
 * @TIME 23:24
 **/


public class ApiService {

    public interface Api {

        @POST("")
        Call<TModel> test();

    }
}
