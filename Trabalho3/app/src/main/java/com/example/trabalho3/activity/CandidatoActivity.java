package com.example.trabalho3.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.trabalho3.R;
import com.example.trabalho3.dados.HeadHunterContract;
import com.example.trabalho3.dados.HeadHunterDBHelper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class CandidatoActivity extends AppCompatActivity {

    private int id; //ID do Candidato para preencher os campos

    private EditText nome;
    private EditText dataNascimento;
    private EditText perfil;
    private EditText telefone;
    private EditText email;
    private Button editarCandidato;
    private Button excluirCandidato;

    //Banco de Dados
    private Cursor cursor;
    private SQLiteDatabase dataBase;
    private HeadHunterDBHelper helper;

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

        preencheInfo(getIntent().getBundleExtra("info"));

        alteraBloqueio(false);

        editarCandidato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alteraBloqueio(true);
            }
        });

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
    }

    private void alteraBloqueio(Boolean valor){
        nome.setEnabled(valor);
        dataNascimento.setEnabled(valor);
        perfil.setEnabled(valor);
        telefone.setEnabled(valor);
        email.setEnabled(valor);
    }
}
