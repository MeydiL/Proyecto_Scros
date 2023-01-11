package com.example.proyecto_scros;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto_scros.AgregarAmigo.Agregar_Amigo;
import com.example.proyecto_scros.Objetos.Amigo;
import com.example.proyecto_scros.ViewHolder.ViewHolder_Amigos;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class AmigosFragment extends Fragment {

    RecyclerView rvAmigo;

    FloatingActionButton fab;
    String uid_usuario, correo;

    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter<Amigo, ViewHolder_Amigos> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Amigo> options;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference amigos;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    TextView agregarAmigos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_amigos, container, false);

        fab = v.findViewById(R.id.btnFab);
        agregarAmigos= v.findViewById(R.id.txtAgregaramigos);

        //RecyclerView Agregar amigo
        rvAmigo= v.findViewById(R.id.rvAmigo);
        rvAmigo.setHasFixedSize(true);

        //Inicializar usuarios (amigos)
        firebaseDatabase = FirebaseDatabase.getInstance();
        amigos = firebaseDatabase.getReference("Amigos");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Agregar_Amigo.class);
                intent.putExtra("Uid", uid_usuario);
                intent.putExtra("Correo", correo);
                startActivity(intent);
            }
        });

        /*------------Metodos---------------*/
        listarAmigos();
        return v;
    }

    private void listarAmigos(){
        Query query = amigos.orderByChild("uid_usuario").equalTo(user.getUid());
        options = new FirebaseRecyclerOptions.Builder<Amigo>().setQuery(query, Amigo.class).build();
        firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Amigo, ViewHolder_Amigos>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Amigos viewHolder_amigos, int position, @NonNull Amigo amigo) {
                viewHolder_amigos.setearAmigo(
                        getActivity(),
                        amigo.getUsuario_amigo(),
                        amigo.getCorreo_amigo(),
                        amigo.getNombre_amigo(),
                        amigo.getApePat_amigo(),
                        amigo.getApeMat_amigo()
                );

                mostrarNoHayAgregados();
                
            }

            @NonNull
            @Override
            public ViewHolder_Amigos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_amigos,parent, false);
                ViewHolder_Amigos viewHolder_amigos = new ViewHolder_Amigos(view);
                viewHolder_amigos.setOnClickListener(new ViewHolder_Amigos.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getActivity(), "Mantenga pulsado para eliminar amigo", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        String uid = getItem(position).getUid_amigo();
                        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

                        dialog.setMessage("¿Desea eliminar este amigo?");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                eliminarAmigo(uid);
                                Toast.makeText(getActivity(), "Amigo borrado", Toast.LENGTH_SHORT).show();
                            }
                        });

                        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getActivity(), "Amigo NO BORRADO", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.show();
                    }
                });
                return viewHolder_amigos;
            }
        };
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);//que se enliste desde el ultimo al primero
        linearLayoutManager.setStackFromEnd(true);

        rvAmigo.setLayoutManager(linearLayoutManager);
        rvAmigo.setAdapter(firebaseRecyclerAdapter);
    }

    public void eliminarAmigo(String uid_amigo){
        Query query = amigos.orderByChild("uid_amigo").equalTo(uid_amigo);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //lo usamos para recorrer en la bd todas los amigos creadas por el user
                for(DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().removeValue();
                }
                Toast.makeText(getContext(), "Amigo eliminado.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarNoHayAgregados(){
        Query query = amigos.orderByChild("uid_usuario").equalTo(user.getUid()).limitToFirst(1); //Esta consulta tiene el limite que si encuentra solo un resultado se detiene
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){                                                            //Si el resulado de la consulta existe, o sea es verdadero
                    agregarAmigos.setVisibility(View.GONE);

                }else{
                    agregarAmigos.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(firebaseRecyclerAdapter!= null){
            firebaseRecyclerAdapter.startListening();
        }
    }
}