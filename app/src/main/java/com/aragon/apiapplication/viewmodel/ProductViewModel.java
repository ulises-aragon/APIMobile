package com.aragon.apiapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.aragon.apiapplication.models.Product;
import com.aragon.apiapplication.models.ProductResponse;
import com.aragon.apiapplication.repository.ApiRepository;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductViewModel extends ViewModel {

    private ApiRepository repository;
    private MutableLiveData<List<Product>> products;
    private MutableLiveData<Boolean> createProductResult;
    private MutableLiveData<String> errorMessage;

    public ProductViewModel() {
        repository = new ApiRepository();
        products = new MutableLiveData<>();
        createProductResult = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }

    public LiveData<Boolean> getCreateProductResult() {
        return createProductResult;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadProducts() {
        repository.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    products.setValue(response.body());
                } else {
                    errorMessage.setValue("Error al cargar productos");
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                errorMessage.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    public void createProduct(String name, boolean status) {
        repository.createProduct(name, status).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    createProductResult.setValue(true);
                    loadProducts(); // Reload products after creation
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
}
