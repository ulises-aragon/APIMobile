package com.aragon.apiapplication.util;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.aragon.apiapplication.network.ApiService;
import com.aragon.apiapplication.viewmodel.ProductViewModel;

public class ProductViewModelFactory implements ViewModelProvider.Factory {
    private final ApiService apiService;

    public ProductViewModelFactory(ApiService apiService) {
        this.apiService = apiService;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProductViewModel.class)) {
            return (T) new ProductViewModel(apiService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}