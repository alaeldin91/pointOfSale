package com.example.pointofsale.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.pointofsale.data.remote.model.Product;
import java.util.List;


/**
 * Data Access Object (DAO) interface for the 'productTable' database table.
 * Defines methods for performing database operations related to products.
 */
@Dao
public interface ProductDao {

    /**
     * Insert a new product into the 'productTable' database table.
     *
     * @param product The product to be inserted.
     */
    @Insert
    void insertProduct(Product product);

    /**
     * Update an existing product in the 'productTable' database table.
     *
     * @param product The updated product information.
     */
    @Update
    void updateProduct(Product product);

    /**
     * Delete a product from the 'productTable' database table.
     *
     * @param product The product to be deleted.
     */
    @Delete
    void deleteProduct(Product product);

    /**
     * Retrieve a product by its unique ID from the 'productTable' database table.
     *
     * @param productId The ID of the product to retrieve.
     * @return A LiveData object containing the retrieved product.
     */
    @Query("SELECT * FROM productTable WHERE id = :productId")
    LiveData<Product> getProductById(int productId);

    /**
     * Retrieve all products from the 'productTable' database table.
     *
     * @return A LiveData object containing a list of all products.
     */
    @Query("SELECT * FROM productTable")
    LiveData<List<Product>> getAllProducts();
}
