package com.example.personal_trainer.models.control_model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.personal_trainer.utils.ConexionBD;

import java.util.ArrayList;
import java.util.List;

public class CronogramaModel {
    private int idCronograma;
    private int idCliente;
    private int idRutina;
    private String fecha;
    private SQLiteDatabase database;

    public CronogramaModel() {
        this(-1, -1, -1, "");
    }

    public CronogramaModel(int idCronograma, int idCliente, int idRutina, String fecha) {
        this.idCronograma = idCronograma;
        this.idCliente = idCliente;
        this.idRutina = idRutina;
        this.fecha = fecha;
    }

    public boolean insertar(int idCliente, int idRutina, String fecha){
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(ConexionBD.FK_ID_CLIENTE, idCliente);
            values.put(ConexionBD.FK_ID_RUTINA, idRutina);
            values.put(ConexionBD.COLUMN_FECHA, fecha);
            database.insertOrThrow(ConexionBD.TABLE_CRONOGRAMA, null, values);
        });
    }

    public boolean modificar(int idCronograma, int idCliente, int idRutina, String fecha) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(ConexionBD.FK_ID_CLIENTE, idCliente);
            values.put(ConexionBD.FK_ID_RUTINA, idRutina);
            values.put(ConexionBD.COLUMN_FECHA, fecha);
            database.update(ConexionBD.TABLE_CRONOGRAMA, values,
                    ConexionBD.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(idCronograma)});
        });
    }

    public boolean borrar(int idCronograma) {
        return executeTransaction(() -> {
            database.delete(ConexionBD.TABLE_CRONOGRAMA,
                    ConexionBD.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(idCronograma)});
        });
    }

    public List<CronogramaModel> consultar() {
        List<CronogramaModel> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.query(ConexionBD.TABLE_CRONOGRAMA, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int idC = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_ID));
                    int idCli = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.FK_ID_CLIENTE));
                    int idRut = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.FK_ID_RUTINA));
                    String fec = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_FECHA));
                    lista.add(new CronogramaModel(idC, idCli, idRut, fec));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lista;
    }

    public CronogramaModel buscar(int idCronograma) {
        CronogramaModel cronograma = null;
        Cursor cursor = null;
        try {
            cursor = database.query(ConexionBD.TABLE_CRONOGRAMA,
                    new String[]{
                            ConexionBD.COLUMN_ID,
                            ConexionBD.FK_ID_CLIENTE,
                            ConexionBD.FK_ID_RUTINA,
                            ConexionBD.COLUMN_FECHA
                    },
                    ConexionBD.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(idCronograma)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_ID));
                int idCliente = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.FK_ID_CLIENTE));
                int idRutina = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.FK_ID_RUTINA));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_FECHA));
                cronograma = new CronogramaModel(id, idCliente, idRutina, fecha);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return cronograma;
    }

    // Métodos adicionales específicos para CronogramaModel
    public List<CronogramaModel> buscarPorCliente(int idCliente) {
        List<CronogramaModel> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.query(ConexionBD.TABLE_CRONOGRAMA,
                    null,
                    ConexionBD.FK_ID_CLIENTE + " = ?",
                    new String[]{String.valueOf(idCliente)},
                    null, null, ConexionBD.COLUMN_FECHA + " ASC");

            if (cursor.moveToFirst()) {
                do {
                    int idC = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_ID));
                    int idRut = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.FK_ID_RUTINA));
                    String fec = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_FECHA));
                    lista.add(new CronogramaModel(idC, idCliente, idRut, fec));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lista;
    }

    public List<CronogramaModel> buscarPorFecha(String fecha) {
        List<CronogramaModel> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.query(ConexionBD.TABLE_CRONOGRAMA,
                    null,
                    ConexionBD.COLUMN_FECHA + " = ?",
                    new String[]{fecha},
                    null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    int idC = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_ID));
                    int idCli = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.FK_ID_CLIENTE));
                    int idRut = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.FK_ID_RUTINA));
                    lista.add(new CronogramaModel(idC, idCli, idRut, fecha));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lista;
    }

    public int getIdCronograma() {
        return idCronograma;
    }

    public void setIdCronograma(int idCronograma) {
        this.idCronograma = idCronograma;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdRutina() {
        return idRutina;
    }

    public void setIdRutina(int idRutina) {
        this.idRutina = idRutina;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    private boolean executeTransaction(Runnable operation) {
        database.beginTransaction();
        try {
            operation.run();
            database.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            database.endTransaction();
        }
    }

    public void initBD(Context context){
        SQLiteDatabase database = null;
        try {
            ConexionBD admin = new ConexionBD(context);
            database = admin.getWritableDatabase();
            this.setDatabase(database);
        } catch (Exception e) {
            System.err.println("Error al iniciar la base de datos: " + e.getMessage());
            if (database != null && database.isOpen()) {
                database.close();
            }
        }
    }
}
