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

public class AtividadeAdapter extends RecyclerView.Adapter <AtividadeAdapter.ViewHolder> {
    private Cursor cursor;
    private AtividadeAdapter.OnAtividadeDadosClickListener listener;

    public AtividadeAdapter(){

    }

    public AtividadeAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    public void alteraDados(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public void setOnAtividadeDadosClickListener(AtividadeAdapter.OnAtividadeDadosClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public AtividadeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View linha = inflater.inflate(R.layout.atividade_layout, viewGroup, false);
        AtividadeAdapter.ViewHolder vh = new AtividadeAdapter.ViewHolder(linha);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AtividadeAdapter.ViewHolder viewHolder, int index) {
        cursor.moveToPosition(index);
        String horas = this.cursor.getString(cursor.getColumnIndex(HeadHunterContract.AtividadeDados.COLUMN_HORAS));
        String descricao = this.cursor.getString(cursor.getColumnIndex(HeadHunterContract.AtividadeDados.COLUMN_DESCRICAO));

        viewHolder.horas.setText(horas);
        viewHolder.descricao.setText(descricao);
    }

    @Override
    public int getItemCount() {
        return this.cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView horas;
        TextView descricao;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            horas = (TextView) itemView.findViewById(R.id.txtHorasAtividadeLayout);
            descricao = (TextView) itemView.findViewById(R.id.txtDescricaoAtividadeLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener!=null){
                        listener.onAtividadeDadosClick(v, position);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position!=RecyclerView.NO_POSITION){
                listener.onAtividadeDadosClick(v,position);
            }
        }
    }

    public interface OnAtividadeDadosClickListener {
        public void onAtividadeDadosClick(View v, int position);
    }
}
