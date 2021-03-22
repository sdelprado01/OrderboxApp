package com.delpradosergio.orderbox.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.delpradosergio.orderbox.Entidades.Articulo;
import com.delpradosergio.orderbox.Entidades.Pedido;
import com.delpradosergio.orderbox.Interfaces.PedidoDAO;


//A que bases hace referencia
@Database(entities = {Pedido.class, Articulo.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PedidoDAO pedidoDAO(); //que permisos tiene (LISTAR, ELIMINAR ACTUALIZAR...)
    private static volatile AppDatabase INSTANCE; //Variable para poder llamar al administrador de base de datos.

    public static AppDatabase getInstance(final Context context) {
        if(INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = buildDatabase(context);
                }
            }
        }
        return INSTANCE;
    }

    private static AppDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "PedidosDatabase")
                .build();
    }
}



