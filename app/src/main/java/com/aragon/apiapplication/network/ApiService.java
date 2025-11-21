package com.aragon.apiapplication.network;

import com.aragon.apiapplication.models.*;
import com.aragon.apiapplication.util.PageResponse;

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
    Call<PageResponse<Product>> getProducts(@Query("page") Integer page,
                                            @Query("size") Integer size,
                                            @Query("search") String search,
                                            @Query("sort") String sort);

    @POST("products")
    Call<ProductResponse> createProduct(@Body Product product);

    @PUT("products/{id}")
    Call<ProductResponse> updateProduct(@Path("id") Integer id, @Body Product product);

    @DELETE("products/{id}")
    Call<ProductResponse> deleteProduct(@Path("id") Integer id);
}
