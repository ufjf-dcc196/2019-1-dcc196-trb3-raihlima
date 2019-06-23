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

public class ProducaoAdapter  extends RecyclerView.Adapter <ProducaoAdapter.ViewHolder> {
    private Cursor cursor;
    private ProducaoAdapter.OnProducaoDadosClickListener listener;

    public ProducaoAdapter(){

    }

    public ProducaoAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    public void alteraDados(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public void setOnProducaoDadosClickListener(ProducaoAdapter.OnProducaoDadosClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProducaoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View linha = inflater.inflate(R.layout.producao_candidato_layout, viewGroup, false);
        ProducaoAdapter.ViewHolder vh = new ProducaoAdapter.ViewHolder(linha);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ProducaoAdapter.ViewHolder viewHolder, int index) {
        cursor.moveToPosition(index);
        String titulo = this.cursor.getString(cursor.getColumnIndex(HeadHunterContract.ProducaoDados.COLUMN_TITULO));
        String descricao = this.cursor.getString(cursor.getColumnIndex(HeadHunterContract.ProducaoDados.COLUMN_DESCRICAO));

        viewHolder.titulo.setText(titulo);
        viewHolder.descricao.setText(descricao);
    }

    @Override
    public int getItemCount() {
        return this.cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titulo;
        TextView descricao;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = (TextView) itemView.findViewById(R.id.txtTituloProducaoCandidato);
            descricao = (TextView) itemView.findViewById(R.id.txtDescricaoProducaoCandidato);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener!=null){
                        listener.onProducaoDadosClick(v, position);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position!=RecyclerView.NO_POSITION){
                listener.onProducaoDadosClick(v,position);
            }
        }
    }

    public interface OnProducaoDadosClickListener {
        public void onProducaoDadosClick(View v, int position);
    }
}
