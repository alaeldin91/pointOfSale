package com.example.pointofsale.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.pointofsale.data.remote.model.Order;
import com.example.pointofsale.databinding.FragmentOrderHistoryBinding;
import com.example.pointofsale.ui.viewmodel.OrderViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OrderHistoryFragment extends Fragment {
    FragmentOrderHistoryBinding fragmentOrderHistoryBinding;
    private OrderViewModel orderViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentOrderHistoryBinding = FragmentOrderHistoryBinding.inflate(inflater, container, false);
        return fragmentOrderHistoryBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        getAllOrders();
    }

    public void getAllOrders(){
        orderViewModel.getAllOrders().observe(getViewLifecycleOwner(), orders -> {
           for (Order order : orders){
               Log.i("hajis",order.getQuantity()+"    "+order.getTotal());
           }
        });
    }
}