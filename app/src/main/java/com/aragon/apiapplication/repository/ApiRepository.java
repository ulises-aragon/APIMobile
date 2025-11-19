package com.aragon.apiapplication.repository;

import com.aragon.apiapplication.models.*;
import com.aragon.apiapplication.network.*;
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

    public Call<List<Product>> getProducts() {
        return apiService.getProducts();
    }

    public Call<ProductResponse> createProduct(String name, boolean status) {
        Product product = new Product(name, status);
        return apiService.createProduct(product);
    }
}