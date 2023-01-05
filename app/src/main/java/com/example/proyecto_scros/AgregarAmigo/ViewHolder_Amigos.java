package com.example.proyecto_scros.AgregarAmigo;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_scros.R;
import com.example.proyecto_scros.ViewHolder.ViewHolder_Proyecto;

public class ViewHolder_Amigos extends RecyclerView.ViewHolder {


    View mView;
    Button btnAgregar;

    private ViewHolder_Amigos.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder_Amigos.ClickListener clickListener){

        mClickListener = clickListener;
    }

    public ViewHolder_Amigos(@NonNull View itemView) { //Constructor
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


    public void setearDatos(Context context,String usuario, String correo){

        //Declarar vistas
        TextView usuarioAmigo, correoAmigo;

        //Conectar con el item
        usuarioAmigo = mView.findViewById(R.id.tvUsuario);
        correoAmigo = mView.findViewById(R.id.tvCorreo);

        //seteamos en el item
        usuarioAmigo.setText(usuario);
        correoAmigo.setText(correo);
    }

    public void setearAmigo(Context context, String usuario, String correo, String nombre, String apePat, String apeMat){

        TextView usuarioAmigo, correoAmigo, nombreAmigo, apePatAmigo, apeMatAmigo;

        usuarioAmigo =mView.findViewById(R.id.tvUsuario);
        correoAmigo =mView.findViewById(R.id.tvCorreo);
        nombreAmigo =mView.findViewById(R.id.tvNombre);
        apePatAmigo =mView.findViewById(R.id.tvApePat);
        apeMatAmigo =mView.findViewById(R.id.tvApeMat);

        usuarioAmigo.setText(usuario);
        correoAmigo.setText(correo);
        nombreAmigo.setText(nombre);
        apePatAmigo.setText(apePat);
        apeMatAmigo.setText(apeMat);
    }
}
