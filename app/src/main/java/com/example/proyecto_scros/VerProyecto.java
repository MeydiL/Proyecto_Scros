package com.example.proyecto_scros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto_scros.AgregarActividad.AgregarActividad;
import com.example.proyecto_scros.Objetos.Actividad;
import com.example.proyecto_scros.ViewHolder.ViewHolder_Actividad;
import com.example.proyecto_scros.ViewHolder.ViewHolder_Proyecto;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class VerProyecto extends AppCompatActivity {

    String tituloProyecto, uid_proyecto, correo;
    TextView titulo;
    Button agregarActividad, activarProyecto;

    RecyclerView recyclerviewActividades;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BASE_DE_DATOS;

    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter<Actividad, ViewHolder_Actividad> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Actividad> options;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    DatabaseReference Usuarios;

    Dialog dialog;
    String uid_usuario;

    Button btn_crear_actividad;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_proyecto);

        titulo = findViewById(R.id.Titulo);
        recyclerviewActividades = findViewById(R.id.recyclerviewActividades);

        //recyclerviewActividades.setHasFixedSize(true);
        //firebaseDatabase = FirebaseDatabase.getInstance();
        //BASE_DE_DATOS = firebaseDatabase.getReference("Actividades");
        //ListarActividadesProyecto();//AQUI VA ORIGINALNEBTE
        //firebaseAuth = FirebaseAuth.getInstance();//instanciamos de la cuenta de autenticacion
        //user = firebaseAuth.getCurrentUser();
        //Usuarios= FirebaseDatabase.getInstance().getReference("Usuarios");

        dialog = new Dialog(VerProyecto.this);
        btn_crear_actividad = findViewById(R.id.Btn_Agregar_Actividad);

        IniciarVariables();
        cargaDatos();
        ListarActividadesProyecto();

        btn_crear_actividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerProyecto.this, AgregarActividad.class);
                intent.putExtra("Uid", uid_usuario);
                intent.putExtra("Correo", correo);
                intent.putExtra("uid_Proyecto", uid_proyecto);
                startActivity(intent);
            }
        });

        activarProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(VerProyecto.this, "Activar Proyecto", Toast.LENGTH_SHORT).show();
                ActivarProyecto();
            }
        });
    }

    private void IniciarVariables(){

        tituloProyecto = getIntent().getStringExtra("tituloProyecto");
        titulo.setText(tituloProyecto);

        uid_proyecto = getIntent().getStringExtra("uid");

        recyclerviewActividades.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        BASE_DE_DATOS = firebaseDatabase.getReference("Actividades");
        //ListarActividadesProyecto();//AQUI VA ORIGINALNEBTE
//v.
        firebaseAuth = FirebaseAuth.getInstance();                                  //instanciamos de la cuenta de autenticacion
        user = firebaseAuth.getCurrentUser();
        Usuarios= FirebaseDatabase.getInstance().getReference("Usuarios");
        activarProyecto = findViewById(R.id.Btn_Activar_Proyecto);
    }

    private void cargaDatos(){
        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {    //Del los datos almacenados en Usuario buscara la uid del usuario actual para obtener sus datos
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {                    //Funcion datasnapshop obtienes los hijo
                //si el usuario existe
                if(snapshot.exists()){                                                    //SI USAURIO EXISTE EN LA DB
                    //obtener los datos
                    uid_usuario= snapshot.child("uid").getValue().toString();
                    correo=snapshot.child("correo").getValue().toString();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ActivarProyecto(){
        DatabaseReference proyectos;
        proyectos= FirebaseDatabase.getInstance().getReference("Proyectos");

        proyectos.child(uid_proyecto).child("selected").setValue(true);

    }

    private void ListarActividadesProyecto(){
        Query query = BASE_DE_DATOS.orderByChild("uid_proyecto").equalTo(uid_proyecto);
        //aparer todos BASE_DE_DATOS o solo los del usuario query
        options = new FirebaseRecyclerOptions.Builder<Actividad>().setQuery(query, Actividad.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Actividad, ViewHolder_Actividad>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Actividad viewHolder_actividad, int position, @NonNull Actividad actividad) {
                viewHolder_actividad.SetearDatos(
                        VerProyecto.this,
                        actividad.getId_actividad(),
                        actividad.getUid_usuario(),
                        actividad.getTitulo(),
                        actividad.getDescripcion(),
                        actividad.getFecha_actividad(),
                        actividad.getFecha()
                );
            }

            @Override
            public ViewHolder_Actividad onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividad,parent, false);
                ViewHolder_Actividad viewHolder_actividad = new ViewHolder_Actividad(view);
                viewHolder_actividad.setOnClickListener(new ViewHolder_Actividad.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                       /* String tituloP = getItem(position).getTitulo();
                        Intent intent = new Intent(VerProyecto.this, VerProyecto.class);
                        intent.putExtra("tituloP",tituloP);
                        startActivity(intent);*/
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        //Toast.makeText(getActivity(), "on item long click", Toast.LENGTH_SHORT).show();
                        String id_actividad = getItem(position).getId_actividad();

                        //declarar vistar
                        Button CD_Eliminar, CD_Actualizar;

                        dialog.setContentView(R.layout.dialogo_opciones);
                        CD_Eliminar = dialog.findViewById(R.id.CD_Eliminar);
                        CD_Actualizar = dialog.findViewById(R.id.CD_Actualizar);

                        CD_Eliminar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                EliminarProyecto(id_actividad);
                                dialog.dismiss();
                                //Toast.makeText(getContext(), "Eliminar proyecto", Toast.LENGTH_SHORT).show();
                            }
                        });

                        CD_Actualizar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Toast.makeText(VerProyecto.this, "Actualizar proyecto", Toast.LENGTH_SHORT).show();
                            }
                        });

                        dialog.show();
                    }

                });
                return viewHolder_actividad;
            }
        };

        linearLayoutManager = new LinearLayoutManager(VerProyecto.this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(false);
        // setReverseLayout() - TRUE - que se enliste desde el ultimo al primero/FALSE del primero al ultimo
        linearLayoutManager.setStackFromEnd(true);
        // setStackFromEnd()  - TRUE- se atasca en el final / FALSE - se atasca al principio

        recyclerviewActividades.setLayoutManager(linearLayoutManager);
        recyclerviewActividades.setAdapter(firebaseRecyclerAdapter);
    }

    private void EliminarProyecto(String id_proyecto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(VerProyecto.this);
        builder.setTitle("Eliminar actividad");
        builder.setMessage("¿Desea eliminar esta actividad del proyecto?");
        builder.setPositiveButton("Si, seguro.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //ELIMINAR ONOTA DB
                Query query = BASE_DE_DATOS.orderByChild("id_proyecto").equalTo(id_proyecto);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //lo usamos para recorrer en la bd todas las notas creadas por el user
                        for(DataSnapshot ds : snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(VerProyecto.this, "Actividad eliminada.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(VerProyecto.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(VerProyecto.this, "No se ha eliminado el proyecto.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(firebaseRecyclerAdapter!= null){
            firebaseRecyclerAdapter.startListening();
        }
    }
}