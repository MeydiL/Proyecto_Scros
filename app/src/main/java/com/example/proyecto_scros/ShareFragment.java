package com.example.proyecto_scros;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.proyecto_scros.AgregarProyecto.Agregar_Proyecto;
import com.example.proyecto_scros.Objetos.Proyecto;
import com.example.proyecto_scros.ViewHolder.ViewHolder_Proyecto;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShareFragment extends Fragment {

    RecyclerView recyclerviewProyectos;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BASE_DE_DATOS;

    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter<Proyecto, ViewHolder_Proyecto> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Proyecto> options;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference Usuarios;


    FloatingActionButton fab;
    String uid_usuario, correo;

    Button btn_crear_proyecto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_share, container, false);

        recyclerviewProyectos = v.findViewById(R.id.recycleviewProyectos);
        //fab = v.findViewById(R.id.fab);

        recyclerviewProyectos.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        BASE_DE_DATOS = firebaseDatabase.getReference("Proyectos");

       // ListarProyectosUsuarios();//AQUI VA ORIGINALNEBTE

        firebaseAuth = FirebaseAuth.getInstance();                                  //instanciamos de la cuenta de autenticacion
        user = firebaseAuth.getCurrentUser();
        Usuarios= FirebaseDatabase.getInstance().getReference("Usuarios");
        btn_crear_proyecto = v.findViewById(R.id.Btn_Crear_Proyecto);
        IniciarVariables();
        cargaDatos();
        ListarProyectosUsuarios();



        btn_crear_proyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Agregar_Proyecto.class);
                intent.putExtra("Uid", uid_usuario);
                intent.putExtra("Correo", correo);
                startActivity(intent);
            }
        });
        return v;
    }

    private void IniciarVariables(){
        recyclerviewProyectos.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        BASE_DE_DATOS = firebaseDatabase.getReference("Proyectos");
        //ListarProyectosUsuarios();//AQUI VA ORIGINALNEBTE
//v.
        firebaseAuth = FirebaseAuth.getInstance();                                  //instanciamos de la cuenta de autenticacion
        user = firebaseAuth.getCurrentUser();
        Usuarios= FirebaseDatabase.getInstance().getReference("Usuarios");
    }

    private void cargaDatos(){
        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {         //Del los datos almacenados en Usuario buscara la uid del usuario actual para obtener sus datos
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {                         //Funcion datasnapshop obtienes los hijo
                //si el usuario existe
                if(snapshot.exists()){                                                          //SI USAURIO EXISTE EN LA DB
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

    private void ListarProyectosUsuarios(){
        options = new FirebaseRecyclerOptions.Builder<Proyecto>().setQuery(BASE_DE_DATOS, Proyecto.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Proyecto, ViewHolder_Proyecto>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Proyecto viewHolder_proyecto, int position, @NonNull Proyecto proyecto) {
                viewHolder_proyecto.SetearDatos(
                        getActivity(),
                        proyecto.getId_proyecto(),
                        proyecto.getUid_usuario(),
                        proyecto.getCorreo_usuario(),
                        proyecto.getFecha_hora_actual(),
                        proyecto.getTitulo(),
                        proyecto.getDescripcion(),
                        proyecto.getFecha_proyecto(),
                        proyecto.getEstado()
                );
            }

            @Override
            public ViewHolder_Proyecto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_proyecto,parent, false);
                ViewHolder_Proyecto viewHolder_proyecto = new ViewHolder_Proyecto(view);
                viewHolder_proyecto.setOnClickListener(new ViewHolder_Proyecto.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getActivity(), "on item click", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(getActivity(), "on item long click", Toast.LENGTH_SHORT).show();
                    }
                });
                return viewHolder_proyecto;
            }
        };

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);//que se enliste desde el ultimo al primero
        linearLayoutManager.setStackFromEnd(true);

        recyclerviewProyectos.setLayoutManager(linearLayoutManager);
        recyclerviewProyectos.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(firebaseRecyclerAdapter!= null){
            firebaseRecyclerAdapter.startListening();
        }
    }
}