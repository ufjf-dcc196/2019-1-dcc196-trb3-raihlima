package com.example.trabalho3.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.trabalho3.R;
import com.example.trabalho3.adapter.AtividadeAdapter;
import com.example.trabalho3.adapter.ProducaoAdapter;
import com.example.trabalho3.dados.HeadHunterContract;
import com.example.trabalho3.dados.HeadHunterDBHelper;

public class ProducaoActivity extends AppCompatActivity {

    private int idProducao;
    private Bundle bundle;
    private EditText producao;
    private EditText categoria;
    private Button adicionarAtividade;
    private Button editarProducao;
    private Button excluirProducao;

    private RecyclerView recyclerView;
    private AtividadeAdapter adapter;

    private Cursor cursorProducao;
    private Cursor cursorAtividade;
    private Cursor cursorCategoria;
    private SQLiteDatabase database;
    private HeadHunterDBHelper helper;

    private final int CRIAR_ATIVIDADE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producao);

        producao = (EditText) findViewById(R.id.edtTxtTituloProducao);
        categoria = (EditText) findViewById(R.id.edtTxtCategoriaProducao);
        adicionarAtividade = (Button) findViewById(R.id.buttonAdicionarAtividade);
        editarProducao = (Button) findViewById(R.id.buttonEditarProducao);
        excluirProducao = (Button) findViewById(R.id.buttonExcluirProducao);

        helper = new HeadHunterDBHelper(getApplicationContext());
        database = helper.getWritableDatabase();

        bundle = getIntent().getBundleExtra("info");
        preencheDados();

        recyclerView = (RecyclerView) findViewById(R.id.rvAtividades);
        adapter = new AtividadeAdapter(cursorAtividade);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adicionarAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProducaoActivity.this, AtividadeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("idProducao",idProducao);
                intent.putExtra("info",bundle);
                startActivityForResult(intent,CRIAR_ATIVIDADE);
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            atualizaDados();
        }
    }

    private void atualizaDados(){
        preencheDados();
        adapter.alteraDados(cursorAtividade);
    }

    private void preencheDados(){
        idProducao = bundle.getInt("idProducao");
        String where = "_ID = " + idProducao;
        cursorProducao = database.query(HeadHunterContract.ProducaoDados.TABLE_NAME,HeadHunterContract.TABELA_PRODUCAO,where,null,null,null,null);
        where = HeadHunterContract.AtividadeDados.COLUMN_ID_PRODUCAO + " = " + idProducao;
        cursorAtividade = database.query(HeadHunterContract.AtividadeDados.TABLE_NAME,HeadHunterContract.TABELA_ATIVIDADE,where,null,null,null,null);

        where = String.format("SELECT c.* FROM %s as c, %s as p WHERE p.%s = %s AND p.%s = c.%s", HeadHunterContract.CategoriaDados.TABLE_NAME, HeadHunterContract.ProducaoDados.TABLE_NAME,
                HeadHunterContract.ProducaoDados._ID, idProducao,HeadHunterContract.ProducaoDados.COLUMN_ID_CATEGORIA, HeadHunterContract.CategoriaDados._ID);
        cursorCategoria = database.rawQuery(where,null);
        cursorProducao.moveToFirst();
        cursorCategoria.moveToFirst();
        producao.setText(cursorProducao.getString(cursorProducao.getColumnIndex(HeadHunterContract.ProducaoDados.COLUMN_TITULO)));
        categoria.setText(cursorCategoria.getString(cursorCategoria.getColumnIndex(HeadHunterContract.CategoriaDados.COLUMN_TITULO)));
    }
}
