package com.example.personal_trainer.models.fitness_model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.personal_trainer.utils.ConexionBD;
import com.example.personal_trainer.utils.Quadruple;
import com.example.personal_trainer.utils.Quintuple;

import java.util.ArrayList;
import java.util.List;

public class RutinaModel {
    private int idRutina;
    private String nombre;
    private List<DetalleRutinaModel> detalleRutina;
    private List<Quintuple<String, String, Integer, Integer, Integer>> listaEjerciciosRutina;
    private SQLiteDatabase database;

    public RutinaModel() {
        this(-1, "");
        this.detalleRutina = new ArrayList<>();
        this.listaEjerciciosRutina = new ArrayList<>();
    }

    public RutinaModel(int idRutina, String nombre) {
        this.idRutina = idRutina;
        this.nombre = nombre;
        this.detalleRutina = new ArrayList<>();
        this.listaEjerciciosRutina = new ArrayList<>();
    }

    public boolean insertar(String nombre, List<Quadruple<Integer, Integer, Integer, Integer>> ejercicios) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(ConexionBD.COLUMN_NOMBRE, nombre);
            long idRutina = database.insertOrThrow(ConexionBD.TABLE_RUTINA, null, values);

            if (idRutina == -1) {
                throw new RuntimeException("Error al insertar la rutina principal");
            }

            this.idRutina = (int) idRutina;
            this.nombre = nombre;
            this.detalleRutina.clear();

            if (ejercicios != null && !ejercicios.isEmpty()) {
                DetalleRutinaModel detalleModel = new DetalleRutinaModel();
                detalleModel.setDatabase(database);

                for (Quadruple<Integer, Integer, Integer, Integer> ejercicio : ejercicios) {
                    boolean detalleInsertado = detalleModel.insertar(
                            (int) idRutina,
                            ejercicio.getFirst(),
                            ejercicio.getSecond(),
                            ejercicio.getThird(),
                            ejercicio.getFourth()
                    );

                    if (!detalleInsertado) {
                        throw new RuntimeException("Error al insertar detalle para ejercicio: " + ejercicio.getFirst());
                    }

                    this.detalleRutina.add(new DetalleRutinaModel(
                            (int) idRutina,
                            ejercicio.getFirst(),
                            ejercicio.getSecond(),
                            ejercicio.getThird(),
                            ejercicio.getFourth()
                    ));
                }
            }
        });
    }

    public boolean modificar(int idRutina, String nombre, List<Quadruple<Integer, Integer, Integer, Integer>> ejercicios) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(ConexionBD.COLUMN_NOMBRE, nombre);
            database.update(ConexionBD.TABLE_RUTINA, values,
                    ConexionBD.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(idRutina)});

            DetalleRutinaModel detalleModel = new DetalleRutinaModel();
            detalleModel.setDatabase(database);

            detalleModel.borrarTodosDeRutina(idRutina);

            if (ejercicios != null && !ejercicios.isEmpty()) {
                for (Quadruple<Integer, Integer, Integer, Integer> ejercicio : ejercicios) {
                    boolean exito = detalleModel.insertar(
                            idRutina,
                            ejercicio.getFirst(),
                            ejercicio.getSecond(),
                            ejercicio.getThird(),
                            ejercicio.getFourth()
                    );
                    if (!exito) {
                        throw new RuntimeException("Error al insertar ejercicio: " + ejercicio.getFirst());
                    }
                }
            }
        });
    }

    public boolean borrar(int idRutina) {
        return executeTransaction(() -> {

            DetalleRutinaModel detalleModel = new DetalleRutinaModel();
            detalleModel.setDatabase(database);
            detalleModel.borrarTodosDeRutina(idRutina);

            database.delete(ConexionBD.TABLE_RUTINA,
                    ConexionBD.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(idRutina)});
        });
    }

    public RutinaModel buscar(int idRutina) {
        RutinaModel rutina = null;
        Cursor cursor = null;
        try {
            cursor = database.query(ConexionBD.TABLE_RUTINA,
                    new String[]{ConexionBD.COLUMN_ID, ConexionBD.COLUMN_NOMBRE},
                    ConexionBD.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(idRutina)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_ID));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_NOMBRE));
                rutina = new RutinaModel(id, nombre);

                DetalleRutinaModel detalleModel = new DetalleRutinaModel();
                detalleModel.setDatabase(database);
                rutina.setDetalleRutina(detalleModel.obtenerEjerciciosDeRutina(idRutina));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return rutina;
    }

    public RutinaModel buscarPorNombre(String nombreRutina) {
        RutinaModel rutina = null;
        Cursor cursor = null;
        try {
            String[] columns = {ConexionBD.COLUMN_ID, ConexionBD.COLUMN_NOMBRE};
            String selection = ConexionBD.COLUMN_NOMBRE + " = ?";
            String[] selectionArgs = {nombreRutina};

            cursor = database.query(ConexionBD.TABLE_RUTINA, columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_ID));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_NOMBRE));
                rutina = new RutinaModel(id, nombre);

                DetalleRutinaModel detalleModel = new DetalleRutinaModel();
                detalleModel.setDatabase(database);
                rutina.setDetalleRutina(detalleModel.obtenerEjerciciosDeRutina(id));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return rutina;
    }

    public List<RutinaModel> consultar() {
        List<RutinaModel> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.query(ConexionBD.TABLE_RUTINA, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int idR = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_ID));
                    String nom = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_NOMBRE));

                    RutinaModel rutina = new RutinaModel(idR, nom);

                    DetalleRutinaModel detalleModel = new DetalleRutinaModel();
                    detalleModel.setDatabase(database);
                    rutina.setDetalleRutina(detalleModel.obtenerEjerciciosDeRutina(idR));

                    lista.add(rutina);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lista;
    }

    public void agregarEjercicio(int idEjercicio, int series, int repeticiones, int descanso) {
        if (detalleRutina == null) {
            detalleRutina = new ArrayList<>();
        }
        detalleRutina.add(new DetalleRutinaModel(idRutina, idEjercicio, series, repeticiones, descanso));
    }

    public void setEjerciciosDeRutina(String nombreEjercicio, String urlImagen,
                                      int cantidadSerie, int cantidadRepeticion,
                                      int duracionReposo) {
        Quintuple<String, String, Integer, Integer, Integer> ejercicio = Quintuple.of(
                nombreEjercicio,
                urlImagen,
                cantidadSerie,
                cantidadRepeticion,
                duracionReposo
        );
        this.listaEjerciciosRutina.add(ejercicio);
    }

    public List<Quintuple<String, String, Integer, Integer, Integer>> getEjerciciosDeRutina() {
        return this.listaEjerciciosRutina;
    }

    public Quintuple<String, String, Integer, Integer, Integer> getEjercicioDeRutina(int posicion) {
        if (posicion >= 0 && posicion < listaEjerciciosRutina.size()) {
            return listaEjerciciosRutina.get(posicion);
        }
        return null;
    }

    public void limpiarEjercicios() {
        if (detalleRutina != null) {
            detalleRutina.clear();
        }
    }

    public List<Quadruple<Integer, Integer, Integer, Integer>> obtenerIdsEjerciciosRutina(int idRutina) {
        DetalleRutinaModel detalleModel = new DetalleRutinaModel();
        detalleModel.setDatabase(database);
        return detalleModel.obtenerEjerciciosConDescripciones(idRutina);
    }
    public int getIdRutina() {
        return idRutina;
    }

    public void setIdRutina(int idRutina) {
        this.idRutina = idRutina;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<DetalleRutinaModel> getDetalleRutina() {
        return detalleRutina;
    }

    public void setDetalleRutina(List<DetalleRutinaModel> detalleRutina) {
        this.detalleRutina = detalleRutina;
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