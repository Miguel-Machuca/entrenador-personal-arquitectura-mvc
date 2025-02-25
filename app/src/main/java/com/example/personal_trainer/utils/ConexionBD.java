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
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_URLIMAGEN = "url_imagen";

    public ConexionBD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(getCreateTableEjercicio());
        sqLiteDatabase.execSQL(getCreateTableObjetivo());
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
                COLUMN_NOMBRE + "TEXT NOT NULL, " +
                COLUMN_URLIMAGEN + "TEXT);";

    }

    private String getCreateTableObjetivo(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_OBJETIVO + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + "TEXT NOT NULL);";
    }
    private String getCreateTableRutina(){
       return "";
    }
}