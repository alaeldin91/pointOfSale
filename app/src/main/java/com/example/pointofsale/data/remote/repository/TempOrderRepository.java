package com.example.pointofsale.data.remote.repository;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.pointofsale.data.local.dao.TempOrderDao;
import com.example.pointofsale.data.remote.model.TempOrder;
import java.util.List;
import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Repository class that provides access to the temporary orders data.
 * Acts as an intermediary between data sources and the rest of the app.
 */
public class TempOrderRepository {

    private final TempOrderDao tempOrderDao;

    @Inject
    public TempOrderRepository(TempOrderDao tempOrderDao){
        this.tempOrderDao = tempOrderDao;
    }


    public LiveData<Boolean> deleteTempOrder(int id){
        MutableLiveData<Boolean> deleteResult = new MutableLiveData<>();

        try{
            tempOrderDao.deleteTempOrderById(id);
            deleteResult.setValue(true);

        }
        catch (Exception e){
            deleteResult.setValue(false);
            Log.e("Delete Error", "Error Delete data: " + e.getMessage());

        }

        return deleteResult;
    }
    /**
     * Inserts a temporary order into the database.
     *
     * @param tempOrder The temporary order to be inserted.
     * @return A LiveData<Boolean> indicating the result of the insertion.
     */
    public LiveData<Boolean> insertTempOrder(TempOrder tempOrder){
        MutableLiveData<Boolean> insertResult = new MutableLiveData<>();

        try {
            tempOrderDao.insertTempOrder(tempOrder);
            insertResult.setValue(true); // Successful insertion
        } catch (Exception e) {
            insertResult.setValue(false); // Error during insertion
            Log.e("InsertError", "Error inserting data: " + e.getMessage());
        }

        return insertResult;
    }

    /**
     * Retrieves a LiveData<Integer> representing the count of temporary orders.
     *
     * @return LiveData<Integer> representing the count of temporary orders.
     */
    public LiveData<Integer> getTempOrderCount() {
        return tempOrderDao.getTempCount();
    }

    /**
     * Checks if a product with the given name exists in temporary orders.
     *
     * @param productName The name of the product.
     * @return True if the product exists, false otherwise.
     */
    public boolean doesProductNameExist(String productName) {
        int count = tempOrderDao.doesProductNameExist(productName);
        return count > 0;
    }

    /**
     * Retrieves a LiveData<List<TempOrder>> containing all temporary orders.
     *
     * @return LiveData<List<TempOrder>> containing all temporary orders.
     */
    public Observable<List<TempOrder>> getAllTempOrders(){
        return tempOrderDao.getAllTempOrders().subscribeOn(Schedulers.io());
    }

    /**
     * Updates a temporary order in the database.
     *
     * @param tempOrder The updated temporary order.
     */
    public void updateTempOrder(TempOrder tempOrder){
        tempOrderDao.updateTempOrder(tempOrder);
    }

    /**
     * Retrieves a LiveData<TempOrder> for the temporary order with the given ID.
     *
     * @param tempOrderId The ID of the temporary order.
     * @return LiveData<TempOrder> for the temporary order with the given ID.
     */
    public LiveData<TempOrder> getTempOrderById(int tempOrderId) {
        return tempOrderDao.getTempOrderById(tempOrderId);
    }
}
