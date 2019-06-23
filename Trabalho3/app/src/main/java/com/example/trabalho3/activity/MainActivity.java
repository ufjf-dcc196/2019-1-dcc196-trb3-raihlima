package com.example.trabalho3.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.trabalho3.R;

public class MainActivity extends AppCompatActivity {

    private Button gerenciarCandidato;
    private Button gerenciarCategoria;

    private static final int GERENCIAR_CANDIDATO = 1;
    private static final int GERENCIAR_CATEGORIA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gerenciarCandidato = (Button) findViewById(R.id.buttonGerenciarCandidatos);
        gerenciarCategoria = (Button) findViewById(R.id.buttonGerenciarCategrorias);

        setTitle("Menu Principal");

        gerenciarCandidato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GerenciarCandidatoActivity.class);
                startActivityForResult(intent, GERENCIAR_CANDIDATO);
            }
        });

        gerenciarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GerenciarCategoriaActivity.class);
                startActivityForResult(intent, GERENCIAR_CATEGORIA);
            }
        });
    }
}
