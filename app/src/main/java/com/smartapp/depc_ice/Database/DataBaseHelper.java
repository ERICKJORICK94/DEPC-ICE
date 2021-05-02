package com.smartapp.depc_ice.Database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.smartapp.depc_ice.Entities.Usuario;
import com.smartapp.depc_ice.Utils.Const;

import java.sql.SQLException;
import java.util.List;

public class DataBaseHelper {


    //HELPER USUARIO
    public static void saveUsuario(Usuario emp, Dao<Usuario, Integer> userDao) {
        try {
            userDao.create(emp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUUsuario(Dao<Usuario, Integer> userDao) throws SQLException {
        DeleteBuilder<Usuario, Integer> deleteBuilder = userDao.deleteBuilder();
        deleteBuilder.delete();
    }

    public static List<Usuario> getUsuario(Dao<Usuario, Integer> userDao) throws SQLException {

        List<Usuario> usuarios = null;
        String query = "SELECT * FROM " + Const.TABLE_USUARIO;
        GenericRawResults<Usuario> rawResults = userDao.queryRaw(query, userDao.getRawRowMapper());
        usuarios = rawResults.getResults();

        return usuarios;
    }

}
