package com.example.trabalho3.dados;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HeadHunterDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "head_hunter.db";

    public HeadHunterDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(HeadHunterContract.CandidatoDados.CREATE_TABLE);
        db.execSQL(HeadHunterContract.CategoriaDados.CREATE_TABLE);
        db.execSQL(HeadHunterContract.ProducaoDados.CREATE_TABLE);
        db.execSQL(HeadHunterContract.AtividadeDados.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(HeadHunterContract.CandidatoDados.DROP_TABLE);
        db.execSQL(HeadHunterContract.CategoriaDados.DROP_TABLE);
        db.execSQL(HeadHunterContract.ProducaoDados.DROP_TABLE);
        db.execSQL(HeadHunterContract.AtividadeDados.DROP_TABLE);
    }
}
