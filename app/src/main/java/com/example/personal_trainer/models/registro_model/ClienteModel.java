package com.example.personal_trainer.models.registro_model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.example.personal_trainer.utils.ConexionBD;

import java.util.ArrayList;
import java.util.List;

public class ClienteModel {
    private int idCliente;
    private String nombre;
    private String apellido;
    private String celular;
    private List<DetalleClienteModel> detalleCliente;
    private SQLiteDatabase database;

    public ClienteModel() {
        this(-1, "", "", "");
        this.detalleCliente = new ArrayList<>();
    }

    public ClienteModel(int idCliente, String nombre, String apellido, String celular) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.celular = celular;
        this.detalleCliente = new ArrayList<>();
    }

    public boolean insertar(String nombre, String apellido, String celular,
                            List<Pair<Integer, String>> objetivos) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(ConexionBD.COLUMN_NOMBRE, nombre);
            values.put(ConexionBD.COLUMN_APELLIDO, apellido);
            values.put(ConexionBD.COLUMN_CELULAR, celular);
            long idCliente = database.insertOrThrow(ConexionBD.TABLE_CLIENTE, null, values);

            if (idCliente != -1 && objetivos != null && !objetivos.isEmpty()) {
                DetalleClienteModel detalleModel = new DetalleClienteModel();
                detalleModel.setDatabase(database);

                for (Pair<Integer, String> objetivo : objetivos) {
                    int idObjetivo = objetivo.first;
                    String descripcion = objetivo.second;

                    boolean detalleInsertado = detalleModel.insertar((int)idCliente, idObjetivo, descripcion);
                    if (!detalleInsertado) {
                        throw new RuntimeException("Error al insertar detalle para objetivo: " + idObjetivo);
                    }
                }
            }
        });
    }

    public boolean modificar(int idCliente, String nombre, String apellido, String celular,
                             List<Pair<Integer, String>> nuevosObjetivos) {
        return executeTransaction(() -> {
            ContentValues values = new ContentValues();
            values.put(ConexionBD.COLUMN_NOMBRE, nombre);
            values.put(ConexionBD.COLUMN_APELLIDO, apellido);
            values.put(ConexionBD.COLUMN_CELULAR, celular);
            database.update(ConexionBD.TABLE_CLIENTE, values,
                    ConexionBD.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(idCliente)});

            DetalleClienteModel detalleModel = new DetalleClienteModel();
            detalleModel.setDatabase(database);

            detalleModel.borrarTodosDeCliente(idCliente);

            if (nuevosObjetivos != null && !nuevosObjetivos.isEmpty()) {
                for (Pair<Integer, String> objetivo : nuevosObjetivos) {
                    boolean exito = detalleModel.insertar(
                            idCliente,
                            objetivo.first,
                            objetivo.second
                    );
                    if (!exito) {
                        throw new RuntimeException("Error al insertar objetivo: " + objetivo.first);
                    }
                }
            }
        });
    }

    public boolean borrar(int idCliente) {
        return executeTransaction(() -> {
            DetalleClienteModel detalleModel = new DetalleClienteModel();
            detalleModel.setDatabase(database);
            detalleModel.borrarTodosDeCliente(idCliente);

            database.delete(ConexionBD.TABLE_CLIENTE,
                    ConexionBD.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(idCliente)});
        });
    }

    public List<ClienteModel> consultar() {
        List<ClienteModel> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.query(ConexionBD.TABLE_CLIENTE, null, null, null, null, null, null);
            if (cursor.moveToFirst()){
                do {
                    int idC = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_ID));
                    String nom = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_NOMBRE));
                    String ape = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_APELLIDO));
                    String cel = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_CELULAR));
                    lista.add(new ClienteModel(idC, nom, ape, cel));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return lista;
    }

    public List<Pair<Integer, String>> obtenerIdsObjetivosCliente(int idCliente) {
        DetalleClienteModel detalleModel = new DetalleClienteModel();
        detalleModel.setDatabase(database);
        return detalleModel.obtenerObjetivosConDescripciones(idCliente);
    }

    public ClienteModel buscar(int idCliente) {
        ClienteModel cliente = null;
        Cursor cursor = null;
        try {
            cursor = database.query(ConexionBD.TABLE_CLIENTE,
                    new String[]{
                            ConexionBD.COLUMN_ID,
                            ConexionBD.COLUMN_NOMBRE,
                            ConexionBD.COLUMN_APELLIDO,
                            ConexionBD.COLUMN_CELULAR
                    },
                    ConexionBD.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(idCliente)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_ID));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_NOMBRE));
                String apellido = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_APELLIDO));
                String celular = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_CELULAR));
                cliente = new ClienteModel(id, nombre, apellido, celular);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return cliente;
    }

    public void agregarObjetivo(int idObjetivo, String descripcion) {
        if (detalleCliente == null) {
            detalleCliente = new ArrayList<>();
        }
        detalleCliente.add(new DetalleClienteModel(idCliente, idObjetivo, descripcion));
    }

    public ClienteModel buscarPorDatos(String nombre, String apellido, String celular) {
        ClienteModel cliente = null;
        Cursor cursor = null;
        try {
            String[] columns = {
                    ConexionBD.COLUMN_ID,
                    ConexionBD.COLUMN_NOMBRE,
                    ConexionBD.COLUMN_APELLIDO,
                    ConexionBD.COLUMN_CELULAR
            };
            String selection = ConexionBD.COLUMN_NOMBRE + " = ? AND " +
                    ConexionBD.COLUMN_APELLIDO + " = ? AND " +
                    ConexionBD.COLUMN_CELULAR + " = ?";
            String[] selectionArgs = {nombre, apellido, celular};

            cursor = database.query(ConexionBD.TABLE_CLIENTE, columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_ID));
                String nom = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_NOMBRE));
                String ape = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_APELLIDO));
                String cel = cursor.getString(cursor.getColumnIndexOrThrow(ConexionBD.COLUMN_CELULAR));

                cliente = new ClienteModel(id, nom, ape, cel);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return cliente;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public List<DetalleClienteModel> getDetalleCliente() {
        return detalleCliente;
    }

    public void setDetalleCliente(List<DetalleClienteModel> detalleCliente) {
        this.detalleCliente = detalleCliente;
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
