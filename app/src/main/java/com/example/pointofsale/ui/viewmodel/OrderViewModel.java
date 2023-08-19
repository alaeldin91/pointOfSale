package com.example.pointofsale.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.pointofsale.data.remote.model.Order;
import com.example.pointofsale.data.remote.repository.OrderRepository;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel class for managing Order-related UI data.
 */
@HiltViewModel
public class OrderViewModel extends ViewModel {

    private final OrderRepository orderRepository;

    @Inject
    public OrderViewModel(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    /**
     * Inserts an order into the repository.
     *
     * @param order The order to be inserted.
     * @return LiveData<Boolean> indicating the result of the insertion.
     */
    public LiveData<Boolean> insertOrder(Order order){
        return this.orderRepository.insertOrder(order);
    }

    /**
     * Retrieves LiveData<List<Order>> containing all orders.
     *
     * @return LiveData<List<Order>> containing all orders.
     */
    public LiveData<List<Order>> getAllOrders(){
        return orderRepository.getAllOrders();
    }
}
