package com.example.pointofsale.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.pointofsale.data.remote.model.TempOrder;
import com.example.pointofsale.data.remote.repository.TempOrderRepository;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Observable;

/**
 * ViewModel class for managing temporary orders (shopping cart) related UI data.
 */
@HiltViewModel
public class TempOrderViewModel extends ViewModel {
    private MutableLiveData<Double> discountLiveData = new MutableLiveData<>();
    private MutableLiveData<Double> taxLiveData = new MutableLiveData<>();

    private MutableLiveData<Double>  subTotalLiveData = new MutableLiveData<>();

    private MutableLiveData<Double> totalLiveData = new MutableLiveData<>();

    private MutableLiveData<Double> sumLiveData = new MutableLiveData<>();

    private final TempOrderRepository tempOrderRepository;

    @Inject
    public TempOrderViewModel(TempOrderRepository tempOrderRepository) {
        this.tempOrderRepository = tempOrderRepository;
    }

    public LiveData<Boolean> deleteTempOrder(int id){

        return this.tempOrderRepository.deleteTempOrder(id);
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
    public Observable<List<TempOrder>> getTempOrders(){
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

    public LiveData<Double> getDiscountLiveData(){

        return discountLiveData;
    }

    public LiveData<Double> getTaxLiveData(){
        return taxLiveData;
    }

    public  LiveData<Double> getSubTotalLiveData(){
        return subTotalLiveData;
    }

    public LiveData<Double> getTotal(){
        return  totalLiveData;
    }

    public LiveData<Double>getSum(){

        return sumLiveData;
    }


    public void calculateAndUpdateValues(List<TempOrder> tempOrders){
        double discount = 0.0;
        double tax = 0.0;
        double total = 0.0;
        double subTotal = 0.0;
        double sum = 0.0;

        for (TempOrder tempOrder : tempOrders){
            double itemTotal = tempOrder.getSubTotal();
            subTotal += itemTotal;
            sum += itemTotal;
             discount += tempOrder.getDiscount();
             tax += tempOrder.getTax();
             total = subTotal - discount;
             if (tax !=0){
             total += tax;
         }
         discountLiveData.setValue(discount);
         taxLiveData.setValue(tax);
         subTotalLiveData.setValue(subTotal);
         totalLiveData.setValue(total);
         sumLiveData.setValue(sum);

        }

    }
}
