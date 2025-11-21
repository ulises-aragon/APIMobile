package com.aragon.apiapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import com.aragon.apiapplication.models.Product;
import com.aragon.apiapplication.models.ProductResponse;
import com.aragon.apiapplication.network.ApiService;
import com.aragon.apiapplication.repository.ApiRepository;
import com.aragon.apiapplication.util.ProductsPagingSource;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductViewModel extends ViewModel {

    private ApiRepository repository;
    private MutableLiveData<Boolean> createProductResult;
    private MutableLiveData<Boolean> updateProductResult;
    private MutableLiveData<Boolean> deleteProductResult;
    private MutableLiveData<String> errorMessage;
    private final LiveData<PagingData<Product>> products;

    public ProductViewModel(ApiService apiService) {
        repository = new ApiRepository();
        createProductResult = new MutableLiveData<>();
        updateProductResult = new MutableLiveData<>();
        deleteProductResult = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();

        Pager<Integer, Product> pager = new Pager<>(
                new PagingConfig(20, /* prefetchDistance */ 2, /* enablePlaceholders */ false),
                () -> new ProductsPagingSource(apiService, 20, null, null)
        );

        products = PagingLiveData.getLiveData(pager);
    }

    public LiveData<PagingData<Product>> getProducts() {
        return products;
    }

    public LiveData<Boolean> getCreateProductResult() {
        return createProductResult;
    }

    public LiveData<Boolean> getUpdateProductResult() {
        return updateProductResult;
    }

    public LiveData<Boolean> getDeleteProductResult() {
        return deleteProductResult;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void createProduct(String name) {
        repository.createProduct(name).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    createProductResult.setValue(true);

                } else {
                    errorMessage.setValue("Error al crear producto");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                errorMessage.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    public void updateProduct(Integer code, String newName) {
        repository.updateProduct(code, newName).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    updateProductResult.setValue(true);

                } else {
                    errorMessage.setValue("Error al actualizar producto");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                errorMessage.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    public void deleteProduct(Integer code) {
        repository.deleteProduct(code).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    deleteProductResult.setValue(true);
                } else {
                    errorMessage.setValue("Error al eliminar producto");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                errorMessage.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }
}
