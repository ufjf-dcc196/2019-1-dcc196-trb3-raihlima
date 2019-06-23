package com.example.trabalho3.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabalho3.R;
import com.example.trabalho3.adapter.CandidatoCategoriaAdapter;
import com.example.trabalho3.dados.HeadHunterContract;
import com.example.trabalho3.dados.HeadHunterDBHelper;

public class CategoriaActivity extends AppCompatActivity {

    private int idCategoria;
    private TextView tituloCategoria;
    private Button editarCategoria;
    private Button excluirCategoria;
    private RecyclerView recyclerView;
    private CandidatoCategoriaAdapter adapter;

    private SQLiteDatabase database;
    private HeadHunterDBHelper helper;
    private Cursor cursorCategoria;
    private Cursor cursorCandidatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        idCategoria = getIntent().getBundleExtra("info").getInt("idCategoria");
        tituloCategoria = (TextView) findViewById(R.id.txtTituloCategoria);
        editarCategoria = (Button) findViewById(R.id.buttonEditarCategoria);
        excluirCategoria = (Button) findViewById(R.id.buttonExcluirCategoria);

        helper = new HeadHunterDBHelper(getApplicationContext());
        database = helper.getWritableDatabase();

        preencheInfo();

        excluirCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mensagem = new AlertDialog.Builder(CategoriaActivity.this);
                mensagem.setTitle("Alerta");
                mensagem.setMessage("Tem certeza que deseja excluir a categoria?");
                mensagem.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removerCategoria();
                        setResult(Activity.RESULT_OK);
                        Toast.makeText(CategoriaActivity.this, "Categoria excluída", Toast.LENGTH_SHORT).show();
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

        editarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CategoriaActivity.this);
                View viewBuilder = getLayoutInflater().inflate(R.layout.nova_categoria_dialog_layout,null);
                final EditText titulo = (EditText) viewBuilder.findViewById(R.id.txtNovaCategoria);
                titulo.setText(tituloCategoria.getText().toString());
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentValues valuesCategoria = new ContentValues();
                        valuesCategoria.put(HeadHunterContract.CategoriaDados.COLUMN_TITULO,titulo.getText().toString());
                        String where = HeadHunterContract.CategoriaDados._ID + " = " + idCategoria;
                        database.update(HeadHunterContract.CategoriaDados.TABLE_NAME, valuesCategoria, where, null);
                        Toast.makeText(CategoriaActivity.this,"Categoria Alterada", Toast.LENGTH_SHORT).show();
                        preencheInfo();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setTitle("Editar Categoria");
                builder.setView(viewBuilder);
                builder.show();
            }
        });
    }

    public void alterarCategoria(){

    }

    public void removerCategoria(){
        String where = HeadHunterContract.CategoriaDados._ID + " = " + idCategoria;
        database.delete(HeadHunterContract.CategoriaDados.TABLE_NAME,where,null);
        database.close();
    }

    public void preencheInfo(){
        String where = HeadHunterContract.CategoriaDados._ID + " = " + idCategoria;
        cursorCategoria = database.query(HeadHunterContract.CategoriaDados.TABLE_NAME, HeadHunterContract.TABELA_CATEGORIA, where, null,null,null,null);
        cursorCategoria.moveToFirst();
        tituloCategoria.setText(cursorCategoria.getString(cursorCategoria.getColumnIndex(HeadHunterContract.CategoriaDados.COLUMN_TITULO)));

    }
}
