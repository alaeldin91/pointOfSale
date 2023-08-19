package com.example.pointofsale.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.example.pointofsale.ui.fragment.OrderHistoryFragment;
import com.example.pointofsale.ui.fragment.ProductFragment;
import com.example.pointofsale.ui.fragment.SettingFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int numOfTabs;
    public PagerAdapter(@NonNull FragmentManager fm,int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;

    }

    /**
     * this Function to Get tab By Position
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

                case 0:
                SettingFragment settingFragment = new SettingFragment();
                return settingFragment;

                case 1:
                OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
                return orderHistoryFragment;

                case 2:
                ProductFragment productFragment = new ProductFragment();
                return  productFragment;

                default:
                return null;
        }

    }

    /**
     * this function get Count Tab
     * @return
     */
    @Override
    public int getCount() {
        return numOfTabs;
    }
}
