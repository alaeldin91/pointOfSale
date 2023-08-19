package com.example.pointofsale.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.pointofsale.data.remote.model.TempOrder;
import java.util.List;

/**
 * Data Access Object (DAO) interface for the 'tempOrder' database table.
 * Defines methods for performing database operations related to temporary orders.
 */
@Dao
public interface TempOrderDao {

    /**
     * Insert a new temporary order into the 'tempOrder' database table.
     *
     * @param tempOrder The temporary order to be inserted.
     */
    @Insert
    void insertTempOrder(TempOrder tempOrder);

    /**
     * Update an existing temporary order in the 'tempOrder' database table.
     *
     * @param tempOrder The updated temporary order information.
     */
    @Update
    void updateTempOrder(TempOrder tempOrder);

    /**
     * Retrieve the count of temporary orders in the 'tempOrder' database table.
     *
     * @return A LiveData object containing the count of temporary orders.
     */
    @Query("SELECT COUNT(*) FROM tempOrder")
    LiveData<Integer> getTempCount();

    /**
     * Delete a temporary order from the 'tempOrder' database table.
     *
     * @param tempOrder The temporary order to be deleted.
     */
    @Delete
    void deleteTempOrder(TempOrder tempOrder);

    /**
     * Retrieve a temporary order by its unique ID from the 'tempOrder' database table.
     *
     * @param orderId The ID of the temporary order to retrieve.
     * @return A LiveData object containing the retrieved temporary order.
     */
    @Query("SELECT * FROM tempOrder WHERE id = :orderId")
    LiveData<TempOrder> getTempOrderById(int orderId);

    /**
     * Retrieve all temporary orders from the 'tempOrder' database table.
     *
     * @return A LiveData object containing a list of all temporary orders.
     */
    @Query("SELECT * FROM tempOrder")
    LiveData<List<TempOrder>> getAllTempOrders();

    /**
     * Delete a temporary order by its ID from the 'tempOrder' database table.
     *
     * @param orderId The ID of the temporary order to delete.
     */
    @Query("DELETE FROM tempOrder WHERE id = :orderId")
    void deleteTempOrderById(int orderId);

    /**
     * Check if a product with the given product name exists in the 'tempOrder' table.
     *
     * @param productName The name of the product to check for existence.
     * @return The count of products with the given name (should be 0 or 1).
     */
    @Query("SELECT COUNT(*) FROM tempOrder WHERE productName = :productName")
    int doesProductNameExist(String productName);
}
