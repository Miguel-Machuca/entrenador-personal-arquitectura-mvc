package com.example.personal_trainer.models.fitness_model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.personal_trainer.utils.ConexionBD;

import java.util.ArrayList;
import java.util.List;

public class EjercicioModel {
    private int idEjercicio;
    private String nombre;
    private String urlImagen;
    private SQLiteDatabase database;

    public EjercicioModel() {
        this(-1, "", "");
    }

    public EjercicioModel(String nombre, String urlImagen) {
        this.nombre = nombre;
        this.urlImagen = urlImagen;
    }

    public EjercicioModel(int idEjercicio, String nombre, String urlImagen) {
        this.idEjercicio = idEjercicio;
        this.nombre = nombre;
        this.urlImagen = urlImagen;
    }

    public boolean insertar(String nombre, String urlImagen) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(ConexionBD.COLUMN_NOMBRE, nombre);
            values.put(ConexionBD.COLUMN_URLIMAGEN, urlImagen);
            database.insertOrThrow(ConexionBD.TABLE_EJERCICIO, null, values);
        });
    }

    public boolean modificar(int idEjercicio, String nombre, String urlImagen) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(ConexionBD.COLUMN_NOMBRE, nombre);
            values.put(ConexionBD.COLUMN_URLIMAGEN, urlImagen);
            database.update(ConexionBD.TABLE_EJERCICIO, values,
                    ConexionBD.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(idEjercicio)});
        });
    }

    public boolean borrar(int idEjercicio) {
        return executeTransaction(() -> {
            database.delete(ConexionBD.TABLE_EJERCICIO,
                    ConexionBD.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(idEjercicio)});
        });
    }

    public List<EjercicioModel> consultar() {
        List<EjercicioModel> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.query(ConexionBD.TABLE_EJERCICIO, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int idE = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_ID));
                    String nom = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_NOMBRE));
                    String url = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_URLIMAGEN));
                    lista.add(new EjercicioModel(idE, nom, url));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lista;
    }

    public EjercicioModel buscar(int idEjercicio) {
        EjercicioModel ejercicio = null;
        Cursor cursor = null;
        try {
            cursor = database.query(ConexionBD.TABLE_EJERCICIO,
                    new String[]{ConexionBD.COLUMN_ID, ConexionBD.COLUMN_NOMBRE, ConexionBD.COLUMN_URLIMAGEN},
                    ConexionBD.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(idEjercicio)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_ID));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_NOMBRE));
                String urlImagen = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_URLIMAGEN));
                ejercicio = new EjercicioModel(id, nombre, urlImagen);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return ejercicio;
    }

    public int getIdEjercicio() {
        return idEjercicio;
    }

    public void setIdEjercicio(int idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
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
                database.close(); // Cerrar la base de datos si ocurrió un error
            }
        }
    }
}
