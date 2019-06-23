package com.example.trabalho3.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
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
import com.example.trabalho3.adapter.CategoriaDadosAdapter;
import com.example.trabalho3.dados.HeadHunterContract;
import com.example.trabalho3.dados.HeadHunterDBHelper;

public class GerenciarCategoriaActivity extends AppCompatActivity {

    private Button criarCategoria;
    private RecyclerView recyclerView;
    private CategoriaDadosAdapter categoriaDadosAdapter;

    //Banco de Dados
    private Cursor cursor;
    private SQLiteDatabase dataBase;
    private HeadHunterDBHelper helper;

    private final int CATEGORIA_SELECIONADA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_categoria);

        helper = new HeadHunterDBHelper(getApplicationContext());
        dataBase = helper.getWritableDatabase();
        cursor = dataBase.query(HeadHunterContract.CategoriaDados.TABLE_NAME, HeadHunterContract.TABELA_CATEGORIA, null, null, null,null, null);//HeadHunterContract.CategoriaDados.COLUMN_TITULO + " ASC");

        criarCategoria = (Button) findViewById(R.id.buttonCadastrarCategoria);
        recyclerView = (RecyclerView) findViewById(R.id.rvGerenciarCategoria);
        categoriaDadosAdapter = new CategoriaDadosAdapter(cursor);
        recyclerView.setAdapter(categoriaDadosAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        criarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarNovaCategoria();
            }
        });

        categoriaDadosAdapter.setOnCategoriaDadosClickListener(new CategoriaDadosAdapter.OnCategoriaDadosClickListener() {
            @Override
            public void onCategoriaDadosClick(View v, int position) {
                cursor.moveToPosition(position);
                Intent intent = new Intent(GerenciarCategoriaActivity.this, CategoriaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("idCategoria", cursor.getInt(cursor.getColumnIndex(HeadHunterContract.CategoriaDados._ID)));
                intent.putExtra("info",bundle);
                startActivityForResult(intent,CATEGORIA_SELECIONADA);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            atualizaDados();
    }

    private void atualizaDados(){
        cursor = dataBase.query(HeadHunterContract.CategoriaDados.TABLE_NAME, HeadHunterContract.TABELA_CATEGORIA, null, null, null,null, null);//HeadHunterContract.CategoriaDados.COLUMN_TITULO + " ASC");
        categoriaDadosAdapter.alteraDados(cursor);
    }

    private void criarNovaCategoria(){
        AlertDialog.Builder builder = new AlertDialog.Builder(GerenciarCategoriaActivity.this);
        View viewBuilder = getLayoutInflater().inflate(R.layout.nova_categoria_dialog_layout,null);
        final EditText tituloCategoria = (EditText) viewBuilder.findViewById(R.id.txtNovaCategoria);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContentValues valuesCategoria = new ContentValues();
                valuesCategoria.put(HeadHunterContract.CategoriaDados.COLUMN_TITULO,tituloCategoria.getText().toString());
                dataBase.insert(HeadHunterContract.CategoriaDados.TABLE_NAME,null,valuesCategoria);
                cursor = dataBase.query(HeadHunterContract.CategoriaDados.TABLE_NAME, HeadHunterContract.TABELA_CATEGORIA, null, null, null,null, null);
                categoriaDadosAdapter.alteraDados(cursor);
                Toast.makeText(GerenciarCategoriaActivity.this,"Categoria Criada", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setTitle("Criar nova Categoria");
        builder.setView(viewBuilder);
        builder.show();
    }
}
