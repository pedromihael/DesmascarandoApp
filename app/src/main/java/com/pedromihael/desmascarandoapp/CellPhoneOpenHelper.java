package com.pedromihael.desmascarandoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class  CellPhoneOpenHelper extends SQLiteOpenHelper {

    private Context mContext = null;
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "cellphone";

    // Table Names
    private static final String TABLE_CELULAR = "celular";
    private static final String TABLE_MARCA = "marca";

    // Celular Table Columns
    private static final String COLUMN_CELULAR_ID = "idCelular";
    private static final String COLUMN_MARCA_FK = "marcaId";
    private static final String COLUMN_MODELO = "modelo";

    //Marca Table Columns
    private static final String COLUMN_MARCA_ID = "marcaId";
    private static final String COLUMN_MARCA = "marca";

    //Cria tabelas
    private static final String CREATE_TABLE_MARCA = "CREATE TABLE IF NOT EXISTS "
            + TABLE_MARCA + " ("
            + COLUMN_MARCA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_MARCA + " TEXT NOT NULL);";

    private static final String CREATE_TABLE_CELULAR = "CREATE TABLE IF NOT EXISTS "
            + TABLE_CELULAR + " ("
            + COLUMN_CELULAR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_MODELO + " TEXT NOT NULL, "
            + COLUMN_MARCA_FK + " TEXT, "
            + " FOREIGN KEY ("+COLUMN_MARCA_FK+") REFERENCES "+TABLE_MARCA+"("+COLUMN_MARCA_ID+"));";

    public CellPhoneOpenHelper() {
        super(null, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public CellPhoneOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MARCA);
        db.execSQL(CREATE_TABLE_CELULAR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CELULAR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARCA);
        this.onCreate(db);
    }

    public boolean addModel(Cellphone cellphone) {

        /* Garante que só 1 device por modelo+marca sera registrado. */
        if (!deviceExists(cellphone.getName())) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            int fk;

            if (!brandExists(cellphone.getBrand())) {
                try {
                    addBrand(cellphone, true);
                } catch (SQLiteException e) {
                    Toast.makeText(mContext, "Falha ao inserir no banco - " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }

            if (cellphone.getName().equals(null) || cellphone.getName().length() == 0) {
                // tentativa de cadastrar uma marca
                Toast.makeText(mContext, "Marca ja cadastrada.", Toast.LENGTH_SHORT).show();
            } else {
                fk = getForeignKeyFromDB(cellphone);

                values.put(COLUMN_MODELO, cellphone.getName());
                values.put(COLUMN_MARCA_FK, fk);

                db.insert(TABLE_CELULAR, null, values);
                db.close();

                Toast.makeText(mContext,"Cadastrado com sucesso!",Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        Toast.makeText(mContext, "Device ja cadastrado.", Toast.LENGTH_SHORT).show();
        return false;
    }

    /*
    * Verifica se marca já existe no banco
    * */
    private boolean brandExists(String brand) {
        String query = "SELECT COUNT(*) AS n FROM " + TABLE_MARCA
                + " WHERE marca = \"" + brand + "\";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            if (cursor.getInt(cursor.getColumnIndex("n")) == 0) {
                return false;
            }
        }

        cursor.close();

        return true;
    }

    /* Verifica se modelo já está cadastrado no banco */
    private boolean deviceExists(String device) {
        String query = "SELECT COUNT(*) AS n FROM " + TABLE_CELULAR
                + " WHERE celular.modelo = \"" + device + "\";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            if (cursor.getInt(cursor.getColumnIndex("n")) == 0) {
                return false;
            }
        }

        cursor.close();

        return true;
    }

    public boolean addBrand(Cellphone cellphone, boolean isVerified) {

        if (isVerified) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(COLUMN_MARCA, cellphone.getBrand());
            db.insert(TABLE_MARCA, null, values);

            return true;
        }

        /* Garante que só haverá 1 tupla por marca */
        if (!brandExists(cellphone.getBrand())) {
            return addBrand(cellphone, true);
        } else {
            return false;
        }
    }

    /*
    * Retorna a marcaId de uma marca para ser usada como FK na tabela celular.
    * */
    private int getForeignKeyFromDB(Cellphone cellphone) {
        String query = "SELECT " + COLUMN_MARCA_ID + " FROM " + TABLE_MARCA
                + " WHERE marca = \"" + cellphone.getBrand() + "\";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int fk = 99999;

        if (cursor.moveToFirst()) {
            fk = cursor.getInt(cursor.getColumnIndex("marcaId"));
        }

        return fk;
    }

    public List<Cellphone> retrieveModels() {

        String query = "SELECT modelo, marca, celular.marcaId, marca.marcaId FROM celular, marca WHERE celular.marcaId = marca.marcaId;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<Cellphone> results = new ArrayList<>();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String model, brand;

                model = cursor.getString(cursor.getColumnIndex("modelo"));
                brand = cursor.getString(cursor.getColumnIndex("marca"));

                Cellphone cellphone = new Cellphone(model, brand);

                if (!brand.equals(model)) {
                    results.add(cellphone);
                }
            }
        }

        cursor.close();

        return results;

    }

    public List<Cellphone> retrieveBrands() {

        String query = "SELECT marca FROM marca;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<Cellphone> results = new ArrayList<>();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String model, brand;

                brand = cursor.getString(cursor.getColumnIndex("marca"));
                model = brand;

                Cellphone cellphone = new Cellphone(model, brand);

                if (!brand.equals("") || brand.length() != 0) {
                    results.add(cellphone);
                }
            }
        }

        cursor.close();

        return results;

    }
}
