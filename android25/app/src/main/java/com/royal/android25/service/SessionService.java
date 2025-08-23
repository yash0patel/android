package com.royal.android25.service;

import com.royal.android25.model.ResponseModel;
import com.royal.android25.model.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SessionService {
    @POST("/api/auth/signup")
    Call<ResponseModel> signupApi(@Body UserModel userModel);

    @POST("/api/auth/login")
    Call<ResponseModel> loginApi(@Body UserModel userModel);
}
