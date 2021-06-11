package com.smartapp.depc_ice.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.smartapp.depc_ice.Entities.Bancos;
import com.smartapp.depc_ice.Entities.Bodega;
import com.smartapp.depc_ice.Entities.ClienteGabinet;
import com.smartapp.depc_ice.Entities.Clientes;
import com.smartapp.depc_ice.Entities.ClientesVisitas;
import com.smartapp.depc_ice.Entities.DetallePedido;
import com.smartapp.depc_ice.Entities.Direcciones;
import com.smartapp.depc_ice.Entities.EstadoGabinet;
import com.smartapp.depc_ice.Entities.FormaPago;
import com.smartapp.depc_ice.Entities.Gabinet;
import com.smartapp.depc_ice.Entities.Pedidos;
import com.smartapp.depc_ice.Entities.Productos;
import com.smartapp.depc_ice.Entities.PuntosVenta;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Entities.Zonas;
import com.smartapp.depc_ice.Utils.Const;
import java.sql.SQLException;

public class Database extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = Const.DATABASE_NAME_;
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            this.db = db;

            Log.i(Database.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Usuario.class);
            TableUtils.createTable(connectionSource, Clientes.class);
            TableUtils.createTable(connectionSource, Bodega.class);
            TableUtils.createTable(connectionSource, Productos.class);
            TableUtils.createTable(connectionSource, Zonas.class);
            TableUtils.createTable(connectionSource, Direcciones.class);
            TableUtils.createTable(connectionSource, Pedidos.class);
            TableUtils.createTable(connectionSource, DetallePedido.class);
            TableUtils.createTable(connectionSource, ClientesVisitas.class);
            TableUtils.createTable(connectionSource, PuntosVenta.class);
            TableUtils.createTable(connectionSource, Gabinet.class);
            TableUtils.createTable(connectionSource, ClienteGabinet.class);
            TableUtils.createTable(connectionSource, EstadoGabinet.class);
            TableUtils.createTable(connectionSource, FormaPago.class);
            TableUtils.createTable(connectionSource, Bancos.class);

        } catch (SQLException e) {
            Log.e(Database.class.getName(), "ERROR AL CREAR LA BASE DE DATOS", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {

            this.db = db;

            TableUtils.dropTable(connectionSource, Usuario.class, true);
            TableUtils.dropTable(connectionSource, Clientes.class, true);
            TableUtils.dropTable(connectionSource, Bodega.class, true);
            TableUtils.dropTable(connectionSource, Productos.class, true);
            TableUtils.dropTable(connectionSource, Zonas.class, true);
            TableUtils.dropTable(connectionSource, Direcciones.class, true);
            TableUtils.dropTable(connectionSource, Pedidos.class, true);
            TableUtils.dropTable(connectionSource, DetallePedido.class, true);
            TableUtils.dropTable(connectionSource, ClientesVisitas.class, true);
            TableUtils.dropTable(connectionSource, PuntosVenta.class, true);
            TableUtils.dropTable(connectionSource, Gabinet.class, true);
            TableUtils.dropTable(connectionSource, ClienteGabinet.class, true);
            TableUtils.dropTable(connectionSource, EstadoGabinet.class, true);
            TableUtils.dropTable(connectionSource, FormaPago.class, true);
            TableUtils.dropTable(connectionSource, Bancos.class, true);
            onCreate(db, connectionSource);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        super.close();
    }

    public void BeginTransaction() {
        db.beginTransaction();
    }

    public void EndTransactionSuccess() {
        db.setTransactionSuccessful();
        db.endTransaction();
    }
    public void EndTransaction() {
        db.endTransaction();
    }
}