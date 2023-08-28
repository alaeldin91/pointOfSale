package com.example.pointofsale.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pointofsale.R;
import com.example.pointofsale.data.remote.model.TempOrder;
import com.example.pointofsale.ui.adapters.TempOrderAdapter;
import com.example.pointofsale.ui.viewmodel.OrderViewModel;
import com.example.pointofsale.ui.viewmodel.TempOrderViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class OrderListFragment extends DialogFragment{
    List<TempOrder> tempOrders = new ArrayList<>();
    private TempOrderViewModel tempOrderViewModel;
    private  RecyclerView tempOrderRecycler;
    private TextView discountTxt;
    private TextView taxTxt;
    private TextView subTotalTxt;
    private TextView totalTxt;
    private  TextView sumTxt;
    private  TempOrderAdapter tempOrderAdapter;

  private  ImageButton close;
    private CompositeDisposable disposables = new CompositeDisposable();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order_list, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tempOrderRecycler = view.findViewById(R.id.tableRecyclerTempOrder);
        discountTxt = view.findViewById(R.id.discountEdt);
        taxTxt = view.findViewById(R.id.taxEdt);
        subTotalTxt = view.findViewById(R.id.subTotalEdt);
        totalTxt = view.findViewById(R.id.totalEdt);
        sumTxt = view.findViewById(R.id.sumEdt);
        tempOrderRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        tempOrderViewModel = new ViewModelProvider(getActivity()).get(TempOrderViewModel.class);
        tempOrderAdapter = new TempOrderAdapter(getActivity(),new ArrayList<>(tempOrders),tempOrderViewModel,getViewLifecycleOwner());
        tempOrderRecycler.setAdapter(tempOrderAdapter);
        tempOrderAdapter.attachSwipeHelper(tempOrderRecycler);
        close = view.findViewById(R.id.close);
        // Set a click listener for the "Close" button to dismiss the dialog
        getAllTempOrders();
        closeDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        tempOrderAdapter.attachSwipeHelper(tempOrderRecycler);
        getDiscountLiveData();
        getTaxLiveData();
        getSubTotalLiveDat();
        getSubTotalLiveDat();
        getSumLiveData();
        geTotalLiveData();
    }

    public void closeDialog(){
        close.setOnClickListener(v ->dismiss());
    }

    public void getSubTotalLiveDat(){

           tempOrderViewModel.getSubTotalLiveData().observe(getViewLifecycleOwner(),subTotal->{
            subTotalTxt.setText(String.valueOf(subTotal));
        });
    }
    public void getTaxLiveData(){

        tempOrderViewModel.getTaxLiveData().observe(getViewLifecycleOwner(),tax->{
            taxTxt.setText(String.valueOf(tax));
        });
    }

    public void getSumLiveData(){
        tempOrderViewModel.getSum().observe(getViewLifecycleOwner(),sum->{
            sumTxt.setText(String.valueOf(sum));
        });
    }

    public void getDiscountLiveData() {
        tempOrderViewModel.getDiscountLiveData().observe(getViewLifecycleOwner(), discount -> {
           discountTxt.setText(String.valueOf(discount));
                }
        );
    }

    public void geTotalLiveData(){
        tempOrderViewModel.getTotal().observe(getViewLifecycleOwner(),total->{
            totalTxt.setText(String.valueOf(total));
        });
    }
    public void getAllTempOrders(){
             disposables.add(
                tempOrderViewModel.getTempOrders().observeOn(AndroidSchedulers.mainThread())
                        .subscribe(tempOrders->{
                            ArrayList<TempOrder> arrayList = new ArrayList<>(tempOrders) ;
                                    tempOrderAdapter.updateList(arrayList);
                                    Toast.makeText(getContext(),tempOrders.get(3).getId(),Toast.LENGTH_LONG).show();
                                },
                                throwable -> {

                                }
                        )
            );

        }
    }

