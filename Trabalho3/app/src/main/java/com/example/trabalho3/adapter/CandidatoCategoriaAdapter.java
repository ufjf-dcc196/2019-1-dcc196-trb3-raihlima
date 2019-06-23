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

public class CandidatoCategoriaAdapter extends RecyclerView.Adapter <CandidatoCategoriaAdapter.ViewHolder> {
    private Cursor cursor;
    private CandidatoCategoriaAdapter.OnCandidatoCategoriaClickListener listener;

    public CandidatoCategoriaAdapter(){

    }

    public CandidatoCategoriaAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    public void alteraDados(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public void setOnCandidatoCategoriaClickListener(CandidatoCategoriaAdapter.OnCandidatoCategoriaClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public CandidatoCategoriaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View linha = inflater.inflate(R.layout.candidato_categoria_layout, viewGroup, false);
        CandidatoCategoriaAdapter.ViewHolder vh = new CandidatoCategoriaAdapter.ViewHolder(linha);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CandidatoCategoriaAdapter.ViewHolder viewHolder, int index) {
        cursor.moveToPosition(index);
        String nome = this.cursor.getString(cursor.getColumnIndex(HeadHunterContract.CandidatoDados.COLUMN_NOME));
        String horas = "";

        viewHolder.nome.setText(nome);
        viewHolder.horas.setText(horas);
    }

    @Override
    public int getItemCount() {
        return this.cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nome;
        TextView horas;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = (TextView) itemView.findViewById(R.id.txtNomeCandidatoCategoria);
            horas = (TextView) itemView.findViewById(R.id.txtHorasCandidatoCategoria);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener!=null){
                        listener.onCandidatoCategoriaClick(v, position);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position!=RecyclerView.NO_POSITION){
                listener.onCandidatoCategoriaClick(v,position);
            }
        }
    }

    public interface OnCandidatoCategoriaClickListener {
        public void onCandidatoCategoriaClick(View v, int position);
    }
}
