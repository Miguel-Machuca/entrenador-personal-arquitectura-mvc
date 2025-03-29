package com.example.personal_trainer.models.registro_model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.example.personal_trainer.utils.ConexionBD;

import java.util.ArrayList;
import java.util.List;

public class DetalleClienteModel {
    private int idCliente;
    private int idObjetivo;
    private String descripcion;
    private SQLiteDatabase database;

    public DetalleClienteModel() {
        this(-1, -1, "");
    }

    public DetalleClienteModel(int idCliente, int idObjetivo, String descripcion) {
        this.idCliente = idCliente;
        this.idObjetivo = idObjetivo;
        this.descripcion = descripcion;
    }

    public boolean insertar(int idCliente, int idObjetivo, String descripcion) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(ConexionBD.FK_ID_CLIENTE, idCliente);
            values.put(ConexionBD.FK_ID_OBJETIVO, idObjetivo);
            values.put(ConexionBD.COLUMN_DESCRIPCION, descripcion);
            database.insertOrThrow(ConexionBD.TABLE_DETALLECLIENTE, null, values);
        });
    }

    public boolean modificar(int idCliente, int idObjetivo, String descripcion) {
        return executeTransaction(() -> {
           ContentValues values = new ContentValues();
           values.put(ConexionBD.COLUMN_DESCRIPCION, descripcion);
           database.update(ConexionBD.TABLE_DETALLECLIENTE, values,
                   ConexionBD.FK_ID_CLIENTE + " = ? AND " + ConexionBD.FK_ID_OBJETIVO + " = ?",
                   new String[]{String.valueOf(idCliente), String.valueOf(idObjetivo)});
        });
    }

    public boolean borrar(int idCliente, int idObjetivo) {
        return executeTransaction(() -> {
            database.delete(ConexionBD.TABLE_DETALLECLIENTE,
                    ConexionBD.COLUMN_ID + " = ? AND " + ConexionBD.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(idCliente), String.valueOf(idObjetivo)});
        });
    }

    public DetalleClienteModel buscar(int idCliente, int idObjetivo) {
        DetalleClienteModel detalleCliente = null;
        Cursor cursor = null;

        try {
            String[] columns = {ConexionBD.COLUMN_DESCRIPCION};

            String selection = ConexionBD.FK_ID_CLIENTE + " = ? AND " + ConexionBD.FK_ID_OBJETIVO + " = ?";
            String[] selectionArgs = {String.valueOf(idCliente), String.valueOf(idObjetivo)};

            cursor = database.query(ConexionBD.TABLE_DETALLECLIENTE, columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_DESCRIPCION));

                detalleCliente = new DetalleClienteModel(idCliente, idObjetivo, descripcion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return detalleCliente;
    }

    public List<Pair<Integer, String>> obtenerObjetivosConDescripciones(int idCliente) {
        List<Pair<Integer, String>> objetivos = new ArrayList<>();
        Cursor cursor = null;

        try {
            String query = "SELECT " + ConexionBD.FK_ID_OBJETIVO + ", " +
                    ConexionBD.COLUMN_DESCRIPCION +
                    " FROM " + ConexionBD.TABLE_DETALLECLIENTE +
                    " WHERE " + ConexionBD.FK_ID_CLIENTE + " = ?";

            cursor = database.rawQuery(query, new String[]{String.valueOf(idCliente)});

            if (cursor.moveToFirst()) {
                do {
                    int idObjetivo = cursor.getInt(0);
                    String descripcion = cursor.getString(1);
                    objetivos.add(new Pair<>(idObjetivo, descripcion));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return objetivos;
    }

    public boolean borrarTodosDeCliente(int idCliente) {
        return executeTransaction(() -> {
            database.delete(ConexionBD.TABLE_DETALLECLIENTE,
                    ConexionBD.FK_ID_CLIENTE + " = ?",
                    new String[]{String.valueOf(idCliente)});
        });
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

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setIdObjetivo(int idObjetivo) {
        this.idObjetivo = idObjetivo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public int getIdObjetivo() {
        return idObjetivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
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
