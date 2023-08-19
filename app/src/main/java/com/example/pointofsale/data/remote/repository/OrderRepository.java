package com.example.pointofsale.data.remote.repository;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.pointofsale.data.local.dao.OrderDao;
import com.example.pointofsale.data.remote.model.Order;
import java.util.List;
import javax.inject.Inject;

/**
 * Repository class that provides access to the orders data.
 * Acts as an intermediary between data sources and the rest of the app.
 */
public class OrderRepository {
    private final OrderDao orderDao;

    @Inject
    public OrderRepository(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    /**
     * Inserts an order into the database.
     *
     * @param order The order to be inserted.
     * @return A LiveData<Boolean> indicating the result of the insertion.
     */
    public LiveData<Boolean> insertOrder(Order order) {
        MutableLiveData<Boolean> insertResult = new MutableLiveData<>();
        try {
            orderDao.insertOrder(order);
            insertResult.setValue(true); // Successful insertion
        } catch (Exception e) {
            insertResult.setValue(false); // Error during insertion
            Log.e("InsertError", "Error inserting data: " + e.getMessage());
        }
        return insertResult;
    }

    /**
     * Retrieves a LiveData<List<Order>> containing all orders from the database.
     *
     * @return LiveData<List<Order>> containing all orders.
     */
    public LiveData<List<Order>> getAllOrders() {
        return orderDao.getAllOrders();
    }
}
