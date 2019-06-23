package com.example.trabalho3.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.trabalho3.R;
import com.example.trabalho3.adapter.AtividadeAdapter;
import com.example.trabalho3.adapter.ProducaoAdapter;
import com.example.trabalho3.dados.HeadHunterContract;
import com.example.trabalho3.dados.HeadHunterDBHelper;

public class ProducaoActivity extends AppCompatActivity {

    private int idProducao;
    private int idCandidato;
    private Bundle bundle;
    private EditText producao;
    private EditText categoria;
    private Button adicionarAtividade;
    private Button editarProducao;
    private Button excluirProducao;
    private boolean editavel = false;

    private RecyclerView recyclerView;
    private AtividadeAdapter adapter;

    private Cursor cursorProducao;
    private Cursor cursorAtividade;
    private Cursor cursorCategoria;
    private SQLiteDatabase database;
    private HeadHunterDBHelper helper;

    private final int CRIAR_ATIVIDADE = 1;
    private final int EDITAR_PRODUCAO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producao);

        producao = (EditText) findViewById(R.id.edtTxtTituloProducao);
        categoria = (EditText) findViewById(R.id.edtTxtCategoriaProducao);
        adicionarAtividade = (Button) findViewById(R.id.buttonAdicionarAtividade);
        editarProducao = (Button) findViewById(R.id.buttonEditarProducao);
        excluirProducao = (Button) findViewById(R.id.buttonExcluirProducao);

        setTitle("Produção");

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
                bundle.putInt("idAtividade",-1);
                intent.putExtra("info",bundle);
                startActivityForResult(intent,CRIAR_ATIVIDADE);
            }
        });

        excluirProducao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mensagem = new AlertDialog.Builder(ProducaoActivity.this);
                mensagem.setTitle("Alerta");
                mensagem.setMessage("Você realmente deseja excluir a Produção?");
                mensagem.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeRegistro();
                        Toast.makeText(ProducaoActivity.this,"Produção excluida", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });
                mensagem.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                mensagem.show();
            }
        });

        editarProducao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProducaoActivity.this, CadastrarProducaoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("idProducao",idProducao);
                bundle.putInt("idCandidato",idCandidato);
                intent.putExtra("info",bundle);
                startActivityForResult(intent,EDITAR_PRODUCAO);
            }
        });

        adapter.setOnAtividadeDadosClickListener(new AtividadeAdapter.OnAtividadeDadosClickListener() {
            @Override
            public void onAtividadeDadosClick(View v, int position) {
                final int index = position;
                AlertDialog.Builder mensagem = new AlertDialog.Builder(ProducaoActivity.this);
                mensagem.setTitle("Informação");
                mensagem.setMessage("O que deseja fazer com a Atividade?");
                mensagem.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(ProducaoActivity.this, AtividadeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("idProducao",idProducao);
                        cursorAtividade.moveToPosition(index);

                        bundle.putInt("idAtividade",cursorAtividade.getInt(cursorAtividade.getColumnIndex(HeadHunterContract.AtividadeDados._ID)));
                        intent.putExtra("info",bundle);
                        startActivityForResult(intent,CRIAR_ATIVIDADE);
                        //Toast.makeText(ProducaoActivity.this, "Candidato excluido", Toast.LENGTH_SHORT).show();
                        //setResult(Activity.RESULT_OK);
                        //finish();
                    }
                });
                mensagem.setNeutralButton("Excluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder msgExcluir = new AlertDialog.Builder(ProducaoActivity.this);
                        msgExcluir.setTitle("Alerta");
                        msgExcluir.setMessage("Deseja realmente excluir a atividade?");
                        msgExcluir.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                removerAtividade(index);
                                atualizaDados();
                                Toast.makeText(ProducaoActivity.this, "Atividade excluida", Toast.LENGTH_SHORT).show();
                            }
                        });
                        msgExcluir.setNeutralButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        msgExcluir.show();
                    }
                });
                mensagem.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                mensagem.show();
            }
        });
    }

    private void removerAtividade(int index){
        cursorAtividade.moveToPosition(index);
        String where = HeadHunterContract.AtividadeDados._ID + " = " + cursorAtividade.getInt(cursorAtividade.getColumnIndex(HeadHunterContract.AtividadeDados._ID));
        database.delete(HeadHunterContract.AtividadeDados.TABLE_NAME, where,null);

    }

    private void removeRegistro(){
        String where = HeadHunterContract.ProducaoDados._ID + " = " + idProducao;
        database.delete(HeadHunterContract.ProducaoDados.TABLE_NAME,where,null);
        where = HeadHunterContract.AtividadeDados.COLUMN_ID_PRODUCAO + " = " + idProducao;
        database.delete(HeadHunterContract.AtividadeDados.TABLE_NAME,where,null);
        database.close();

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
        idCandidato = bundle.getInt("idCandidato");
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
