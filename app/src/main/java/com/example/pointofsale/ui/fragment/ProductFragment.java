package com.example.pointofsale.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.pointofsale.ui.adapters.ProductAdapter;
import com.example.pointofsale.ui.viewmodel.OrderViewModel;
import com.example.pointofsale.ui.viewmodel.ProductViewModel;
import com.example.pointofsale.ui.viewmodel.TempOrderViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import dagger.hilt.android.AndroidEntryPoint;

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
    private ProductAdapter productAdapter;

    private TempOrderViewModel tempOrderViewModel;

    private OrderViewModel orderViewModel;

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
     *                           ###################################################################################################
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        this.tempOrderViewModel = new ViewModelProvider(this).get(TempOrderViewModel.class);
        this.orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        // Initialize UI elements and setup event listeners.
        DefinitionView();
        clickListenerFlatButton(floatingActionButton);
        intilizeRecyclerViewProduct();
        getAllProducts();
        cardClickListener();

    }

    /**
     * #####################################################################################
     * Sets a click listener for a FloatingActionButton.
     *
     * @param floatingActionButton The FloatingActionButton to attach the click listener to.
     *                             #########################################################################################
     */
    public void clickListenerFlatButton(FloatingActionButton floatingActionButton) {
        View view = inflateLayout(R.layout.addproduct);
        floatingActionButton.setOnClickListener(v -> {
            createDialog(view);
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
    private void createDialog(View view) {
        Activity activity = getActivity();
        if (activity != null) {
            dialog = new Dialog(getActivity());
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            handleDialog(lp, view);
            // Initialize UI elements

        }
    }


    /**
     * Inflates a layout resource and returns the corresponding View.
     *
     * @param layoutResId The resource ID of the layout to be inflated.
     * @return The inflated View representing the specified layout.
     */
    private View inflateLayout(int layoutResId) {
        LayoutInflater inflater = getLayoutInflater();
        return inflater.inflate(layoutResId, null);
    }

    /**
     * Initializes the temporary order table dialog by finding its close button
     * and setting up a click listener to dismiss the dialog when the button is clicked.
     */
    public void initializeTempOrderTable() {
        // Find the "Close" button in the temporary order table dialog
        ImageButton close = dialog.findViewById(R.id.close);

        // Set a click listener for the "Close" button to dismiss the dialog
        close.setOnClickListener(v -> dialog.dismiss());
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
        fragmentProductBinding.recyclerProductCart.setLayoutManager(new GridLayoutManager(getActivity(),
                3, GridLayoutManager.VERTICAL, false));

        // Create a ProductAdapter with the list of products and the item click listener
        productAdapter = new ProductAdapter(getActivity(), products, this);

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

    /**
     * Observes the count of temporary orders from the ViewModel and updates the UI to display the count.
     * If the count is greater than 0, the count is displayed; otherwise, the count is hidden.
     */
    public void getTempOrderCount() {
        // Find the TextView element for displaying the count
        TextView countText = requireActivity().findViewById(R.id.txtCount);

        // Observe the count of temporary orders from the ViewModel
        tempOrderViewModel.getTempOrderCount().observe(getViewLifecycleOwner(), tempOrderCount -> {
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

    /**
     * Configures and displays a dialog with the given layout parameters and content view.
     *
     * @param lp   The layout parameters for the dialog.
     * @param view The content view of the dialog.
     */
    public void handleDialog(WindowManager.LayoutParams lp, View view) {
        // Set up the dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);

        // Copy attributes from the dialog's window and customize dimensions
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        // Set the content view and show the dialog
        dialog.setContentView(view);
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
        String productName = "";
        double price = 0.0;
        double tax = 0.0;
        double discount = 0.0;
        for (Product product : products) {

            productName = product.getProductName();
            price = product.getProductPrice();
        }

        insertTempOrder(productName, price, tax, discount);
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

        // Display a toast indicating whether the product already exists
        Toast.makeText(getActivity(), exists ? "This item is Already Added to Your Cart" : "Item Added to Cart", Toast.LENGTH_LONG).show();

        if (!exists) {
            // If the product doesn't exist, insert it into the temporary orders
            tempOrderViewModel.insertTempOrder(tempOrder).observe(getViewLifecycleOwner(), success -> {
                if (success) {
                    // If insertion is successful, update the temporary order count and display a toast
                    getTempOrderCount();
                    Toast.makeText(getActivity(), "Item Added to Cart", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            // If the product already exists, display a toast indicating that it's in the cart
            Toast.makeText(getActivity(), "This item is Already Added to Your Cart", Toast.LENGTH_LONG).show();
        }
    }


    public void cardClickListener() {
        ImageView shoppingCart = getActivity().findViewById(R.id.shoppingCard);
        View view = inflateLayout(R.layout.table_temp_order);
        shoppingCart.setOnClickListener(v -> {
            createDialog(view);
            initializeTempOrderTable();
            generateTableAutomaticlly(view);
            handleGetAllTempOrders(view);

        });
    }

    /**
     * Populates the temporary order table with data fetched from the ViewModel.
     *
     * @param view The root view containing the temporary order table.
     */
    @SuppressLint("ResourceAsColor")
    public void handleGetAllTempOrders(View view) {
        // Find the table layout view from the provided root view
        TableLayout tableLayout = view.findViewById(R.id.table_layout);

        // Fetch and observe the list of temporary orders from the ViewModel
        tempOrderViewModel.getTempOrders().observe(getViewLifecycleOwner(), tempOrders -> {
            // Create buttons for increasing and decreasing quantities
            Button btnPlus = new Button(getActivity());
            Button btnMinus = new Button(getActivity());
            btnMinus.setText("-");
            btnPlus.setText("+");

            // Iterate through each temporary order and populate the table
            for (TempOrder tempOrder : tempOrders) {
                final int[] quantity = {tempOrder.getQuantity()};
                final double[] subTotal = {tempOrder.getSubTotal()};
                final double[] totals = {0.0};

                // Create and configure TextViews for quantity and subtotals
                TextView quantityTextView = new TextView(getActivity());
                TextView subTotalTextView = new TextView(getActivity());
                TextView textViewTotal = new TextView(getActivity());

                // Set up click listeners for the plus and minus buttons
                btnPlus.setOnClickListener(v -> {
                    quantity[0] = quantity[0] + 1;
                    quantityTextView.setText(String.valueOf(quantity[0]));
                    subTotal[0] = tempOrder.getSubTotal() * quantity[0];
                    subTotalTextView.setText(String.valueOf(subTotal[0]));
                    totals[0] += subTotal[0];
                    textViewTotal.setText(String.valueOf(totals[0]));
                });

                // Update TextViews with initial values
                quantityTextView.setText(String.valueOf(quantity[0]));
                subTotalTextView.setText(String.valueOf(subTotal[0]));
                textViewTotal.setText(String.valueOf(totals[0]));

                // Set up click listener for the minus button
                btnMinus.setOnClickListener(v -> {
                    quantity[0] = quantity[0] - 1;
                    subTotal[0] = tempOrder.getSubTotal() * quantity[0];
                    quantityTextView.setText(String.valueOf(quantity[0]));
                    subTotalTextView.setText(String.valueOf(subTotal[0]));
                    totals[0] += subTotal[0];
                    textViewTotal.setText(String.valueOf(totals[0]));
                });

                // Convert and handle total value parsing
                double parsedValue = 0;
                if (!textViewTotal.getText().toString().isEmpty()) {
                    try {
                        parsedValue = Double.parseDouble(textViewTotal.getText().toString());
                    } catch (NumberFormatException e) {
                        // Handle the exception if parsing fails
                    }
                }

                // Send order details and populate a TableRow
                sendOrder(view, tempOrder.getProductName(),
                        tempOrder.getPrice(),
                        Integer.parseInt(quantityTextView.getText().toString()), tempOrder.getTax(),
                        tempOrder.getDiscount(),
                        Double.parseDouble(subTotalTextView.getText().toString()),
                        Double.parseDouble(textViewTotal.getText().toString()));

                // Create and configure a TableRow to hold the data
                TableRow dataRow = new TableRow(getActivity());
                dataRow.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                LinearLayout quantityLayout = new LinearLayout(getActivity());
                LinearLayout subTotalQuantity = new LinearLayout(getActivity());
                subTotalQuantity.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout totalLinear = new LinearLayout(getActivity());
                totalLinear.setOrientation(LinearLayout.HORIZONTAL);
                quantityLayout.setOrientation(LinearLayout.HORIZONTAL);

                // Add data cells and views to the TableRow
                addDataCell(dataRow, tempOrder.getProductName());
                addDataCell(dataRow, String.valueOf(tempOrder.getPrice()));
                dataRow.addView(btnPlus);
                dataRow.addView(quantityTextView);
                dataRow.addView(btnMinus);
                addDataCell(dataRow, String.valueOf(tempOrder.getTax()));
                addDataCell(dataRow, String.valueOf(tempOrder.getDiscount()));
                dataRow.addView(subTotalTextView);
                dataRow.addView(textViewTotal);

                // Add the populated TableRow to the table layout
                tableLayout.addView(dataRow);
            }

            // Center-align the table layout contents
            tableLayout.setGravity(Gravity.CENTER);
        });
    }

    /**
     * Generates the automatic table header and adds it to the provided table layout.
     *
     * @param view The root view containing the table layout.
     */
    public void generateTableAutomaticlly(View view) {
        // Find the table layout view from the provided root view
        TableLayout tableLayout = view.findViewById(R.id.table_layout);

        // Create header row for the table
        TableRow headerRow = new TableRow(getActivity());
        headerRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        // Add header cells to the header row
        addHeaderCell(headerRow, getResources().getString(R.string.productName));
        addHeaderCell(headerRow, getResources().getString(R.string.price));
        addHeaderCell(headerRow, "Increase quantity");
        addHeaderCell(headerRow, getResources().getString(R.string.quantity));
        addHeaderCell(headerRow, "Decrease quantity");
        addHeaderCell(headerRow, getResources().getString(R.string.tax));
        addHeaderCell(headerRow, getResources().getString(R.string.discount));
        addHeaderCell(headerRow, getResources().getString(R.string.subTotal));
        addHeaderCell(headerRow, "Total");

        // Add the header row to the table layout
        tableLayout.addView(headerRow);
    }

    /**
     * Adds a header cell to the provided row in the table.
     *
     * @param row  The TableRow to which the header cell should be added.
     * @param text The text content of the header cell.
     */
    private void addHeaderCell(TableRow row, String text) {
        // Create a TextView for the header cell
        TextView textView = new TextView(getActivity());
        textView.setText(text);

        // Set layout parameters for the header cell
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        textView.setLayoutParams(params);

        // Center the text content within the header cell
        textView.setGravity(Gravity.CENTER);

        // Apply styling to the header cell using text appearance and background color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(R.style.TableHeaderCell);
            textView.setBackgroundResource(R.color.colorError);
            textView.setPadding(50, 10, 50, 10);
            params.setMargins(8, 8, 8, 8); // Adjust margin values as needed

            // Set the font size for the header cell text
            textView.setTextSize(15);
        }

        // Add the header cell to the provided row
        row.addView(textView);
    }

    /**
     * Adds a data cell to the provided row in the table.
     *
     * @param row  The TableRow to which the data cell should be added.
     * @param text The text content of the data cell.
     */
    private void addDataCell(TableRow row, String text) {
        // Create a TextView for the data cell
        TextView textView = new TextView(getActivity());
        textView.setText(text);

        // Set layout parameters for the data cell
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        textView.setLayoutParams(params);

        // Center the text content within the data cell
        textView.setGravity(Gravity.CENTER);

        // Apply styling to the data cell using text appearance, background color, and padding
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(R.style.TableHeaderCell);
            textView.setBackgroundResource(R.color.onSurface);
            textView.setPadding(100, 10, 100, 10);
            textView.setTextSize(15);
            params.setMargins(8, 8, 8, 8); // Adjust margin values as needed
        }

        // Add the data cell to the provided row
        row.addView(textView);
    }


    /**
     * Sends the order to be inserted into the database.
     *
     * @param view        The parent view containing the UI elements.
     * @param productName The name of the product in the order.
     * @param price       The price of the product in the order.
     * @param quantity    The quantity of the product in the order.
     * @param tax         The tax amount applied to the order.
     * @param discount    The discount amount applied to the order.
     * @param subTotal    The subtotal of the order before tax and discount.
     * @param total       The total amount of the order including tax and discount.
     */
    public void sendOrder(View view, String productName, double price, int quantity,
                          double tax, double discount, double subTotal, double total) {
        // Find the submit button in the view
        Button submit = view.findViewById(R.id.submit);

        // Set a click listener for the submit button
        submit.setOnClickListener(v -> {
            // Create an Order object with the provided data
            Order order = new Order();
            order.setProductName(productName);
            order.setPrice(price);
            order.setQuantity(quantity);
            order.setTax(tax);
            order.setDiscount(discount);
            order.setSubTotal(subTotal);
            order.setTotal(total);

            // Insert the order into the database
            orderViewModel.insertOrder(order).observe(getViewLifecycleOwner(), success -> {
                if (success) {
                    // Update the temporary order count and show a success message
                    getTempOrderCount();
                    Toast.makeText(getActivity(), "Order sent successfully", Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
