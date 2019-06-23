package com.example.trabalho3.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
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

public class CadastrarCandidatoActivity extends AppCompatActivity {

    private Button confirmar;
    private EditText nome;
    private EditText dataNascimento;
    private EditText perfil;
    private EditText telefone;
    private EditText email;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_candidato);

        confirmar = (Button) findViewById(R.id.buttonCadastrarNovoCandidato);
        nome = (EditText) findViewById(R.id.txtNomeCandidato);
        dataNascimento = (EditText) findViewById(R.id.txtDataNascimentoCandidato);
        perfil = (EditText) findViewById(R.id.txtPerfilCandidato);
        telefone = (EditText) findViewById(R.id.txtTelefoneCandidato);
        email = (EditText) findViewById(R.id.txtEmailCandidato);

        setTitle("Cadastrar Candidato");

        dataNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gerarCalendario();
            }
        });

        dataNascimento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==true){
                    gerarCalendario();
                }
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

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificaPreenchimento()) {
                    gravarCandidato();
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(CadastrarCandidatoActivity.this, "Preencha todos os dados!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void gerarCalendario(){
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dateDialog= new DatePickerDialog(CadastrarCandidatoActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,ano,mes,dia);
        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateDialog.show();
    }

    private void gravarCandidato(){
        ContentValues values = new ContentValues();

        values.put(HeadHunterContract.CandidatoDados.COLUMN_NOME, nome.getText().toString());

        String inDate = dataNascimento.getText().toString();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Timestamp ts = new Timestamp(((java.util.Date)df.parse(inDate)).getTime());
            values.put(HeadHunterContract.CandidatoDados.COLUMN_DATA_NASCIMENTO, ts.toString());

        } catch (ParseException e) {
            Timestamp dataDeHoje = new Timestamp(System.currentTimeMillis());
            values.put(HeadHunterContract.CandidatoDados.COLUMN_DATA_NASCIMENTO, dataDeHoje.toString());
        }

        values.put(HeadHunterContract.CandidatoDados.COLUMN_PERFIL, perfil.getText().toString());
        values.put(HeadHunterContract.CandidatoDados.COLUMN_TELEFONE, telefone.getText().toString());
        values.put(HeadHunterContract.CandidatoDados.COLUMN_EMAIL, email.getText().toString());

        HeadHunterDBHelper helper = new HeadHunterDBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        long novoID = db.insert(HeadHunterContract.CandidatoDados.TABLE_NAME,null,values);
        Toast.makeText(CadastrarCandidatoActivity.this,"Nova Tarefa criada com o id: " + novoID,Toast.LENGTH_SHORT).show();
    }


    private boolean verificaPreenchimento() {
        if (nome.getText().toString().equals("")) {
            return false;
        } else if (dataNascimento.getText().toString().equals("")) {
            return false;
        } else if (perfil.getText().toString().equals("")) {
            return false;
        } else if (email.getText().toString().equals("")) {
            return false;
        } else if (telefone.getText().toString().equals("")) {
            return false;
        }
        return true;
    }
}
