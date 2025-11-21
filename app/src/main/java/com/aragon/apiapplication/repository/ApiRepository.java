package com.aragon.apiapplication.repository;

import com.aragon.apiapplication.models.*;
import com.aragon.apiapplication.network.*;
import com.aragon.apiapplication.util.PageResponse;

import java.util.List;
import retrofit2.Call;

public class ApiRepository {

    private ApiService apiService;

    public ApiRepository() {
        apiService = RetrofitClient.getInstance().getApiService();
    }

    public Call<LoginResponse> login(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);
        return apiService.login(request);
    }

    public Call<RegisterResponse> register(String name, String lastName,
                                           String email, String password,
                                           String confirmPassword) {
        RegisterRequest request = new RegisterRequest(name, lastName, email,
                password, confirmPassword);
        return apiService.register(request);
    }

    public Call<LogoutResponse> logout() {
        return apiService.logout();
    }

    public Call<PageResponse<Product>> getProducts(Integer page, Integer size, String search, String sort) {
        return apiService.getProducts(page, size, search, sort);
    }

    public Call<ProductResponse> createProduct(String name) {
        Product product = new Product(name);
        return apiService.createProduct(product);
    }

    public Call<ProductResponse> updateProduct(Integer id, String name) {
        Product product = new Product(name);
        return apiService.updateProduct(id, product);
    }

    public Call<ProductResponse> deleteProduct(Integer id) {
        return apiService.deleteProduct(id);
    }

}