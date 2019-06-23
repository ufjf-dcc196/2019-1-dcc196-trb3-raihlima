package com.example.trabalho3.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trabalho3.R;
import com.example.trabalho3.adapter.CandidatoDadosAdapter;
import com.example.trabalho3.adapter.ProducaoAdapter;
import com.example.trabalho3.dados.HeadHunterContract;
import com.example.trabalho3.dados.HeadHunterDBHelper;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    private DatePickerDialog.OnDateSetListener mDateSetListener;

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


        id = getIntent().getBundleExtra("info").getInt("id");
        preencheInfo();

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
                    alteraRegistro();
                    alteraBloqueio(false);
                    editavel = false;
                    alteraNomeBotao();
                    Toast.makeText(CandidatoActivity.this, "Alterações salvas!", Toast.LENGTH_SHORT).show();
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
                            removeRegistro();
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
                } else {
                    editavel=false;
                    alteraNomeBotao();
                    alteraBloqueio(false);
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
                    bundle.putInt("idProducao",-1);
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
                bundle.putInt("idCandidato",id);
                intent.putExtra("info",bundle);
                startActivityForResult(intent, GERENCIAR_PRODUCAO);
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(month<9){
                    dataNascimento.setText(dayOfMonth+"/0"+(month+1)+"/"+year);
                } else {
                    dataNascimento.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                }
            }
        };

        dataNascimento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    gerarCalendario();
                }
            }
        });

        dataNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gerarCalendario();
            }
        });
    }

    private void gerarCalendario(){
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dateDialog= new DatePickerDialog(CandidatoActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,ano,mes,dia);
        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateDialog.show();
    }

    private void removeRegistro(){
        String where = HeadHunterContract.CandidatoDados._ID + " = " + id;
        dataBase.delete(HeadHunterContract.CandidatoDados.TABLE_NAME,where,null);

        where = HeadHunterContract.ProducaoDados.COLUMN_ID_CANDIDATO + " = " + id;
        Cursor cursorAux = dataBase.query(HeadHunterContract.ProducaoDados.TABLE_NAME,HeadHunterContract.TABELA_PRODUCAO,where,null,null,null,null);
        for(int i=0;i< cursorAux.getCount();i++){
            cursorAux.moveToPosition(i);
            where = HeadHunterContract.AtividadeDados.COLUMN_ID_PRODUCAO + " = " + cursorAux.getInt(cursorAux.getColumnIndex(HeadHunterContract.ProducaoDados._ID));
            dataBase.delete(HeadHunterContract.AtividadeDados.TABLE_NAME,where,null);
        }
        where = HeadHunterContract.ProducaoDados.COLUMN_ID_CANDIDATO + " = " + id;
        dataBase.delete(HeadHunterContract.ProducaoDados.TABLE_NAME,where,null);
        dataBase.close();
    }

    private void alteraRegistro(){
        ContentValues values = new ContentValues();
        values.put(HeadHunterContract.CandidatoDados.COLUMN_NOME,nome.getText().toString());
        values.put(HeadHunterContract.CandidatoDados.COLUMN_PERFIL,perfil.getText().toString());
        values.put(HeadHunterContract.CandidatoDados.COLUMN_TELEFONE,telefone.getText().toString());
        values.put(HeadHunterContract.CandidatoDados.COLUMN_EMAIL,email.getText().toString());

        String inDate = dataNascimento.getText().toString();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Timestamp ts = new Timestamp(((java.util.Date)df.parse(inDate)).getTime());
            values.put(HeadHunterContract.CandidatoDados.COLUMN_DATA_NASCIMENTO, ts.toString());

        } catch (ParseException e) {
            Timestamp dataDeHoje = new Timestamp(System.currentTimeMillis());
            values.put(HeadHunterContract.CandidatoDados.COLUMN_DATA_NASCIMENTO, dataDeHoje.toString());
        }

        String where = "_ID" + "=" + id;
        dataBase.update(HeadHunterContract.CandidatoDados.TABLE_NAME,values,where,null);
        preencheInfo();
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

    private void preencheInfo() {
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
