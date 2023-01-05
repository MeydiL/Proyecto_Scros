package com.example.proyecto_scros;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;


public class CuentaFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference Usuarios;

    TextView perfilNombre,perfilApellidoP,perfilApellidoM, perfilUsuario, perfilContra;
    TextView tituloCorreo, tituloUsuario;
    String usuario="",nombre="",apePat="",apeMat="",correo="", contraseña="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_cuenta, container, false);

        perfilNombre= v.findViewById(R.id.perfilNombre);
        perfilApellidoP= v.findViewById(R.id.perfilApellidoP);
        perfilApellidoM= v.findViewById(R.id.perfilApellidoM);
        perfilUsuario= v.findViewById(R.id.perfilUsuario);
        perfilContra= v.findViewById(R.id.perfilContra);
        tituloCorreo=v.findViewById(R.id.tituloCorreo);
        tituloUsuario=v.findViewById(R.id.tituloUsuario);

        firebaseAuth = FirebaseAuth.getInstance();                                  //instanciamos de la cuenta de autenticacion
        user = firebaseAuth.getCurrentUser();                                       //obtener el usuario actual
        Usuarios= FirebaseDatabase.getInstance().getReference("Usuarios");      //Obtenemos referencia de los datos de usuarios
        cargaDatos();

        return v;
    }

    private void cargaDatos(){
        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {         //Del los datos almacenados en Usuario buscara la uid del usuario actual para obtener sus datos
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {                         //Funcion datasnapshop obtienes los hijo
                //si el usuario existe
                if(snapshot.exists()){                                                          //SI USAURIO EXISTE EN LA DB
                    //obtener los datos
                    usuario= snapshot.child("usuario").getValue().toString();                   // Le asignamos a la variable string el valor del atributo usuario
                    nombre=snapshot.child("nombre").getValue().toString();                      //getvalue().tostring() funciona para
                    apePat=snapshot.child("apellidoPat").getValue().toString();                 //convertir el valor de snapshop en string
                    apeMat=snapshot.child("apellidoMat").getValue().toString();
                    correo=snapshot.child("correo").getValue().toString();
                    contraseña=snapshot.child("contraseña").getValue().toString();
                    //enviar los daos a sus respectivos textView
                    perfilNombre.setText(nombre);
                    perfilApellidoP.setText(apePat);
                    perfilApellidoM.setText(apeMat);
                    perfilUsuario.setText(usuario);
                    perfilContra.setText(contraseña);
                    tituloCorreo.setText(correo);
                    tituloUsuario.setText(usuario);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


}