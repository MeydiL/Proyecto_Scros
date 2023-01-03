package com.example.proyecto_scros.AgregarAmigo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.proyecto_scros.AmigosFragment;
import com.example.proyecto_scros.Login;
import com.example.proyecto_scros.Objetos.Proyecto;
import com.example.proyecto_scros.R;
import com.example.proyecto_scros.Registro;
import com.example.proyecto_scros.ViewHolder.ViewHolder_Proyecto;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Agregar_Amigo extends AppCompatActivity {

    RecyclerView rvagregarAmigo;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference db_firebase, amigos, usuarios;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;


    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter<Amigo, ViewHolder_Amigos> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Amigo> options;

    Button btnAgregar;

    String uid_usuario, correo_usuario, uid_amigo, usuario_amigo, correo_amigo, nombre_amigo, apePat_amigo, apeMat_amigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_amigo);

        //RecyclerView Agregar amigo
        rvagregarAmigo= findViewById(R.id.rvagregarAmigo);
        rvagregarAmigo.setHasFixedSize(true);

        //Inicializar usuarios (amigos)
        firebaseDatabase = FirebaseDatabase.getInstance();
        usuarios = firebaseDatabase.getReference("Usuarios");

        //Inicializar autenticacion de usuario
        firebaseAuth= FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        //inicializamos referencia hacia la base de datos
        db_firebase = FirebaseDatabase.getInstance().getReference();
        amigos= FirebaseDatabase.getInstance().getReference().child("Amigos");// HACE REFERENCIA DIRECTAMENTE CON EL HIJO LLAMADO AMIGO



        btnAgregar = findViewById(R.id.btnAgregarAmigo);

        /*------------Metodos---------------*/
        listarAmigos();

    }


    private void listarAmigos(){
        options = new FirebaseRecyclerOptions.Builder<Amigo>().setQuery(usuarios, Amigo.class).build();
        firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Amigo, ViewHolder_Amigos>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Amigos viewHolder_amigos, int position, @NonNull Amigo amigo) {
                viewHolder_amigos.setearDatos(
                        Agregar_Amigo.this,
                        amigo.getUsuario(),
                        amigo.getCorreo()
                );
                /*COMPROBAR SI EXISTE EL USUARRIO EN LA TABLA AMIGO*/
                amigos.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        for(DataSnapshot snapshot : datasnapshot.getChildren()){                //CON EL FOR EVALUA TODOS LOS HIJOS DE LA RAMA AMIGO
                            String ma = snapshot.child("uid_amigo").getValue().toString();      // BUSCA EL ATRIBUTO UID_AMIGO  DE LOS AMIGOS
                            System.out.println("UID AMIGO: " +ma);
                            if(ma.equals(amigo.getUid())){                                      // SI LA UID_AMIGO OBTENIDA ES IGUAL A LA UID DEL RECYCLERVIEW
                                viewHolder_amigos.btnAgregar.setVisibility(View.GONE);          //SIGNIFICA QUE ESE USUARIO YA ESTA EN LA LISTA DE AMIGOS
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                /* Agregar accion del boton agregar amigo*/
                viewHolder_amigos.btnAgregar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uid_usuario = user.getUid();
                        correo_usuario = user.getEmail();
                        uid_amigo =amigo.getUid();
                        usuario_amigo= amigo.getUsuario();
                        correo_amigo = amigo.getCorreo();
                        nombre_amigo = amigo.getNombre();
                        apePat_amigo = amigo.getApellidoPat();
                        apeMat_amigo = amigo.getApellidoMat();

                        agregarAmigo();

                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder_Amigos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addamigos,parent, false);
                ViewHolder_Amigos viewHolder_amigos = new ViewHolder_Amigos(view);
                viewHolder_amigos.setOnClickListener(new ViewHolder_Amigos.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(Agregar_Amigo.this, "on item click", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(Agregar_Amigo.this, "on item long click", Toast.LENGTH_SHORT).show();

                    }
                });
                return viewHolder_amigos;
            }
        };
        linearLayoutManager = new LinearLayoutManager(Agregar_Amigo.this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);//que se enliste desde el ultimo al primero
        linearLayoutManager.setStackFromEnd(true);

        rvagregarAmigo.setLayoutManager(linearLayoutManager);
        rvagregarAmigo.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseRecyclerAdapter!= null){
            firebaseRecyclerAdapter.startListening();
        }
    }

    private void agregarAmigo(){

        String Amigo_usuario = db_firebase.push().getKey();

        HashMap<String, String> Datos = new HashMap<>();

        Datos.put("uid_usuario",uid_usuario);
        Datos.put("correo_usuario",correo_usuario);
        Datos.put("uid_amigo", uid_amigo);
        Datos.put("usuario_amigo",usuario_amigo);
        Datos.put("correo_amigo",correo_amigo);
        Datos.put("nombre_amigo",nombre_amigo);
        Datos.put("apePat_amigo",apePat_amigo);
        Datos.put("apeMat_amigo",apeMat_amigo);

        db_firebase = FirebaseDatabase.getInstance().getReference("Amigos");
        db_firebase.child(Amigo_usuario)
                .setValue(Datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(Agregar_Amigo.this, "Amigo agregado correctamente.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Agregar_Amigo.this, "Error al agregar amigo. ", Toast.LENGTH_SHORT).show();
            }
        });
    }

}