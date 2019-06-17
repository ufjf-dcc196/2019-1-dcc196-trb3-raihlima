package com.example.trabalho3.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.trabalho3.R;
import com.example.trabalho3.adapter.CandidatoDadosAdapter;
import com.example.trabalho3.adapter.CategoriaDadosAdapter;
import com.example.trabalho3.dados.HeadHunterContract;
import com.example.trabalho3.dados.HeadHunterDBHelper;

public class GerenciarCategoriaActivity extends AppCompatActivity {

    private Button criarCategoria;
    private RecyclerView recyclerView;
    private CategoriaDadosAdapter categoriaDadosAdapter;

    //Banco de Dados
    private Cursor cursor;
    private SQLiteDatabase dataBase;
    private HeadHunterDBHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_categoria);

        helper = new HeadHunterDBHelper(getApplicationContext());
        dataBase = helper.getWritableDatabase();
        cursor = dataBase.query(HeadHunterContract.CategoriaDados.TABLE_NAME, HeadHunterContract.TABELA_CATEGORIA, null, null, null,null, HeadHunterContract.CategoriaDados.COLUMN_TITULO + " ASC");

        criarCategoria = (Button) findViewById(R.id.buttonCadastrarCategoria);
        recyclerView = (RecyclerView) findViewById(R.id.rvGerenciarCandidato);
        categoriaDadosAdapter = new CategoriaDadosAdapter(cursor);
        recyclerView.setAdapter(categoriaDadosAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        criarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
