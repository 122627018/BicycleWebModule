package com.wxxiaomi.ming.bicyclewebmodule.service;



import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by 12262 on 2016/5/31.
 */
public interface DemoService {

    @GET
    Observable<String> sendGet(@Url String url, @QueryMap Map<String, String> options);

    @FormUrlEncoded
    @POST
    Observable<String> sendPost(@Url String url, @FieldMap Map<String, String> options);

//    @FormUrlEncoded
//    @POST
//    Observable<String> sendPost2(@Url String url, @Field("topic") String options);
//
//    @Headers({"Content-Type: application/json","Accept: application/json"})
//    @POST
//    Observable<String> sendPost3(@Url String url, @Body RequestBody body);
}
