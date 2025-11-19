package com.aragon.apiapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.aragon.apiapplication.models.Product;
import com.aragon.apiapplication.viewmodel.AuthViewModel;
import com.aragon.apiapplication.viewmodel.ProductViewModel;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    private ListView listViewProducts;
    private EditText etProductName;
    private Chip checkboxStatus;
    private Button btnCreateProduct, btnLogout;

    private ProductViewModel productViewModel;
    private AuthViewModel authViewModel;

    private ArrayAdapter<String> adapter;
    private List<String> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupViewModels();
        setupListeners();

        productViewModel.loadProducts();
    }

    private void initializeViews() {
        listViewProducts = findViewById(R.id.listViewProducts);
        etProductName = findViewById(R.id.productNameField);
        checkboxStatus = findViewById(R.id.productStatus);
        btnCreateProduct = findViewById(R.id.newProductBttn);
        btnLogout = findViewById(R.id.logOutBttn);

        productList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        listViewProducts.setAdapter(adapter);
    }

    private void setupViewModels() {
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        productViewModel.getProducts().observe(this, products -> {
            if (products != null) {
                productList.clear();
                for (Product product : products) {
                    productList.add("Item " + product.getCode() + ": " + product.getName());
                }
                adapter.notifyDataSetChanged();
            }
        });

        productViewModel.getCreateProductResult().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Producto creado exitosamente", Toast.LENGTH_SHORT).show();
                etProductName.setText("");
                checkboxStatus.setChecked(false);
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
        btnCreateProduct.setOnClickListener(v -> {
            String productName = etProductName.getText().toString().trim();
            boolean status = checkboxStatus.isChecked();

            if (productName.isEmpty()) {
                Toast.makeText(this, "Ingrese el nombre del producto", Toast.LENGTH_SHORT).show();
                return;
            }

            productViewModel.createProduct(productName, status);
        });

        btnLogout.setOnClickListener(v -> authViewModel.logout());
    }
}