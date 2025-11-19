package com.aragon.apiapplication.network;

import com.aragon.apiapplication.models.*;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("auth/logout")
    Call<LogoutResponse> logout();

    @GET("products")
    Call<List<Product>> getProducts();

    @POST("products")
    Call<ProductResponse> createProduct(@Body Product product);
}
