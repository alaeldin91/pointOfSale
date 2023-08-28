package com.example.pointofsale.ui.adapters;

import static java.lang.Math.round;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewTreeLifecycleOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pointofsale.data.remote.model.Product;
import com.example.pointofsale.data.remote.model.TempOrder;
import com.example.pointofsale.databinding.ListItemTempOrderBinding;
import com.example.pointofsale.ui.viewmodel.TempOrderViewModel;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;

public class TempOrderAdapter extends RecyclerView.Adapter<TempOrderAdapter.ViewHolderTempOrder> {
    private Context context;
    private int counterTempOrder = 1;
    private TempOrder tempOrder;
    private LifecycleOwner lifecycleOwner;

    private TempOrderViewModel tempOrderViewModel;

    ArrayList<TempOrder> tempOrders;
    ListItemTempOrderBinding tempOrderBinding;

    public TempOrderAdapter(Context context, ArrayList<TempOrder> tempOrders, TempOrderViewModel tempOrderViewModel, LifecycleOwner lifecycleOwner) {
        this.context = context;
        this.tempOrders = tempOrders;
        this.tempOrderViewModel = tempOrderViewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public TempOrderAdapter.ViewHolderTempOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        tempOrderBinding = ListItemTempOrderBinding.inflate(layoutInflater, parent, false);
        return new ViewHolderTempOrder(tempOrderBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull TempOrderAdapter.ViewHolderTempOrder holder, int position) {
        tempOrder = tempOrders.get(position);
        Bundle args = new Bundle();
        args.putString("temp_id", String.valueOf(position));
        holder.tempOrderBinding.productName.setText(tempOrders.get(position).getProductName());
        holder.tempOrderBinding.price.setText(String.valueOf(tempOrders.get(position).getPrice()));
        holder.tempOrderBinding.quantity.setText(String.valueOf(tempOrders.get(position).getQuantity()));
        holder.tempOrderBinding.discount.setText(String.valueOf(tempOrders.get(position).getDiscount()));
        holder.tempOrderBinding.tax.setText(String.valueOf(tempOrders.get(position).getTax()));
        holder.tempOrderBinding.subTotal.setText(String.valueOf(tempOrders.get(position).getSubTotal()));
       // holder.tempOrderBinding.hash.setText(String.valueOf(counterTempOrder++));
        /**holder.tempOrderBinding.plus.setOnClickListener(v -> {
            int currentQuantity = tempOrders.get(position).getQuantity()+1;

            Log.i("hello",round(currentQuantity,1 )+"  "+ tempOrder.getProductName());
            tempOrder.setQuantity(currentQuantity + 1);
            notifyDataSetChanged();

            // holder.tempOrderBinding.quantity.setText(String.valueOf(tempOrder.getQuantity()));
          //  double newSubTotal = tempOrder.getQuantity() * tempOrder.getPrice();
           // holder.tempOrderBinding.subTotal.setText(String.valueOf(newSubTotal));
            //tempOrder.setSubTotal(newSubTotal);
          //  tempOrderViewModel.calculateAndUpdateValues(tempOrders);
            notifyDataSetChanged();

        });**/

        /**holder.tempOrderBinding.minuse.setOnClickListener(v -> {
            int currentQuantity = tempOrders.get(position).getQuantity();
            tempOrder.setQuantity(currentQuantity - 1);
            holder.tempOrderBinding.quantity.setText(String.valueOf(tempOrder.getQuantity()));
            //double newSubTotal = tempOrder.getQuantity() * tempOrder.getPrice();
            //holder.tempOrderBinding.subTotal.setText(String.valueOf(newSubTotal));
            //tempOrder.setSubTotal(newSubTotal);
            tempOrderViewModel.calculateAndUpdateValues(tempOrders);
            notifyDataSetChanged();
        });**/
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    private final ItemTouchHelper.Callback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return true;
        }

        @SuppressLint("CheckResult")
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Observable.timer(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(timer -> {
                tempOrderViewModel.deleteTempOrder(tempOrder.getId()).observe(lifecycleOwner, success -> {
                    if (position >= 0 && position < tempOrders.size()) {
                        if (success) {
                            tempOrders.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Successfully Delete Item" + "  " +
                                    tempOrder.getProductName(), Toast.LENGTH_LONG).show();
                            tempOrderViewModel.calculateAndUpdateValues(tempOrders);
                            notifyDataSetChanged();
                        }
                    } else {
                        Log.i("delete", " Is not Success Delete ");

                    }

                });
            });

        }
    };


    public void attachSwipeHelper(RecyclerView recyclerView) {
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return tempOrders == null ? 0 : tempOrders.size();

    }

    public class ViewHolderTempOrder extends RecyclerView.ViewHolder {
        ListItemTempOrderBinding tempOrderBinding;

        public ViewHolderTempOrder(@NonNull ListItemTempOrderBinding tempOrderBinding) {
            super(tempOrderBinding.getRoot());
            this.tempOrderBinding = tempOrderBinding;

            tempOrderBinding.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    tempOrder = tempOrders.get(position);
                    int currentQuantity = tempOrder.getQuantity();
                    tempOrder.setQuantity(currentQuantity+1);
                    double newSubTotal = tempOrder.getQuantity() * tempOrder.getPrice();
                      tempOrder.setSubTotal(newSubTotal);
                    Log.i("musa",currentQuantity+1+" "+tempOrder.getProductName()+"  "+newSubTotal);
                    tempOrderViewModel.calculateAndUpdateValues(tempOrders);
                    notifyDataSetChanged();
                }
            });

            tempOrderBinding.minuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    tempOrder = tempOrders.get(position);
                    int currentQuantity = tempOrder.getQuantity();
                    tempOrder.setQuantity(currentQuantity - 1);
                    double newSubTotal = tempOrder.getQuantity() * tempOrder.getPrice();
                    tempOrder.setSubTotal(newSubTotal);
                    Log.i("musa",currentQuantity -1 +" "+tempOrder.getProductName()+"  "+newSubTotal);
                    tempOrderViewModel.calculateAndUpdateValues(tempOrders);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<TempOrder> tempOrders) {
        this.tempOrders = tempOrders;
        notifyDataSetChanged();
    }
}
