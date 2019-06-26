package com.example.trabalho3.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
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
import com.example.trabalho3.adapter.SelecionarCategoriaAdapter;
import com.example.trabalho3.dados.HeadHunterContract;
import com.example.trabalho3.dados.HeadHunterDBHelper;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CadastrarProducaoActivity extends AppCompatActivity {

    private int idCandidato;
    private int idProducao;
    private int idCategoria = -1;
    private EditText titulo;
    private EditText descricao;
    private EditText inicio;
    private EditText fim;
    private RecyclerView recyclerView;
    private SelecionarCategoriaAdapter selecionarCategoriaAdapter;
    private Button confirmar;

    private Cursor cursor;
    private SQLiteDatabase dataBase;
    private HeadHunterDBHelper helper;

    private DatePickerDialog.OnDateSetListener listenerInicio;
    private DatePickerDialog.OnDateSetListener listenerFim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_producao);

        helper = new HeadHunterDBHelper(getApplicationContext());
        dataBase = helper.getWritableDatabase();
        cursor = dataBase.query(HeadHunterContract.CategoriaDados.TABLE_NAME, HeadHunterContract.TABELA_CATEGORIA, null, null, null,null, null);

        idCandidato = getIntent().getBundleExtra("info").getInt("idCandidato");
        idProducao = getIntent().getBundleExtra("info").getInt("idProducao");

        titulo = (EditText) findViewById(R.id.txtTituloNovaProducao);
        descricao = (EditText) findViewById(R.id.txtDescricaoNovaProducao);
        inicio = (EditText) findViewById(R.id.txtInicioNovaProducao);
        fim = (EditText) findViewById(R.id.txtFimNovaProducao);
        confirmar = (Button) findViewById(R.id.buttonCadastrarNovaProducao);
        recyclerView = (RecyclerView) findViewById(R.id.rvNovaProducao);

        preencheCampos();

        selecionarCategoriaAdapter = new SelecionarCategoriaAdapter(cursor, idCategoria);
        recyclerView.setAdapter(selecionarCategoriaAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificaPreenchimento()){
                    gravaDados();
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(CadastrarProducaoActivity.this,"Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listenerInicio = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(month<9){
                    inicio.setText(dayOfMonth+"/0"+(month+1)+"/"+year);
                } else {
                    inicio.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                }

            }
        };

        listenerFim = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(month<9){
                    fim.setText(dayOfMonth+"/0"+(month+1)+"/"+year);
                } else {
                    fim.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                }

            }
        };

        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geraCalendario(0);
            }
        });

        inicio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    geraCalendario(0);
                }
            }
        });

        fim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geraCalendario(1);
            }
        });

        fim.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    geraCalendario(1);
                }
            }
        });
    }

    private void geraCalendario(int i){
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dateDialog;
        if(i==0)
            dateDialog= new DatePickerDialog(CadastrarProducaoActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, listenerInicio,ano,mes,dia);
        else
            dateDialog= new DatePickerDialog(CadastrarProducaoActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, listenerFim,ano,mes,dia);

        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateDialog.show();
    }

    private boolean verificaPreenchimento(){
        if(titulo.getText().equals("")){
            return false;
        } else if(descricao.getText().equals("")){
            return false;
        } else if(inicio.getText().equals("")){
            return false;
      //  } else if(fim.getText().equals("")){
      //      return false;
        } else if(selecionarCategoriaAdapter.getSelecionado()==-1){
            return false;
        }
        return true;
    }

    private void gravaDados(){
        ContentValues values = new ContentValues();

        values.put(HeadHunterContract.ProducaoDados.COLUMN_TITULO, titulo.getText().toString());
        values.put(HeadHunterContract.ProducaoDados.COLUMN_DESCRICAO, descricao.getText().toString());

        String inDate = inicio.getText().toString();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Timestamp ts = new Timestamp(((java.util.Date)df.parse(inDate)).getTime());
            values.put(HeadHunterContract.ProducaoDados.COLUMN_INICIO, ts.toString());

        } catch (ParseException e) {
            Timestamp dataDeHoje = new Timestamp(System.currentTimeMillis());
            values.put(HeadHunterContract.ProducaoDados.COLUMN_INICIO, dataDeHoje.toString());
        }
        inDate = fim.getText().toString();
        try {
            Timestamp ts = new Timestamp(((java.util.Date)df.parse(inDate)).getTime());
            values.put(HeadHunterContract.ProducaoDados.COLUMN_FIM, ts.toString());

        } catch (ParseException e) {
            Timestamp dataDeHoje = new Timestamp(System.currentTimeMillis());
            values.put(HeadHunterContract.ProducaoDados.COLUMN_FIM, dataDeHoje.toString());
        }

        cursor.moveToPosition(selecionarCategoriaAdapter.getSelecionado());
        int idCategoria = cursor.getInt(cursor.getColumnIndex(HeadHunterContract.CategoriaDados._ID));

        values.put(HeadHunterContract.ProducaoDados.COLUMN_ID_CANDIDATO,idCandidato);
        values.put(HeadHunterContract.ProducaoDados.COLUMN_ID_CATEGORIA, idCategoria);
        HeadHunterDBHelper helper = new HeadHunterDBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        if(idProducao==-1){
            long novoID = db.insert(HeadHunterContract.ProducaoDados.TABLE_NAME,null,values);
            Toast.makeText(CadastrarProducaoActivity.this,"Nova Producao criada.",Toast.LENGTH_SHORT).show();
        } else {
            String where = HeadHunterContract.ProducaoDados._ID + " = " + idProducao;

            db.update(HeadHunterContract.ProducaoDados.TABLE_NAME,values,where,null);
            Toast.makeText(CadastrarProducaoActivity.this,"Producao alterada!",Toast.LENGTH_SHORT).show();
        }

    }

    public void preencheCampos(){
        setTitle("Cadastrar Producao");
        if(idProducao>-1){
            setTitle("Editar Producao");
            HeadHunterDBHelper helper = new HeadHunterDBHelper(getApplicationContext());
            SQLiteDatabase db = helper.getWritableDatabase();
            String where = HeadHunterContract.ProducaoDados._ID + " = " + idProducao;
            Cursor cursor = db.query(HeadHunterContract.ProducaoDados.TABLE_NAME,HeadHunterContract.TABELA_PRODUCAO,where,null,null,null,null);
            cursor.moveToFirst();
            titulo.setText(cursor.getString(cursor.getColumnIndex(HeadHunterContract.ProducaoDados.COLUMN_TITULO)));
            descricao.setText(cursor.getString(cursor.getColumnIndex(HeadHunterContract.ProducaoDados.COLUMN_DESCRICAO)));

            Timestamp ts = Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(HeadHunterContract.ProducaoDados.COLUMN_INICIO)));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dataL = dateFormat.format(ts);
            inicio.setText(dataL);

            ts = Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(HeadHunterContract.ProducaoDados.COLUMN_FIM)));
            dataL = dateFormat.format(ts);
            //String dataL  = (this.cursor.getString(cursor.getColumnIndex(TarefaContract.TarefaDados.COLUMN_DATA_LIMITE)));

            fim.setText(dataL);
            confirmar.setText("Salvar Alterações");

            idCategoria = cursor.getInt(cursor.getColumnIndex(HeadHunterContract.ProducaoDados.COLUMN_ID_CATEGORIA));
        }
    }
}
