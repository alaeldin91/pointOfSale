package com.example.pointofsale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import com.example.pointofsale.ui.adapters.PagerAdapter;
import com.google.android.material.tabs.TabLayout;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * ######################################
 * Wrote By Alaeldin Abo Hanna
 * This is the main activity for your app
 * ######################################
 */

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    // This method is called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initlize Tab Layout and Create tab
        TabLayout tabLayout = findViewById(R.id.tab);
        // Initlize View Pager
        final ViewPager viewPager = findViewById(R.id.pager);
        viewPagerAndTabLayoutHandling(viewPager, tabLayout);
    }

    /**
     * ###############################################################
     * Sets up the ViewPager and TabLayout for handling tabbed content.
     * @param viewPager The ViewPager widget to display tabbed content.
     * @param tabLayout The TabLayout widget for tab navigation.
     * ###############################################################
     */
    public void viewPagerAndTabLayoutHandling(ViewPager viewPager, TabLayout tabLayout) {
        // Create a PagerAdapter with the given FragmentManager and tab count
        configureAndPopulateTabLayout(tabLayout);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());
        // Set the PagerAdapter to the ViewPager
        viewPager.setAdapter(pagerAdapter);
        // Add a listener to the ViewPager for automatic tab synchronization
        viewPager.addOnPageChangeListener(new TabLayout.
                TabLayoutOnPageChangeListener(tabLayout));
        // Attach a click listener to the TabLayout for manual tab switching
        clickListenerTabLayout(tabLayout, viewPager);

    }

    /**
     * ##########################################################################
     * Sets up a click listener for the TabLayout to enable manual tab switching.
     * @param tabLayout The TabLayout widget for tab navigation.
     * @param viewPager The ViewPager widget associated with the TabLayout.
     *############################################################################
     */
    public void clickListenerTabLayout(TabLayout tabLayout, ViewPager viewPager) {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab item) {
                // When a tab is selected, set the current item of the ViewPager to the selected tab position
                viewPager.setCurrentItem(item.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Do nothing when a tab is unselected

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Do nothing when a tab is reselected
            }
        });

    }

    /**
     * #####################################################################
     * Configures and populates a TabLayout with tabs and icons.
     * @param tabLayout The TabLayout widget to be configured and populated.
     * @return The configured and populated TabLayout.
     * #######################################################################
     */
    public TabLayout configureAndPopulateTabLayout(TabLayout tabLayout) {
        // Add tabs with text and icons to the TabLayout
        tabLayout.addTab(tabLayout.newTab().setText(R.string.settings)
                .setIcon(R.drawable.settings_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.orders)
                .setIcon(R.drawable.border_bottom_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.product)
                .setIcon(R.drawable.baseline_dataset_24));
        // Set the tab gravity to evenly distribute the tabs within the layout
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // Return the configured and populated TabLayout
        return tabLayout;
    }
}