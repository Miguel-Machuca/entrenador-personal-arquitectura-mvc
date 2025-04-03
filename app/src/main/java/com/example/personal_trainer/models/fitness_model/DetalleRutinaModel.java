package com.example.personal_trainer.models.fitness_model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.personal_trainer.utils.ConexionBD;
import com.example.personal_trainer.utils.Quadruple;

import java.util.ArrayList;
import java.util.List;

public class DetalleRutinaModel {
    private int idRutina;
    private int idEjercicio;
    private int cantidadSerie;
    private int cantidadRepeticion;
    private int duracionReposo;
    private SQLiteDatabase database;

    public DetalleRutinaModel() {
        this(-1, -1, -1, -1, -1);
    }

    public DetalleRutinaModel(int idRutina, int idEjercicio, int cantidadSerie, int cantidadRepeticion, int duracionReposo) {
        this.idRutina = idRutina;
        this.idEjercicio = idEjercicio;
        this.cantidadSerie = cantidadSerie;
        this.cantidadRepeticion = cantidadRepeticion;
        this.duracionReposo = duracionReposo;
    }

    public boolean insertar(int idRutina, int idEjercicio, int cantidadSerie, int cantidadRepeticion, int duracionReposo) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(ConexionBD.FK_ID_RUTINA, idRutina);
            values.put(ConexionBD.FK_ID_EJERCICIO, idEjercicio);
            values.put(ConexionBD.COLUMN_CANTIDADSERIE, cantidadSerie);
            values.put(ConexionBD.COLUMN_CANTIDADREPETICION, cantidadRepeticion);
            values.put(ConexionBD.COLUMN_DURACIONREPOSO, duracionReposo);
            database.insertOrThrow(ConexionBD.TABLE_DETALLERUTINA, null, values);
        });
    }

    public boolean modificar(int idRutina, int idEjercicio, int cantidadSerie,int cantidadRepeticion, int duracionReposo) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(ConexionBD.COLUMN_CANTIDADSERIE, cantidadSerie);
            values.put(ConexionBD.COLUMN_CANTIDADREPETICION, cantidadRepeticion);
            values.put(ConexionBD.COLUMN_DURACIONREPOSO, duracionReposo);
            database.update(ConexionBD.TABLE_DETALLERUTINA, values,
                    ConexionBD.FK_ID_RUTINA + " = ? AND " + ConexionBD.FK_ID_EJERCICIO + " = ?",
                    new String[]{String.valueOf(idRutina), String.valueOf(idEjercicio)});
        });
    }

    public boolean borrar(int idRutina, int idEjercicio) {
        return executeTransaction(() -> {
            database.delete(ConexionBD.TABLE_DETALLERUTINA,
                    ConexionBD.FK_ID_RUTINA + " = ? AND " + ConexionBD.FK_ID_EJERCICIO + " = ?",
                    new String[]{String.valueOf(idRutina), String.valueOf(idEjercicio)});
        });
    }

    public boolean borrarTodosDeRutina(int idRutina) {
        return executeTransaction(() -> {
            database.delete(ConexionBD.TABLE_DETALLERUTINA,
                    ConexionBD.FK_ID_RUTINA + " = ?",
                    new String[]{String.valueOf(idRutina)});
        });
    }

    public DetalleRutinaModel buscar(int idRutina, int idEjercicio) {
        DetalleRutinaModel detalleRutina = null;
        Cursor cursor = null;

        try {
            String[] columns = {
                    ConexionBD.FK_ID_RUTINA,
                    ConexionBD.FK_ID_EJERCICIO,
                    ConexionBD.COLUMN_CANTIDADSERIE,
                    ConexionBD.COLUMN_CANTIDADREPETICION,
                    ConexionBD.COLUMN_DURACIONREPOSO
            };

            String selection = ConexionBD.FK_ID_RUTINA + " = ? AND " + ConexionBD.FK_ID_EJERCICIO + " = ?";
            String[] selectionArgs = {String.valueOf(idRutina), String.valueOf(idEjercicio)};

            cursor = database.query(
                    ConexionBD.TABLE_DETALLERUTINA,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                int cantidadSerie = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_CANTIDADSERIE));
                int cantidadRepeticion = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_CANTIDADREPETICION));
                int duracionReposo = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_DURACIONREPOSO));

                detalleRutina = new DetalleRutinaModel(idRutina, idEjercicio, cantidadSerie, cantidadRepeticion, duracionReposo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return detalleRutina;
    }

    public List<DetalleRutinaModel> obtenerEjerciciosDeRutina(int idRutina) {
        List<DetalleRutinaModel> ejercicios = new ArrayList<>();
        Cursor cursor = null;

        try {
            String[] columns = {
                    ConexionBD.FK_ID_EJERCICIO,
                    ConexionBD.COLUMN_CANTIDADSERIE,
                    ConexionBD.COLUMN_CANTIDADREPETICION,
                    ConexionBD.COLUMN_DURACIONREPOSO
            };

            String selection = ConexionBD.FK_ID_RUTINA + " = ?";
            String[] selectionArgs = {String.valueOf(idRutina)};

            cursor = database.query(
                    ConexionBD.TABLE_DETALLERUTINA,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor.moveToFirst()) {
                do {
                    int idEjercicio = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.FK_ID_EJERCICIO));
                    int cantidadSerie = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_CANTIDADSERIE));
                    int cantidadRepeticion = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_CANTIDADREPETICION));
                    int duracionReposo = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_DURACIONREPOSO));

                    ejercicios.add(new DetalleRutinaModel(
                            idRutina,
                            idEjercicio,
                            cantidadSerie,
                            cantidadRepeticion,
                            duracionReposo
                    ));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return ejercicios;
    }

    public List<Quadruple<Integer, Integer, Integer, Integer>> obtenerEjerciciosConDescripciones(int idRutina) {
        List<Quadruple<Integer, Integer, Integer, Integer>> ejercicios = new ArrayList<>();
        Cursor cursor = null;

        try {
            String query = "SELECT " + ConexionBD.FK_ID_EJERCICIO + ", " +
                    ConexionBD.COLUMN_CANTIDADSERIE + ", " + ConexionBD.COLUMN_CANTIDADREPETICION +
                    ", " + ConexionBD.COLUMN_DURACIONREPOSO +
                    " FROM " + ConexionBD.TABLE_DETALLERUTINA +
                    " WHERE " + ConexionBD.FK_ID_RUTINA + " = ?";
            cursor = database.rawQuery(query, new String[]{String.valueOf(idRutina)});

            if (cursor.moveToFirst()) {
                do {
                    int idEjercicio = cursor.getInt(0);
                    int cantidadSerie = cursor.getInt(1);
                    int cantidadRepeticion = cursor.getInt(2);
                    int duracionReposo = cursor.getInt(3);
                    ejercicios.add(new Quadruple<>(idEjercicio, cantidadSerie, cantidadRepeticion, duracionReposo));
                } while (cursor.moveToNext());  // Corregido: moveToNext() en lugar de moveToFirst()
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return ejercicios;
    }
    public int getIdEjercicio() {
        return idEjercicio;
    }

    public void setIdEjercicio(int idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public int getIdRutina() {
        return idRutina;
    }

    public void setIdRutina(int idRutina) {
        this.idRutina = idRutina;
    }

    public int getCantidadSerie() {
        return cantidadSerie;
    }

    public void setCantidadSerie(int cantidadSerie) {
        this.cantidadSerie = cantidadSerie;
    }

    public int getCantidadRepeticion() {
        return cantidadRepeticion;
    }

    public void setCantidadRepeticion(int cantidadRepeticion) {
        this.cantidadRepeticion = cantidadRepeticion;
    }

    public int getDuracionReposo() {
        return duracionReposo;
    }

    public void setDuracionReposo(int duracionReposo) {
        this.duracionReposo = duracionReposo;
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
