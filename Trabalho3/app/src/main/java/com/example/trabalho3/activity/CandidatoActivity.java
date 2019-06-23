package com.example.trabalho3.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
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
import com.example.trabalho3.adapter.CandidatoDadosAdapter;
import com.example.trabalho3.adapter.ProducaoAdapter;
import com.example.trabalho3.dados.HeadHunterContract;
import com.example.trabalho3.dados.HeadHunterDBHelper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class CandidatoActivity extends AppCompatActivity {

    private int id; //ID do Candidato para preencher os campos

    private boolean editavel = false;

    private EditText nome;
    private EditText dataNascimento;
    private EditText perfil;
    private EditText telefone;
    private EditText email;
    private Button editarCandidato;
    private Button excluirCandidato;
    private Button adicionarProducao;

    private RecyclerView recyclerView;
    private ProducaoAdapter adapter;

    //Banco de Dados
    private Cursor cursor;
    private Cursor cursorProducao;
    private SQLiteDatabase dataBase;
    private HeadHunterDBHelper helper;

    private final int CADASTRAR_PRODUCAO = 1;

    private final int GERENCIAR_PRODUCAO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidato);

        helper = new HeadHunterDBHelper(getApplicationContext());
        dataBase = helper.getWritableDatabase();

        nome = (EditText) findViewById(R.id.txtNomeCandidato0);
        dataNascimento = (EditText) findViewById(R.id.txtDataNascimentoCandidato0);
        perfil = (EditText) findViewById(R.id.txtPerfilCandidato0);
        telefone = (EditText) findViewById(R.id.txtTelefoneCandidato0);
        email = (EditText) findViewById(R.id.txtEmailCandidato0);

        editarCandidato = (Button) findViewById(R.id.buttonEditarCandidato);
        excluirCandidato = (Button) findViewById(R.id.buttonExcluirCandidato);
        adicionarProducao = (Button) findViewById(R.id.buttonAdicionarProducao);

        preencheInfo(getIntent().getBundleExtra("info"));

        recyclerView = (RecyclerView) findViewById(R.id.rvProducaoCandidato);
        adapter = new ProducaoAdapter(cursorProducao);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        alteraBloqueio(false);

        editarCandidato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editavel==false){
                    alteraBloqueio(true);
                    editavel = true;
                    alteraNomeBotao();
                } else {
                    alteraBloqueio(false);
                    editavel = false;
                    alteraNomeBotao();
                    Toast.makeText(CandidatoActivity.this, "Alterações salvas!", Toast.LENGTH_SHORT);
                }

            }
        });

        excluirCandidato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editavel==false){
                    AlertDialog.Builder mensagem = new AlertDialog.Builder(CandidatoActivity.this);
                    mensagem.setTitle("Alerta");
                    mensagem.setMessage("Tem certeza que deseja excluir o candidato?");
                    mensagem.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //removeRegistro(id);
                            Toast.makeText(CandidatoActivity.this, "Candidato excluido", Toast.LENGTH_SHORT).show();
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
            }
        });

        adicionarProducao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursorCategoria = dataBase.query(HeadHunterContract.CategoriaDados.TABLE_NAME, HeadHunterContract.TABELA_CATEGORIA, null, null, null, null, null);
                if(cursorCategoria.getCount()>0){
                    Intent intent = new Intent(CandidatoActivity.this, CadastrarProducaoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("idCandidato",id);
                    intent.putExtra("info",bundle);
                    startActivityForResult(intent,CADASTRAR_PRODUCAO);
                } else {
                    AlertDialog.Builder mensagem = new AlertDialog.Builder(CandidatoActivity.this);
                    mensagem.setTitle("Erro");
                    mensagem.setMessage("Não existem categorias, por favor crie uma!");
                    mensagem.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    mensagem.show();
                }

            }
        });

        adapter.setOnProducaoDadosClickListener(new ProducaoAdapter.OnProducaoDadosClickListener() {
            @Override
            public void onProducaoDadosClick(View v, int position) {
                Intent intent = new Intent(CandidatoActivity.this, ProducaoActivity.class);
                Bundle bundle = new Bundle();
                cursorProducao.moveToPosition(position);
                int idProducao = cursorProducao.getInt(cursorProducao.getColumnIndex(HeadHunterContract.ProducaoDados._ID));
                bundle.putInt("idProducao", idProducao);
                intent.putExtra("info",bundle);
                startActivityForResult(intent, GERENCIAR_PRODUCAO);
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
        String where = HeadHunterContract.ProducaoDados.COLUMN_ID_CANDIDATO + " = " + id;
        cursorProducao = dataBase.query(HeadHunterContract.ProducaoDados.TABLE_NAME, HeadHunterContract.TABELA_PRODUCAO, where,null,null,null, null);
        adapter.alteraDados(cursorProducao);
    }

    private void preencheInfo(Bundle bundle) {
        id = bundle.getInt("id");
        String where = "_ID = " + id;
        cursor = dataBase.query(HeadHunterContract.CandidatoDados.TABLE_NAME, HeadHunterContract.TABELA_CANDIDATO, where, null, null, null, null);
        cursor.moveToFirst();
        nome.setText(this.cursor.getString(cursor.getColumnIndex(HeadHunterContract.CandidatoDados.COLUMN_NOME)));
        perfil.setText(this.cursor.getString(cursor.getColumnIndex(HeadHunterContract.CandidatoDados.COLUMN_PERFIL)));
        telefone.setText(this.cursor.getString(cursor.getColumnIndex(HeadHunterContract.CandidatoDados.COLUMN_TELEFONE)));
        email.setText(this.cursor.getString(cursor.getColumnIndex(HeadHunterContract.CandidatoDados.COLUMN_EMAIL)));

        Timestamp ts = Timestamp.valueOf(this.cursor.getString(cursor.getColumnIndex(HeadHunterContract.CandidatoDados.COLUMN_DATA_NASCIMENTO)));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataN = dateFormat.format(ts);

        dataNascimento.setText(dataN);

        where = HeadHunterContract.ProducaoDados.COLUMN_ID_CANDIDATO + " = " + id;
        cursorProducao = dataBase.query(HeadHunterContract.ProducaoDados.TABLE_NAME, HeadHunterContract.TABELA_PRODUCAO, where,null,null,null, null);
    }

    private void alteraBloqueio(Boolean valor){
        nome.setEnabled(valor);
        dataNascimento.setEnabled(valor);
        perfil.setEnabled(valor);
        telefone.setEnabled(valor);
        email.setEnabled(valor);
    }

    private void alteraNomeBotao(){
        if(editavel==false){
            editarCandidato.setText("Editar Candidato");
            excluirCandidato.setText("Excluir Candidato");
        } else {
            editarCandidato.setText("Confirmar Alterações");
            excluirCandidato.setText("Cancelar Alterações");
        }
    }
}
