package com.example.trabalho3.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trabalho3.R;
import com.example.trabalho3.dados.HeadHunterContract;

public class CategoriaDadosAdapter extends RecyclerView.Adapter <CategoriaDadosAdapter.ViewHolder> {
    private Cursor cursor;
    private CategoriaDadosAdapter.OnCategoriaDadosClickListener listener;

    public CategoriaDadosAdapter(){
    }

    public CategoriaDadosAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    public void alteraDados(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public void setOnCategoriaDadosClickListener(CategoriaDadosAdapter.OnCategoriaDadosClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoriaDadosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View linha = inflater.inflate(R.layout.gerenciar_cadidato_layout, viewGroup, false);
        CategoriaDadosAdapter.ViewHolder vh = new CategoriaDadosAdapter.ViewHolder(linha);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaDadosAdapter.ViewHolder viewHolder, int index) {
        cursor.moveToPosition(index);
        String titulo = this.cursor.getString(cursor.getColumnIndex(HeadHunterContract.CategoriaDados.COLUMN_TITULO));

        viewHolder.titulo.setText(titulo);
    }

    @Override
    public int getItemCount() {
        return this.cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titulo;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = (TextView) itemView.findViewById(R.id.txtGerenciarCategoria);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener!=null){
                        listener.onCategoriaDadosClick(v, position);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position!=RecyclerView.NO_POSITION){
                listener.onCategoriaDadosClick(v,position);
            }
        }
    }

    public interface OnCategoriaDadosClickListener {
        public void onCategoriaDadosClick(View v, int position);
    }
}
