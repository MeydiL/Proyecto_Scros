package com.example.proyecto_scros.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_scros.R;

public class ViewHolder_Actividad extends RecyclerView.ViewHolder {

    View mView;

    private ViewHolder_Actividad.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder_Actividad.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolder_Actividad(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getBindingAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getBindingAdapterPosition());
                return false;
            }
        });
    }

    public void SetearDatos(Context context,
                            String id_actividad,
                            String uid_usuario,
                            String titulo,
                            String descripcion,
                            String fecha_hora_registro,
                            String fecha_actividad){

        //declarar vistas
        TextView Id_actividad_Item, Uid_Usuario_Item, Fecha_hora_registro_Item, Titulo_Item,
                Descripcion_Item, Fecha_Item;

        //establecer conexcion con item
        Id_actividad_Item = mView.findViewById(R.id.Id_actividad_Item);
        Uid_Usuario_Item = mView.findViewById(R.id.Uid_Usuario_Item);
        Fecha_hora_registro_Item = mView.findViewById(R.id.Fecha_hora_registro_Item);
        Titulo_Item = mView.findViewById(R.id.Titulo_Item);
        Descripcion_Item = mView.findViewById(R.id.Descripcion_Item);
        Fecha_Item = mView.findViewById(R.id.Fecha_Item);

        //seteamos dentro del item
        Id_actividad_Item.setText(id_actividad);
        Uid_Usuario_Item.setText(uid_usuario);
        Fecha_hora_registro_Item.setText(fecha_hora_registro);
        Titulo_Item.setText(titulo);
        Descripcion_Item.setText(descripcion);
        Fecha_Item.setText(fecha_actividad);
    }

}
