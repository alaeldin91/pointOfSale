package com.example.pointofsale.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.pointofsale.data.remote.model.TempOrder;
import com.example.pointofsale.data.remote.repository.TempOrderRepository;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel class for managing temporary orders (shopping cart) related UI data.
 */
@HiltViewModel
public class TempOrderViewModel extends ViewModel {

    private final TempOrderRepository tempOrderRepository;

    @Inject
    public TempOrderViewModel(TempOrderRepository tempOrderRepository) {
        this.tempOrderRepository = tempOrderRepository;
    }

    /**
     * Inserts a temporary order (shopping cart item) into the repository.
     *
     * @param tempOrder The temporary order to be inserted.
     * @return LiveData<Boolean> indicating the result of the insertion.
     */
    public LiveData<Boolean> insertTempOrder(TempOrder tempOrder){
        return this.tempOrderRepository.insertTempOrder(tempOrder);
    }

    /**
     * Retrieves LiveData<Integer> containing the count of temporary orders (shopping cart items).
     *
     * @return LiveData<Integer> containing the count of temporary orders.
     */
    public LiveData<Integer> getTempOrderCount() {
        return tempOrderRepository.getTempOrderCount();
    }

    /**
     * Checks if a product with the given name exists in the temporary orders.
     *
     * @param productName The name of the product to check.
     * @return true if the product exists, false otherwise.
     */
    public boolean doesProductNameExist(String productName) {
        return tempOrderRepository.doesProductNameExist(productName);
    }

    /**
     * Retrieves LiveData<List<TempOrder>> containing all temporary orders (shopping cart items).
     *
     * @return LiveData<List<TempOrder>> containing all temporary orders.
     */
    public LiveData<List<TempOrder>> getTempOrders(){
        return tempOrderRepository.getAllTempOrders();
    }

    /**
     * Updates a temporary order (shopping cart item) in the repository.
     *
     * @param tempOrder The temporary order to be updated.
     */
    public void updateTempOrder(TempOrder tempOrder) {
        tempOrderRepository.updateTempOrder(tempOrder);
    }
}
