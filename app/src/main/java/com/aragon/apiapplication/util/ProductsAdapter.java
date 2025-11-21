package com.aragon.apiapplication.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.aragon.apiapplication.R;
import com.aragon.apiapplication.models.Product;

public class ProductsAdapter extends PagingDataAdapter<Product, ProductsAdapter.ProductViewHolder> {

    private static OnProductClickListener listener;

    public ProductsAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setOnProductClickListener(OnProductClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product item = getItem(position);
        if (item != null) {
            holder.bind(item);
        } else {
            holder.clear();
        }
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final TextView productCodeText, productNameText;
        private final ImageButton productEditButton, productDeleteButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productCodeText = itemView.findViewById(R.id.productCodeText);
            productNameText = itemView.findViewById(R.id.productNameText);
            productEditButton = itemView.findViewById(R.id.productEditButton);
            productDeleteButton = itemView.findViewById(R.id.productDeleteButton);
        }

        void bind(Product product) {
            productCodeText.setText(String.format("%03d", product.getCode()));
            productNameText.setText(product.getName());

            productEditButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(product);
                }
            });

            productDeleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(product);
                }
            });
        }

        void clear() {
            productCodeText.setText("");
            productNameText.setText("");
        }
    }

    private static final DiffUtil.ItemCallback<Product> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Product>() {
                @Override
                public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
                    return oldItem.getCode().equals(newItem.getCode());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
                    return oldItem.getName().equals(newItem.getName())
                            && (oldItem.isStatus() == newItem.isStatus());
                }
            };

    public interface OnProductClickListener {
        void onEditClick(Product product);
        void onDeleteClick(Product product);
    }
}
