package com.example.trabalho3.dados;

import android.provider.BaseColumns;

public class HeadHunterContract {
    public static final String[] TABELA_CANDIDATO ={
            CandidatoDados._ID,
            CandidatoDados.COLUMN_NOME,
            CandidatoDados.COLUMN_DATA_NASCIMENTO,
            CandidatoDados.COLUMN_PERFIL,
            CandidatoDados.COLUMN_TELEFONE,
            CandidatoDados.COLUMN_EMAIL
    };

    public static final class CandidatoDados implements BaseColumns {
        public static final String TABLE_NAME = "candidato";
        public static final String COLUMN_NOME = "nome";
        public static final String COLUMN_DATA_NASCIMENTO = "data_nascimento";
        public static final String COLUMN_PERFIL = "perfil";
        public static final String COLUMN_TELEFONE = "telefone";
        public static final String COLUMN_EMAIL = "email";
        public static final String CREATE_TABLE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TIMESTAMP, %s TEXT, %s TEXT, %s TEXT)", TABLE_NAME, _ID, COLUMN_NOME, COLUMN_DATA_NASCIMENTO, COLUMN_PERFIL, COLUMN_TELEFONE, COLUMN_EMAIL);
        public static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
    }

    public static final String[] TABELA_PRODUCAO ={
            ProducaoDados._ID,
            ProducaoDados.COLUMN_TITULO,
            ProducaoDados.COLUMN_DESCRICAO,
            ProducaoDados.COLUMN_INICIO,
            ProducaoDados.COLUMN_FIM,
            ProducaoDados.COLUMN_ID_CANDIDATO,
            ProducaoDados.COLUMN_ID_CATEGORIA
    };

    public static final class ProducaoDados implements BaseColumns {
        public static final String TABLE_NAME = "producao";
        public static final String COLUMN_TITULO = "titulo";
        public static final String COLUMN_DESCRICAO = "descricao";
        public static final String COLUMN_INICIO = "inicio";
        public static final String COLUMN_FIM = "fim";
        public static final String COLUMN_ID_CANDIDATO = "id_candidato";
        public static final String COLUMN_ID_CATEGORIA = "id_categoria";
        public static final String CREATE_TABLE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TIMESTAMP, %s TIMESTAMP, %s INTEGER, %s INTEGER, FOREIGN KEY(%s) REFERENCES %s(%s), FOREIGN KEY(%s) REFERENCES %s(%s))",
                TABLE_NAME, _ID, COLUMN_TITULO, COLUMN_DESCRICAO, COLUMN_INICIO, COLUMN_FIM, COLUMN_ID_CANDIDATO, COLUMN_ID_CATEGORIA, COLUMN_ID_CANDIDATO, CandidatoDados.TABLE_NAME, CandidatoDados._ID, COLUMN_ID_CATEGORIA, CategoriaDados._ID, CandidatoDados._ID);
        public static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
    }

    public static final String[] TABELA_CATEGORIA ={
            CategoriaDados._ID,
            CategoriaDados.COLUMN_TITULO,
    };

    public static final class CategoriaDados implements BaseColumns{
        public static final String TABLE_NAME = "categoria";
        public static final String COLUMN_TITULO = "titulo";
        public static final String CREATE_TABLE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT)", TABLE_NAME, _ID, COLUMN_TITULO);
        public static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
    }

    public static final String[] TABELA_ATIVIDADE ={
            AtividadeDados._ID,
            AtividadeDados.COLUMN_DESCRICAO,
            AtividadeDados.COLUMN_DATA,
            AtividadeDados.COLUMN_HORAS,
            AtividadeDados.COLUMN_ID_PRODUCAO
    };

    public static final class AtividadeDados implements BaseColumns {
        public static final String TABLE_NAME = "atividade";
        public static final String COLUMN_DESCRICAO = "descricao";
        public static final String COLUMN_DATA = "data";
        public static final String COLUMN_HORAS = "horas";
        public static final String COLUMN_ID_PRODUCAO = "id_producao";
        public static final String CREATE_TABLE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TIMESTAMP, %s INTEGER, %s INTEGER, FOREIGN KEY(%s) REFERENCES %s(%s))",
                TABLE_NAME, _ID, COLUMN_DESCRICAO, COLUMN_DATA, COLUMN_HORAS, COLUMN_ID_PRODUCAO, COLUMN_ID_PRODUCAO, ProducaoDados.TABLE_NAME, ProducaoDados._ID);
        public static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
    }

}
