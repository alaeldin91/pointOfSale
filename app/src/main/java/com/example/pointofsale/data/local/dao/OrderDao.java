package com.example.pointofsale.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.pointofsale.data.remote.model.Order;

import java.util.List;

/**
 * Data Access Object (DAO) interface for the 'orderTable' database table.
 * Defines methods for performing database operations related to orders.
 */
@Dao
public interface OrderDao {

    /**
     * Inserts an order into the 'orderTable' database table.
     *
     * @param order The order to be inserted.
     */
    @Insert
    void insertOrder(Order order);

    /**
     * Retrieves all orders from the 'orderTable' database table.
     *
     * @return A LiveData object containing a list of all orders.
     */
    @Query("SELECT * FROM orderTable")
    LiveData<List<Order>> getAllOrders();
}

