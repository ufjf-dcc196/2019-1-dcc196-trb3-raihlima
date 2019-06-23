package com.example.trabalho3.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabalho3.R;
import com.example.trabalho3.dados.HeadHunterContract;

public class SelecionarCategoriaAdapter extends RecyclerView.Adapter <SelecionarCategoriaAdapter.ViewHolder>{
    private Cursor cursor;
    private SelecionarCategoriaAdapter.OnSelecionarCategoriaClickListener listener;
    private int selecionado = -1;

    public SelecionarCategoriaAdapter(){
    }

    public SelecionarCategoriaAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    public void alteraDados(Cursor cursor){
        this.cursor = cursor;
        //notifyDataSetChanged();
    }

    public void setOnSelecionarCategoriaClickListener(SelecionarCategoriaAdapter.OnSelecionarCategoriaClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public SelecionarCategoriaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View linha = inflater.inflate(R.layout.producao_categoria_layout, viewGroup, false);
        SelecionarCategoriaAdapter.ViewHolder vh = new SelecionarCategoriaAdapter.ViewHolder(linha);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SelecionarCategoriaAdapter.ViewHolder viewHolder, int index) {
        cursor.moveToPosition(index);
        String titulo = this.cursor.getString(cursor.getColumnIndex(HeadHunterContract.CategoriaDados.COLUMN_TITULO));

        viewHolder.titulo.setText(titulo);
        viewHolder.titulo.setChecked(index==selecionado);
    }

    @Override
    public int getItemCount() {
        return this.cursor.getCount();
    }

    public int getSelecionado() {
        return selecionado;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RadioButton titulo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = (RadioButton) itemView.findViewById(R.id.radioButtonCategoria);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selecionado = getAdapterPosition();
                    notifyDataSetChanged();
                }
            };
            itemView.setOnClickListener(clickListener);
            titulo.setOnClickListener(clickListener);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position!=RecyclerView.NO_POSITION){
                listener.onSelecionarCategoriaClick(v,position);
            }

        }
    }

    public interface OnSelecionarCategoriaClickListener {
        public void onSelecionarCategoriaClick(View v, int position);
    }
}
