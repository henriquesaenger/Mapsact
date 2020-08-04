package com.example.mapsact;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Banco extends SQLiteOpenHelper {
    public static final String DATABASE_NAME= "Restaurantes.db";
    public static final String TABLE_NAME="Restaurantes";
    public static final String COL1 ="ID";
    public static final String COL2 ="NOME";
    public static final String COL3 ="TIPO";
    public static final String COL4 ="GLUTENFO";
    public static final String COL5 ="LACTOSEFO";
    public static final String COL6 ="LATITUDE";
    public static final String COL7 ="LONGITUDE";



    public Banco(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NOME TEXT, TIPO TEXT, GLUTENFO BOOLEAN, LACTOSEFO BOOLEAN, LATITUDE INTEGER, LONGITUDE INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }
}
