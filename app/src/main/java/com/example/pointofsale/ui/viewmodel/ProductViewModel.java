package com.example.pointofsale.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.pointofsale.data.remote.model.Product;
import com.example.pointofsale.data.remote.repository.ProductRepository;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ##################################################################################
 * Wrote By Alaeldin Abo Hanna
 * View model class that acts as an intermediary between the UI and the data layer,
 * specifically for product-related operations. This class extends Android's ViewModel
 * and provides a clean interface for inserting products asynchronously through the
 * associated ProductRepository.
 * ####################################################################################
 */

@HiltViewModel
public class ProductViewModel extends ViewModel {

    private final   ProductRepository productRepository;

    /**
     * ###########################################################################
     * Constructs a ProductViewModel with the given ProductRepository instance,
     * enabling seamless communication between the UI and data layers.
     *
     * @param productRepository The repository handling product data operations.
     *################################################################################
     */

    @Inject
    public ProductViewModel(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    /**
     * ##############################################################################
     * Inserts a new product into the data source through the associated repository.
     *
     * @param product The product to be inserted.
     *#################################################################################
     */
    public LiveData<Boolean> insertProduct(Product product){

       return this.productRepository.insertProduct(product);
    }

    public LiveData<List<Product>> getAllProducts(){

        return productRepository.getAllProducts();
    }
}
