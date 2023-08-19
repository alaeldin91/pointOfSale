package com.example.pointofsale.data.remote.repository;


import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.pointofsale.data.local.dao.ProductDao;
import com.example.pointofsale.data.remote.model.Product;
import java.util.List;
import javax.inject.Inject;

/**
 * ####################################################################################
 * Wrote By Alaeldin Musa
 * A repository class responsible for managing interactions between the data source
 * and the application for products. This class uses a background executor to perform
 * asynchronous operations on the provided ProductDao.
 * ######################################################################################
 */
public class ProductRepository {
    private final ProductDao productDao;


    /**
     *##########################################################################
     * Constructs a ProductRepository with the given ProductDao, initializing
     * the background executor for asynchronous operations.
     *
     * @param productDao The data access object for products.
     *#############################################################################
     */

    @Inject
    public ProductRepository(ProductDao productDao) {
        this.productDao = productDao;

    }

    /**
     * ##########################################################
     * Inserts a product into the data source asynchronously.
     *
     * @param product The product to be inserted.
     *############################################################
     */
    public LiveData<Boolean> insertProduct(Product product){
        MutableLiveData<Boolean> insertResult = new MutableLiveData<>();
       // executor.execute(() -> productDao.insertProduct(product));
        try {
            productDao.insertProduct(product);
            insertResult.setValue(true); // Successful insertion
        } catch (Exception e) {
            insertResult.setValue(false); // Error during insertion
            Log.e("InsertError", "Error inserting data: " + e.getMessage());
        }

        return insertResult;

    }
    public LiveData<List<Product>> getAllProducts(){

        return productDao.getAllProducts();
    }

}
