package com.example.personal_trainer.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ConexionBD extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "personal_trainer.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_EJERCICIO = "ejercicio";
    public static final String TABLE_RUTINA = "rutina";
    public static final String TABLE_DETALLERUTINA = "detalle_rutina";
    public static final String TABLE_CRONOGRAMA = "cronograma";
    public static final String TABLE_CLIENTE = "cliente";
    public static final String TABLE_OBJETIVO = "objetivo";
    public static final String TABLE_DETALLECLIENTE = "detalle_cliente";
    public static final String COLUMN_ID = "id";
    public static final String FK_ID_CLIENTE = "id_cliente";
    public static final String FK_ID_OBJETIVO = "id_objetivo";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_URLIMAGEN = "url_imagen";
    public static ConexionBD instance;

    public static synchronized ConexionBD getInstance(Context context) {
        if (instance == null) {
            instance = new ConexionBD(context.getApplicationContext());
        }
        return instance;
    }

    public ConexionBD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(getCreateTableEjercicio());
        sqLiteDatabase.execSQL(getCreateTableObjetivo());
        sqLiteDatabase.execSQL(getCreateTableCliente());
        sqLiteDatabase.execSQL(getCreateTableDetalleCliente());
        sqLiteDatabase.execSQL(getCreateTableRutina());
        sqLiteDatabase.execSQL(getCreateTableDetalleRutina());
        sqLiteDatabase.execSQL(getCreateTableCronograma());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        dropTables(sqLiteDatabase);
        onCreate(sqLiteDatabase);
    }

    private void dropTables(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EJERCICIO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RUTINA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DETALLERUTINA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CRONOGRAMA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_OBJETIVO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DETALLECLIENTE);
    }

    private String getCreateTableEjercicio(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_EJERCICIO + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT NOT NULL, " +
                COLUMN_URLIMAGEN + " TEXT);";

    }

    private String getCreateTableObjetivo(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_OBJETIVO + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT NOT NULL);";
    }

    private String getCreateTableCliente() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_CLIENTE + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT NOT NULL, " +
                COLUMN_APELLIDO + " TEXT NOT NULL, " +
                COLUMN_CELULAR + " TEXT NOT NULL);";
    }

    private String getCreateTableDetalleCliente() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_DETALLECLIENTE + "(" +
                FK_ID_CLIENTE + " INTEGER NOT NULL, " +
                FK_ID_OBJETIVO + " INTEGER NOT NULL, " +
                COLUMN_DESCRIPCION + " TEXT, " +
                "PRIMARY KEY (" + FK_ID_CLIENTE + ", " + FK_OBJETIVO + "), " +
                "FOREIGN KEY (" + FK_ID_CLIENTE + ") REFERENCES " + TABLE_CLIENTE + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY (" + FK_ID_OBJETIVO + ") REFERENCES " + TABLE_OBJETIVO + "(" + COLUMN_ID + "));";
    }

    private String getCreateTableRutina() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_RUTINA + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT NOT NULL);";
    }

    private String getCreateTableDetalleRutina() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_DETALLERUTINA + " (" +
                FK_ID_RUTINA + " INTEGER NOT NULL, " +
                FK_ID_EJERCICIO + " INTEGER NOT NULL, " +
                COLUMN_CANTIDADSERIE + " INTEGER NOT NULL, " +
                COLUMN_CANTIDADREPETICION + " INTEGER NOT NULL, " +
                COLUMN_DURACIONREPOSO + " INTEGER NOT NULL, " +
                "PRIMARY KEY (" + FK_ID_RUTINA + ", " + FK_ID_EJERCICIO + "), " +
                "FOREIGN KEY (" + FK_ID_RUTINA + ") REFERENCES " + TABLE_RUTINA + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY (" + FK_ID_EJERCICIO + ") REFERENCES " + TABLE_EJERCICIO + "(" + COLUMN_ID + "));";
    }

    private String getCreateTableCronograma() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_CRONOGRAMA + "(" +
                COLUMN_ID_CRONOGRAMA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FECHA + " DATE, " +
                FK_ID_CLIENTE + " INTEGER NOT NULL, " +
                FK_ID_RUTINA + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + FK_ID_CLIENTE + ") REFERENCES " + TABLE_CLIENTE + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY (" + FK_ID_RUTINA + ") REFERENCES " + TABLE_RUTINA + "(" + COLUMN_ID + "));";
    }

}