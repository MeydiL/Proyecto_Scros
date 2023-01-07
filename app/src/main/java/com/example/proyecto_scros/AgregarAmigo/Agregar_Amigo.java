package com.example.proyecto_scros.AgregarAmigo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.proyecto_scros.Objetos.Amigo;
import com.example.proyecto_scros.Objetos.Usuario;
import com.example.proyecto_scros.R;
import com.example.proyecto_scros.ViewHolder.ViewHolder_Usuario;
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
    FirebaseRecyclerAdapter<Usuario, ViewHolder_Usuario> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Usuario> options;

    Button btnAgregar;

    String uid_usuario, correo_usuario, uid_amigo, usuario_amigo, correo_amigo, nombre_amigo, apePat_amigo, apeMat_amigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_amigo);

        //RecyclerView Agregar amigo
        rvagregarAmigo = findViewById(R.id.rvagregarAmigo);
        rvagregarAmigo.setHasFixedSize(true);

        //Inicializar usuarios (amigos)
        firebaseDatabase = FirebaseDatabase.getInstance();
        usuarios = firebaseDatabase.getReference("Usuarios");

        //Inicializar autenticacion de usuario
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        //inicializamos referencia hacia la base de datos
        db_firebase = FirebaseDatabase.getInstance().getReference();
        amigos = FirebaseDatabase.getInstance().getReference().child("Amigos");// HACE REFERENCIA DIRECTAMENTE CON EL HIJO LLAMADO AMIGO


        btnAgregar = findViewById(R.id.btnAgregarAmigo);

        /*------------Metodos---------------*/
        listarAmigos();

    }


    private void listarAmigos() {
        options = new FirebaseRecyclerOptions.Builder<Usuario>().setQuery(usuarios, Usuario.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Usuario, ViewHolder_Usuario>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Usuario viewHolder_usuario, int position, @NonNull Usuario usuario) {
                viewHolder_usuario.setearDatosUsuario(
                        Agregar_Amigo.this,
                        usuario.getUsuario(),
                        usuario.getCorreo()
                );

                //COMPROBAR LOS USUARIOS AGREGADOS PARA INHABILITAR EL BOTON AGREGAR.

                Query query = amigos.orderByChild("uid_usuario").equalTo(user.getUid());            //Consulta los amigos del usuario que inicio sesion
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //lo usamos para recorrer en la bd todas los amigos creadas por el user
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String query1 = ds.child("uid_amigo").getValue().toString();            //Consulta la Udi de los amigos que hay en la base de datos
                            System.out.println("UID AMIGO: " + ds);
                            if (query1.equals(usuario.getUid())) {                                  //Compara si la uid_amigo es igual al que está en el recycleyview
                                viewHolder_usuario.btnAgregar.setVisibility(View.GONE);             //Si la sentencia se cumple ese amigo ya esta agregado y se bloquea el boton de agregar
                                System.out.println("2");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Agregar_Amigo.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


                //Agregar accion del boton agregar amigo
                viewHolder_usuario.btnAgregar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uid_usuario = user.getUid();
                        correo_usuario = user.getEmail();
                        uid_amigo = usuario.getUid();
                        usuario_amigo = usuario.getUsuario();
                        correo_amigo = usuario.getCorreo();
                        nombre_amigo = usuario.getNombre();
                        apePat_amigo = usuario.getApellidoPat();
                        apeMat_amigo = usuario.getApellidoMat();

                        agregarAmigo();

                    }
                });

            }

            @NonNull
            @Override
            public ViewHolder_Usuario onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addamigos, parent, false);
                ViewHolder_Usuario viewHolder_usuario = new ViewHolder_Usuario(view);
                viewHolder_usuario.setOnClickListener(new ViewHolder_Usuario.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(Agregar_Amigo.this, "on item click", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(Agregar_Amigo.this, "on itemlong click", Toast.LENGTH_SHORT).show();
                    }
                });
                return viewHolder_usuario;
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
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }

    private void agregarAmigo() {

        String Amigo_usuario = db_firebase.push().getKey();

        HashMap<String, String> Datos = new HashMap<>();

        Datos.put("uid_usuario", uid_usuario);
        Datos.put("correo_usuario", correo_usuario);
        Datos.put("uid_amigo", uid_amigo);
        Datos.put("usuario_amigo", usuario_amigo);
        Datos.put("correo_amigo", correo_amigo);
        Datos.put("nombre_amigo", nombre_amigo);
        Datos.put("apePat_amigo", apePat_amigo);
        Datos.put("apeMat_amigo", apeMat_amigo);

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