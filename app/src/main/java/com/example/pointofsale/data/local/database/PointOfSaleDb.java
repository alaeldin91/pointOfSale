package com.example.pointofsale.data.local.database;



import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.pointofsale.data.local.dao.OrderDao;
import com.example.pointofsale.data.local.dao.ProductDao;
import com.example.pointofsale.data.local.dao.TempOrderDao;
import com.example.pointofsale.data.remote.model.Order;
import com.example.pointofsale.data.remote.model.Product;
import com.example.pointofsale.data.remote.model.TempOrder;

// Import statements

/**
 * The database class for the Point of Sale application.
 *
 * This class is annotated with @Database to define the database schema,
 * including entity classes and version.
 */
@Database(entities = {Product.class, TempOrder.class, Order.class}, version = 24, exportSchema = false)
public abstract class PointOfSaleDb extends RoomDatabase {

    /**
     * Abstract method to access the DAO for Product operations.
     *
     * @return An instance of the ProductDao interface.
     */
    public abstract ProductDao productDao();

    /**
     * Abstract method to access the DAO for TempOrder operations.
     *
     * @return An instance of the TempOrderDao interface.
     */
    public abstract TempOrderDao tempOrderDao();

    /**
     * Abstract method to access the DAO for Order operations.
     *
     * @return An instance of the OrderDao interface.
     */
    public abstract OrderDao orderDao();

    // Additional methods, if any

}
