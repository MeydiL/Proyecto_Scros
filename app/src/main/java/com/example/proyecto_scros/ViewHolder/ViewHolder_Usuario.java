package com.example.proyecto_scros.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_scros.R;

public class ViewHolder_Usuario extends RecyclerView.ViewHolder {

    public Button btnAgregar;
    View mView;
    //Button btnAgregar;

    private ViewHolder_Usuario.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder_Usuario.ClickListener clickListener){

        mClickListener = clickListener;
    }

    public ViewHolder_Usuario(@NonNull View itemView) { //Constructor
        super(itemView);
        mView = itemView;
        btnAgregar= itemView.findViewById(R.id.btnAgregarAmigo);

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

    public void setearDatosUsuario(Context context, String usuario, String correo){

        //Declarar vistas
        TextView usuarioAmigo, correoAmigo;

        //Conectar con el item
        usuarioAmigo = mView.findViewById(R.id.tvUsuario);
        correoAmigo = mView.findViewById(R.id.tvCorreo);

        //seteamos en el item
        usuarioAmigo.setText(usuario);
        correoAmigo.setText(correo);
    }
}
