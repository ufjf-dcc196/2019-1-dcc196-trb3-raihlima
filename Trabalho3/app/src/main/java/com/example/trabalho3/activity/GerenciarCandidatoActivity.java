package com.example.trabalho3.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.trabalho3.R;
import com.example.trabalho3.adapter.CandidatoDadosAdapter;
import com.example.trabalho3.dados.HeadHunterContract;
import com.example.trabalho3.dados.HeadHunterDBHelper;

public class GerenciarCandidatoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button cadastrarCandidato;
    private CandidatoDadosAdapter candidatoDadosAdapter;

    //banco de dados
    private Cursor cursor;
    private HeadHunterDBHelper helper;
    private SQLiteDatabase dataBase;


    private static final int CADASTRAR_CANDIDATO = 1;
    private static final int VER_CANDIDATO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_candidato);

        setTitle("Lista de Candidatos");

        helper = new HeadHunterDBHelper(getApplicationContext());
        dataBase = helper.getWritableDatabase();
        cursor = dataBase.query(HeadHunterContract.CandidatoDados.TABLE_NAME, HeadHunterContract.TABELA_CANDIDATO, null, null, null,null, HeadHunterContract.CandidatoDados.COLUMN_NOME + " ASC");

        cadastrarCandidato = (Button) findViewById(R.id.buttonCadastrarCandidato);
        recyclerView = (RecyclerView) findViewById(R.id.rvGerenciarCandidato);
        candidatoDadosAdapter = new CandidatoDadosAdapter(cursor);
        recyclerView.setAdapter(candidatoDadosAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cadastrarCandidato = (Button) findViewById(R.id.buttonCadastrarCandidato);

        cadastrarCandidato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GerenciarCandidatoActivity.this, CadastrarCandidatoActivity.class);
                startActivityForResult(intent, CADASTRAR_CANDIDATO);
            }
        });

        candidatoDadosAdapter.setOnCandidatoDadosClickListener(new CandidatoDadosAdapter.OnCandidatoDadosClickListener() {
            @Override
            public void onCandidatoDadosClick(View v, int position) {
                Bundle bundle = new Bundle();
                cursor.moveToPosition(position);
                bundle.putInt("id",cursor.getInt(cursor.getColumnIndex(HeadHunterContract.CandidatoDados._ID)));
                Intent intent = new Intent(GerenciarCandidatoActivity.this, CandidatoActivity.class);
                intent.putExtra("info",bundle);
                startActivityForResult(intent,VER_CANDIDATO);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        atualizaDados();
    }

    private void atualizaDados(){
        cursor = dataBase.query(HeadHunterContract.CandidatoDados.TABLE_NAME, HeadHunterContract.TABELA_CANDIDATO, null, null, null,null, HeadHunterContract.CandidatoDados.COLUMN_NOME + " ASC");
        candidatoDadosAdapter.alteraDados(cursor);
    }
}
