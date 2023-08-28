package com.example.pointofsale.ui.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.pointofsale.R;
import com.example.pointofsale.data.remote.model.Product;
import com.example.pointofsale.databinding.ProductItemBinding;

import java.util.ArrayList;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolderProduct> {
    private final Context context;
    ArrayList<Product> products;
    ProductItemBinding productItemBinding;
    public onItemClickProduct onItemClickProduct;


    public ProductAdapter(Context context,ArrayList<Product>products, onItemClickProduct onItemClickProduct){
        this.context = context;
        this.products = products;
        this.onItemClickProduct =onItemClickProduct;
    }

    public interface onItemClickProduct {
        public void onItemClickProduct(View view, int position, ArrayList<Product> categoryItems);
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolderProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        productItemBinding = ProductItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolderProduct(productItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolderProduct holder, int position) {
        Glide.with(context).load(R.drawable.fix).into(holder.productItemBinding.imageProduct);
        holder.productItemBinding.txtProductName.setText(products.get(position).getProductName());
        holder.productItemBinding.txtProductCurrency.setText( String.valueOf(products.get(position).getProductPrice()));
    }

    @Override
    public int getItemCount() {
        return  products == null ? 0 : products.size();
    }

    public class ViewHolderProduct extends RecyclerView.ViewHolder {
        private final ProductItemBinding productItemBinding;
        public ViewHolderProduct(@NonNull ProductItemBinding productItemBinding) {
            super(productItemBinding.getRoot());
            this.productItemBinding = productItemBinding;
            productItemBinding.addCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickProduct.onItemClickProduct(v, getAdapterPosition(), products);
                }
            });
        }
    }

   @SuppressLint("NotifyDataSetChanged")
   public void updateList(ArrayList<Product> products){
    this.products = products;
    notifyDataSetChanged();
   }
}
