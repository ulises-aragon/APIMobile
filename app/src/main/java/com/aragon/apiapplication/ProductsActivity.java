package com.aragon.apiapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aragon.apiapplication.models.Product;
import com.aragon.apiapplication.network.ApiService;
import com.aragon.apiapplication.network.RetrofitClient;
import com.aragon.apiapplication.util.ProductViewModelFactory;
import com.aragon.apiapplication.util.ProductsAdapter;
import com.aragon.apiapplication.viewmodel.AuthViewModel;
import com.aragon.apiapplication.viewmodel.ProductViewModel;
import com.google.android.material.textfield.TextInputEditText;


public class ProductsActivity extends AppCompatActivity implements ProductsAdapter.OnProductClickListener {

    private RecyclerView productsView;
    private ProductsAdapter productsAdapter;
    private ImageButton productCreateButton, logoutButton;
    private ProductViewModel productViewModel;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupViewModels();
        setupListeners();
    }

    private void initializeViews() {
        productsView = findViewById(R.id.productsView);
        productCreateButton = findViewById(R.id.productCreateButton);
        logoutButton = findViewById(R.id.logOutButton);

        productsAdapter = new ProductsAdapter();
        productsAdapter.setOnProductClickListener(this);
        productsView.setLayoutManager(new LinearLayoutManager(this));
        productsView.setAdapter(productsAdapter);
    }

    private void setupViewModels() {
        ApiService apiService = RetrofitClient.getInstance().getApiService();
        ProductViewModelFactory factory = new ProductViewModelFactory(apiService);
        productViewModel = new ViewModelProvider(this, factory).get(ProductViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        productViewModel.getProducts().observe(this, products -> {
            productsAdapter.submitData(getLifecycle(), products);
        });

        productViewModel.getCreateProductResult().observe(this, success -> {
            if (success) {
                productsAdapter.refresh();
                Toast.makeText(this, "Producto creado exitosamente", Toast.LENGTH_SHORT).show();
            }
        });

        productViewModel.getUpdateProductResult().observe(this, success -> {
            if (success) {
                productsAdapter.refresh();
                Toast.makeText(this, "Producto actualizado exitosamente", Toast.LENGTH_SHORT).show();
            }
        });

        productViewModel.getDeleteProductResult().observe(this, success -> {
            if (success) {
                productsAdapter.refresh();
                Toast.makeText(this, "Producto eliminado exitosamente", Toast.LENGTH_SHORT).show();
            }
        });

        productViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        authViewModel.getLogoutResult().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        });
    }

    private void setupListeners() {
        productCreateButton.setOnClickListener(v -> {
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_product_input, null);
            final TextInputEditText productNameEditText = dialogView.findViewById(R.id.edit_text_product_name);

            new AlertDialog.Builder(this)
                    .setView(dialogView)
                    .setTitle("Crear nuevo producto")
                    .setPositiveButton("Crear", (dialog, which) -> {
                        String productName = productNameEditText.getText().toString();
                        if (productName.isEmpty()) {
                            Toast.makeText(this, "Ingrese el nombre del producto", Toast.LENGTH_SHORT).show();
                        } else {
                            productViewModel.createProduct(productName);
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        logoutButton.setOnClickListener(v -> authViewModel.logout());
    }

    @Override
    public void onEditClick(Product product) {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_product_input, null);
        final TextInputEditText productNameEditText = dialogView.findViewById(R.id.edit_text_product_name);
        productNameEditText.setText(product.getName());

        new AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle("Editar producto")
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String productName = productNameEditText.getText().toString();
                    if (productName.isEmpty()) {
                        Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                    } else {
                        productViewModel.updateProduct(product.getCode(), productName);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onDeleteClick(Product product) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Producto")
                .setMessage("¿Estás seguro de que quieres eliminar \"" + product.getName() + "\"?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    productViewModel.deleteProduct(product.getCode());
                })
                .setNegativeButton("Cancelar", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}