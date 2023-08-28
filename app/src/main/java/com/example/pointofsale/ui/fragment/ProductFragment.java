package com.example.pointofsale.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pointofsale.R;
import com.example.pointofsale.data.remote.model.Order;
import com.example.pointofsale.data.remote.model.Product;
import com.example.pointofsale.data.remote.model.ShoppingCart;
import com.example.pointofsale.data.remote.model.TempOrder;
import com.example.pointofsale.databinding.FragmentProductBinding;
import com.example.pointofsale.databinding.TableTempOrderBinding;
import com.example.pointofsale.ui.adapters.ProductAdapter;
import com.example.pointofsale.ui.adapters.TempOrderAdapter;
import com.example.pointofsale.ui.viewmodel.OrderViewModel;
import com.example.pointofsale.ui.viewmodel.ProductViewModel;
import com.example.pointofsale.ui.viewmodel.TempOrderViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

/**
 * #################################################
 * Wrote By Alaeldin Musa
 * Product Fragment
 * #################################################
 */

@AndroidEntryPoint
public class ProductFragment extends Fragment implements ProductAdapter.onItemClickProduct {

    // Initializing the binding object for the product fragment view elements.
    FragmentProductBinding fragmentProductBinding;
    Dialog dialog;
    FloatingActionButton floatingActionButton;
    private ProductViewModel productViewModel;

    ArrayList<Product> products;

    List<TempOrder> tempOrders = new ArrayList<>();
    private ProductAdapter productAdapter;

    private TempOrderAdapter tempOrderAdapter;

    private TempOrderViewModel tempOrderViewModel;

    private OrderViewModel orderViewModel;

    private CompositeDisposable disposables = new CompositeDisposable();


    /**
     * ##################################################################################################
     * Called when the fragment's view should be created.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views
     *                           in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI
     *                           should be attached to. The fragment should not add the view
     *                           itself, but this can be used to generate the LayoutParams of the
     *                           view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a
     *                           previous saved state as given here.
     * @return The inflated view hierarchy of the fragment's layout.
     * ################################################################################################
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        fragmentProductBinding = FragmentProductBinding.inflate(inflater, container, false);

        this.productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        this.tempOrderViewModel = new ViewModelProvider(this).get(TempOrderViewModel.class);
        this.orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        // Initialize UI elements and setup event listeners.
        DefinitionView();
        clickListenerFlatButton(floatingActionButton);
        intilizeRecyclerViewProduct();
        getAllProducts();
        cardClickListener();
        return fragmentProductBinding.getRoot();
    }

    public void DefinitionView() {
        floatingActionButton = fragmentProductBinding.floating;
    }

    /**
     * ############################################################################################
     * Called when the fragment's view has been created and is ready for UI initialization and
     * event handling.
     *
     * @param view               The root view of the fragment's layout.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here.
     *###################################################################################################
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    /**
     * #####################################################################################
     * Sets a click listener for a FloatingActionButton.
     *
     * @param floatingActionButton The FloatingActionButton to attach the click listener to.
     *                             #########################################################################################
     */
    public void clickListenerFlatButton(FloatingActionButton floatingActionButton) {
        floatingActionButton.setOnClickListener(v -> {
            createDialog(R.layout.addproduct);
            intilizeDialogAddProduct();
        });


    }

    /**
     * Initializes the add product dialog by finding its elements and setting up click listeners.
     */
    public void intilizeDialogAddProduct() {
        // Find the "Cancel" and "Save" buttons in the dialog
        Button cancel = dialog.findViewById(R.id.cancel);
        Button save = dialog.findViewById(R.id.save);

        // Find the input fields for product name and price
        TextInputEditText edtProductName = dialog.findViewById(R.id.productNameEdt);
        TextInputEditText edtProductPrice = dialog.findViewById(R.id.priceEdt);

        // Set a click listener for the "Save" button
        save.setOnClickListener(v -> SaveDialogListener(edtProductName, edtProductPrice));

        // Set a click listener for the "Cancel" button to dismiss the dialog
        cancel.setOnClickListener(v -> dialog.dismiss());
    }

    /**
     * Displays a dialog for adding a new product. The dialog includes input fields for
     * the product name and price, along with buttons to save or cancel the operation.
     * If the associated Activity is not null, the dialog is initialized and shown.
     */
    private void createDialog(int id) {

        Activity activity = getActivity();

        if (activity != null) {

            dialog = new Dialog(getActivity());

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

            if(R.layout.addproduct == id) {
                handleDialog(lp);

            }



        }
    }

    /**
     * Handles the click event of the "Save" button in the dialog for adding a new product.
     *
     * @param edtProductName  The input field for entering the product name.
     * @param edtProductPrice The input field for entering the product price.
     */
    public void SaveDialogListener(TextInputEditText edtProductName, TextInputEditText edtProductPrice) {
        // Get the entered product name and price as strings
        String productName = Objects.requireNonNull(edtProductName.getText()).toString();
        String price = Objects.requireNonNull(edtProductPrice.getText()).toString();

        double priceNumber;

        try {
            // Convert the price string to a double value
            priceNumber = Double.parseDouble(price);

            // Validate the input fields
            if (validateFields(edtProductName, edtProductPrice)) {
                // Insert the product with the entered name and price
                insertProduct(productName, priceNumber);
                // Close the dialog
                dialog.dismiss();
            }

        } catch (NumberFormatException ignored) {
            // Handle the exception if parsing fails
            // (e.g., when the entered price is not a valid number)
        }
    }


    /**
     * Initializes the RecyclerView for displaying the list of products.
     * Sets up the layout manager, adapter, and binds the adapter to the RecyclerView.
     */

    public void intilizeRecyclerViewProduct() {
        // Set up a GridLayoutManager with 3 columns, vertical orientation, and no reverse layout
        fragmentProductBinding.recyclerProductCart
                .setLayoutManager(new GridLayoutManager(getActivity(),
                3, GridLayoutManager.VERTICAL, false));

        // Create a ProductAdapter with the list of products and the item click listener
        productAdapter = new ProductAdapter(getActivity(), products, this);
        Log.i("PRODUCT",products+"");
        // Bind the ProductAdapter to the RecyclerView
        fragmentProductBinding.recyclerProductCart.setAdapter(productAdapter);
    }

    /**
     * Observes the list of all products from the ViewModel and updates the RecyclerView's adapter
     * with the new list if products are available.
     */

    public void getAllProducts() {
        // Observe the list of products from the ViewModel
        productViewModel.getAllProducts().observe(getViewLifecycleOwner(), products -> {
            // Convert the LiveData list to an ArrayList
            ArrayList<Product> productArrayList = new ArrayList<>(products);

            // If there are products available, update the RecyclerView's adapter
            if (products.size() > 0) {

                productAdapter.updateList(productArrayList);
            }
        });
    }
    public void getTempOrderCount() {
        // Find the TextView element for displaying the count
        TextView countText = requireActivity().findViewById(R.id.txtCount);

        // Observe the count of temporary orders from the ViewModel
        tempOrderViewModel.getTempOrderCount().observe(getViewLifecycleOwner(), tempOrderCount -> {
            Log.i("alaTemp",tempOrderCount+"");
            if (tempOrderCount > 0) {
                // If there are temporary orders, display the count and make the TextView visible
                countText.setVisibility(View.VISIBLE);
                countText.setText(tempOrderCount + " ");
            } else {
                // If there are no temporary orders, hide the count TextView
                countText.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Displays an AlertDialog to indicate successful insertion of a product.
     * The dialog includes a title, message, "OK" button, and a success icon.
     */
    private void showSuccesfullyDialog() {
        // Create an AlertDialog.Builder instance
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the title and message for the dialog
        builder.setTitle("Successfully")
                .setMessage("Successfully Inserted Product")

                // Set the "OK" button with a dismiss action
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())

                // Set an icon representing success
                .setIcon(R.drawable.baseline_check_24)

                // Show the dialog
                .show();
    }

    /**
     * Displays an AlertDialog to indicate an error during product insertion.
     * The dialog includes a title, message, "OK" button, and an error icon.
     */
    private void showErrorDialog() {
        // Create an AlertDialog.Builder instance
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the title and message for the dialog
        builder.setTitle("Error Insert Data")
                .setMessage("Error Insert Product")

                // Set the "OK" button with a dismiss action
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())

                // Set an icon representing an error
                .setIcon(R.drawable.baseline_error_24)

                // Show the dialog
                .show();
    }
    public void handleDialog(WindowManager.LayoutParams lp) {
        // Set up the dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.addproduct);

        // Copy attributes from the dialog's window and customize dimensions
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        // Set the content view and show the dialog
        dialog.show();

        // Apply the updated attributes to the dialog's window
        dialog.getWindow().setAttributes(lp);
    }

    /**
     * Validates the entered product name and price fields.
     *
     * @param productNameEdt  The input field for the product name.
     * @param productPriceEdt The input field for the product price.
     * @return True if both fields are valid, false otherwise.
     */
    public boolean validateFields(TextInputEditText productNameEdt, TextInputEditText productPriceEdt) {
        String productName = Objects.requireNonNull(productNameEdt.getText()).toString();
        String price = Objects.requireNonNull(productPriceEdt.getText()).toString();

        try {
            // Check if product name is empty
            if (TextUtils.isEmpty(productName)) {
                productNameEdt.setError("Product Name is required");
                productNameEdt.requestFocus();
                return false;
            }

            // Check if price is empty
            if (TextUtils.isEmpty(price)) {
                productPriceEdt.setError("Price is required");
                productPriceEdt.requestFocus();
                return false;
            }
        } catch (NumberFormatException ignored) {
            // Handle the exception if parsing fails (usually for price)
            // Ignored because validation is the main concern
        }

        // Both fields are valid
        return true;
    }


    @Override
    public void onItemClickProduct(View view, int position, ArrayList<Product> products) {
        Product product = products.get(position);
        double tax = 0.0; // Set the appropriate tax value

        //tax is equals = 0
        if (tax==0) {
            double discount = 0.0;
            insertTempOrder(product.getProductName(), product.getProductPrice(), tax, discount);
        }


    }

    /**
     * Inserts a new product into the database and handles success or failure.
     *
     * @param productName The name of the product to be inserted.
     * @param priceNumber The price of the product to be inserted.
     */
    public void insertProduct(String productName, double priceNumber) {
        // Create a new Product instance and set its attributes
        Product product = new Product();
        product.setProductName(productName);
        product.setProductPrice(priceNumber);

        // Observe the result of the product insertion operation
        productViewModel.insertProduct(product).observe(getViewLifecycleOwner(), success -> {
            if (success) {
                // If insertion is successful, display success dialog and log the success
                showSuccesfullyDialog();
                Log.i("musa", "Successfully Inserted: " + success.toString());
            } else {
                // If insertion fails, display error dialog
                showErrorDialog();
            }
        });
    }

    /**
     * Inserts a new temporary order into the database and handles success or failure.
     *
     * @param productName The name of the product in the temporary order.
     * @param price       The price of the product in the temporary order.
     * @param tax         The tax applied to the product in the temporary order.
     * @param discount    The discount applied to the product in the temporary order.
     */
    public void insertTempOrder(String productName, double price, double tax, double discount) {
        // Create a ShoppingCart instance to calculate subtotal
        ShoppingCart shoppingCart = new ShoppingCart(productName, price, 1, tax, discount);
        double subtotal = shoppingCart.getSubtotal();

        // Create a new TempOrder instance and set its attributes
        TempOrder tempOrder = new TempOrder();
        tempOrder.setProductName(productName);
        tempOrder.setPrice(price);
        tempOrder.setQuantity(1);
        tempOrder.setTax(tax);
        tempOrder.setDiscount(discount);
        tempOrder.setSubTotal(subtotal);

        // Check if the product already exists in the temporary orders
        boolean exists = tempOrderViewModel.doesProductNameExist(productName);
        Log.i("ala","ProductName"+productName);

        // Display a toast indicating whether the product already exists
        Toast.makeText(getActivity(), exists ? "This item is Already Added to Your Cart" : "Item Added to Cart"
                , Toast.LENGTH_LONG).show();

        if (!exists) {
            getTempOrderCount();

            // If the product doesn't exist, insert it into the temporary orders
            tempOrderViewModel.insertTempOrder(tempOrder).observe(getViewLifecycleOwner(), success -> {
                if (success) {
                    // If insertion is successful, update the temporary order count and display a toast
                    getTempOrderCount();
                    Toast.makeText(getActivity(), "Item Added to Cart", Toast.LENGTH_LONG).show();
                }
            });
        }

        else {

            // If the product already exists, display a toast indicating that it's in the cart
            Toast.makeText(getActivity(), "This item is Already Added to Your Cart", Toast.LENGTH_LONG).show();
        }
    }


    public void cardClickListener() {
        ImageView shoppingCart = getActivity().findViewById(R.id.shoppingCard);
        shoppingCart.setOnClickListener(v -> {
            OrderListFragment orderListFragment = new OrderListFragment();
            orderListFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
            orderListFragment.show(getActivity().getSupportFragmentManager(), "tempOrderFragment");


        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }


}
