package com.example.trabalho3.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trabalho3.R;
import com.example.trabalho3.dados.HeadHunterContract;
import com.example.trabalho3.dados.HeadHunterDBHelper;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AtividadeActivity extends AppCompatActivity {

    private int idProducao;
    private int idAtividade;
    private EditText descricao;
    private EditText data;
    private EditText hora;
    private Button confirmar;

    private DatePickerDialog.OnDateSetListener dataListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade);

        idProducao = getIntent().getBundleExtra("info").getInt("idProducao");
        idAtividade = getIntent().getBundleExtra("info").getInt("idAtividade");
        descricao = (EditText) findViewById(R.id.edtTxtDescricaoNovaAtivicade);
        data = (EditText) findViewById(R.id.edtTxtDataNovaAtividade);
        hora = (EditText) findViewById(R.id.edtTxtHoraNovaAtividade);
        confirmar = (Button) findViewById(R.id.buttonConfirmarNovaAtividade);

        preencherdados();

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificaPreenchimento()){
                    salvarDados();
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(AtividadeActivity.this,"Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dataListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(month<9){
                    data.setText(dayOfMonth+"/0"+(month+1)+"/"+year);
                } else {
                    data.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                }

            }
        };

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geraCalendario(1);
            }
        });

        data.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        dateDialog= new DatePickerDialog(AtividadeActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dataListener,ano,mes,dia);
        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateDialog.show();
    }

    public void salvarDados(){
        ContentValues values = new ContentValues();

        values.put(HeadHunterContract.AtividadeDados.COLUMN_DESCRICAO, descricao.getText().toString());

        String inDate = data.getText().toString();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Timestamp ts = new Timestamp(((java.util.Date)df.parse(inDate)).getTime());
            values.put(HeadHunterContract.AtividadeDados.COLUMN_DATA, ts.toString());

        } catch (ParseException e) {
            Timestamp dataDeHoje = new Timestamp(System.currentTimeMillis());
            values.put(HeadHunterContract.AtividadeDados.COLUMN_DATA, dataDeHoje.toString());
        }
        values.put(HeadHunterContract.AtividadeDados.COLUMN_HORAS, hora.getText().toString());


        values.put(HeadHunterContract.AtividadeDados.COLUMN_ID_PRODUCAO,idProducao);
        HeadHunterDBHelper helper = new HeadHunterDBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        if(idAtividade==-1){
            long novoID = db.insert(HeadHunterContract.AtividadeDados.TABLE_NAME,null,values);
            Toast.makeText(AtividadeActivity.this,"Nova Atividade criada.",Toast.LENGTH_SHORT).show();
        } else {
            String where = HeadHunterContract.AtividadeDados._ID + " = " + idAtividade;
            db.update(HeadHunterContract.AtividadeDados.TABLE_NAME,values,where,null);
            Toast.makeText(AtividadeActivity.this,"Atividade alterada.",Toast.LENGTH_SHORT).show();
        }

    }

    public boolean verificaPreenchimento(){
        if(descricao.getText().equals("")){
            return false;
       // } else if (data.getText().equals("")){
          //  return false;
        } else if (hora.getText().equals("")){
            return false;
        }
        return true;
    }

    public void preencherdados(){
        setTitle("Cadastrar Atividade");
        if(idAtividade>-1){
            setTitle("Editar Atividade");
            HeadHunterDBHelper helper = new HeadHunterDBHelper(getApplicationContext());
            SQLiteDatabase db = helper.getWritableDatabase();
            String where = HeadHunterContract.AtividadeDados._ID + " = " + idAtividade;
            Cursor cursorAux = db.query(HeadHunterContract.AtividadeDados.TABLE_NAME, HeadHunterContract.TABELA_ATIVIDADE, where, null,null,null,null);
            cursorAux.moveToFirst();
            descricao.setText(cursorAux.getString(cursorAux.getColumnIndex(HeadHunterContract.AtividadeDados.COLUMN_DESCRICAO)));
            Timestamp ts = Timestamp.valueOf(cursorAux.getString(cursorAux.getColumnIndex(HeadHunterContract.AtividadeDados.COLUMN_DATA)));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dataL = dateFormat.format(ts);
            data.setText(dataL);
            hora.setText(cursorAux.getString(cursorAux.getColumnIndex(HeadHunterContract.AtividadeDados.COLUMN_HORAS)));
            confirmar.setText("Confirmar Alterações");
        }
    }
}
