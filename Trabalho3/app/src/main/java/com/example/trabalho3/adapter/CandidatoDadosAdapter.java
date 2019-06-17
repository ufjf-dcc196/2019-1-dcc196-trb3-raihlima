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

public class CandidatoDadosAdapter extends RecyclerView.Adapter <CandidatoDadosAdapter.ViewHolder> {
    private Cursor cursor;
    private CandidatoDadosAdapter.OnCandidatoDadosClickListener listener;

    public CandidatoDadosAdapter(){

    }

    public CandidatoDadosAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    public void alteraDados(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public void setOnCandidatoDadosClickListener(CandidatoDadosAdapter.OnCandidatoDadosClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public CandidatoDadosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View linha = inflater.inflate(R.layout.gerenciar_cadidato_layout, viewGroup, false);
        CandidatoDadosAdapter.ViewHolder vh = new CandidatoDadosAdapter.ViewHolder(linha);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CandidatoDadosAdapter.ViewHolder viewHolder, int index) {
        cursor.moveToPosition(index);
        String nome = this.cursor.getString(cursor.getColumnIndex(HeadHunterContract.CandidatoDados.COLUMN_NOME));

        viewHolder.nome.setText(nome);
    }

    @Override
    public int getItemCount() {
        return this.cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nome;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = (TextView) itemView.findViewById(R.id.txtGerenciarCandidato);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener!=null){
                        listener.onCandidatoDadosClick(v, position);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position!=RecyclerView.NO_POSITION){
                listener.onCandidatoDadosClick(v,position);
            }
        }
    }

    public interface OnCandidatoDadosClickListener {
        public void onCandidatoDadosClick(View v, int position);
    }
}
