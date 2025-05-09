package com.example.personal_trainer.models.registro_model;

import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.content.Context;

import com.example.personal_trainer.utils.ConexionBD;

import java.util.ArrayList;
import java.util.List;

public class ObjetivoModel {

    private int idObjetivo;
    private String nombre;
    private SQLiteDatabase database;

    public ObjetivoModel() {
        this(-1, "");
    }

    public ObjetivoModel(String nombre) {
        this.nombre = nombre;
    }

    public ObjetivoModel(int idObjetivo, String nombre) {
        this.idObjetivo = idObjetivo;
        this.nombre = nombre;
    }

    public boolean insertar(String nombre) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(ConexionBD.COLUMN_NOMBRE, nombre);
            database.insertOrThrow(ConexionBD.TABLE_OBJETIVO, null, values);
        });
    }

    public boolean modificar(int idObjetivo, String nombre) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(ConexionBD.COLUMN_NOMBRE, nombre);
            database.update(ConexionBD.TABLE_OBJETIVO, values,
                    ConexionBD.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(idObjetivo)});
        });
    }

    public boolean borrar(int idObjetivo) {
        return executeTransaction(() -> {
            database.delete(ConexionBD.TABLE_OBJETIVO,
                    ConexionBD.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(idObjetivo)});
        });
    }

    public List<ObjetivoModel> consultar() {
        List<ObjetivoModel> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.query(ConexionBD.TABLE_OBJETIVO, null, null, null, null, null, null);
            if (cursor.moveToFirst()){
                do {
                    int idO = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_ID));
                    String nom = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_NOMBRE));
                    lista.add(new ObjetivoModel(idO, nom));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return lista;
    }

    public ObjetivoModel buscar(int idObjetivo) {
        ObjetivoModel objetivo = null;
        Cursor cursor = null;
        try {
            cursor = database.query(ConexionBD.TABLE_OBJETIVO,
                    new String[]{ConexionBD.COLUMN_ID, ConexionBD.COLUMN_NOMBRE},
                    ConexionBD.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(idObjetivo)},
                    null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_ID));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_NOMBRE));
                objetivo = new ObjetivoModel(id, nombre);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return objetivo;
    }

    public ObjetivoModel buscarPorNombre(String nombre) {
        ObjetivoModel objetivo = null;
        Cursor cursor = null;

        try {
            String[] columns = {ConexionBD.COLUMN_ID, ConexionBD.COLUMN_NOMBRE};
            String selection = ConexionBD.COLUMN_NOMBRE + " = ?";
            String[] selectionArgs = {nombre};

            cursor = database.query(ConexionBD.TABLE_OBJETIVO, columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_ID));
                String nom = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_NOMBRE));

                objetivo = new ObjetivoModel(id, nom);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return objetivo;
    }

    public int getIdObjetivo() {
        return idObjetivo;
    }

    public void setIdObjetivo(int idObjetivo) {
        this.idObjetivo = idObjetivo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
