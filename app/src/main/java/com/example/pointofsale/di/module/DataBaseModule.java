package com.example.pointofsale.di.module;


import android.app.Application;
import androidx.room.Room;

import com.example.pointofsale.data.local.dao.OrderDao;
import com.example.pointofsale.data.local.dao.ProductDao;
import com.example.pointofsale.data.local.dao.TempOrderDao;
import com.example.pointofsale.data.local.database.PointOfSaleDb;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DataBaseModule {

    @Provides
    @Singleton
    public static PointOfSaleDb providePointOfSale(Application application){

        return Room.databaseBuilder(application,PointOfSaleDb.class,"productDb")
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();

    }
    @Provides
    @Singleton
    public static ProductDao provideProductDao(PointOfSaleDb pointOfSaleDb){

        return pointOfSaleDb.productDao();
    }

    @Provides
    @Singleton
    public static TempOrderDao provideTempOrder(PointOfSaleDb pointOfSaleDb){
        return pointOfSaleDb.tempOrderDao();
    }

    @Provides
    @Singleton
    public static OrderDao provideOrderDao(PointOfSaleDb pointOfSaleDb){

        return pointOfSaleDb.orderDao();
    }

}
