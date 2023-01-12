package com.example.proyecto_scros;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto_scros.Objetos.Actividad;
import com.example.proyecto_scros.ViewHolder.ViewHolder_Actividad;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class HomeFragment extends Fragment {

    Button buttonAnterior, buttonSiguiente;
    TextView tituloProyecto, nombreDia;

    int numDia = 0;
    Calendar calendar = Calendar.getInstance();
    String mesF;
    int day = calendar.get(Calendar.DAY_OF_WEEK);
    int dia = calendar.get(Calendar.DAY_OF_MONTH);
    int mes = calendar.get(Calendar.MONTH);
    int anio = calendar.get(Calendar.YEAR);

    int contDias =0;
    String fechaActual;
    String proyectoActivoUID;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference BASE_DE_DATOS;
    DatabaseReference BASE_DE_DATOSLAP;
    FirebaseRecyclerAdapter<Actividad, ViewHolder_Actividad> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Actividad> options;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    String[] dias = new String[8];
    RecyclerView recyclerviewActividades;
    LinearLayoutManager linearLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_home, container, false);

        InicializarVariables(v);


        buttonAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoverDia(-1);
                PRU(proyectoActivoUID);
            }
        });

        buttonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoverDia(1);
                PRU(proyectoActivoUID);
             //   ListarActividadesProyecto(proyectoActivoUID);
            }
        });
        return v;
    }

    private void MoverDia(int mov) {
        numDia += mov;//0
        contDias += mov;//4

        if(numDia > 7){
            contDias = (dia - numDia) * -1;
            numDia = 1;
            CambiarDia(dias[numDia]);
        }else if(numDia < 1){
            contDias = (contDias*-1)-1;
            numDia = 7;
            CambiarDia(dias[numDia]);
        }else{
            CambiarDia(dias[numDia]);
        }

        if(mes < 10){
            mesF = "0"+(mes+1);
        }else{
            mesF = String.valueOf(mes+1);
        }
        fechaActual = (dia + contDias) + "/" + mesF +"/"+anio;
        Toast.makeText(getActivity(), fechaActual + "", Toast.LENGTH_SHORT).show();
        //Toast.makeText(getActivity(), numDia + "", Toast.LENGTH_SHORT).show();
    }

    private void RevisarProyectoActivo() {
        Query query = BASE_DE_DATOS.orderByChild("uid_usuario").equalTo(user.getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()){
                    //Toast.makeText(getActivity(), ds.child("selected").getValue().toString(), Toast.LENGTH_SHORT).show();
                    if(ds.child("selected").getValue().equals(true)){
                        //Toast.makeText(getActivity(), ds.child("titulo").getValue().toString(), Toast.LENGTH_SHORT).show();
                        tituloProyecto.setText(ds.child("titulo").getValue().toString());
                        proyectoActivoUID = ds.child("uid").getValue().toString();
                        PRU(ds.child("uid").getValue().toString());
                        //    ListarActividadesProyecto(ds.child("uid").getValue().toString());
                        //Toast.makeText(getActivity(),ds.child("uid").getValue().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CambiarDia(String dia) {
        nombreDia.setText(dia);
    }

    private void ListarActividadesProyecto(String uid_proyecto){

        Query query = BASE_DE_DATOSLAP.orderByChild("uid_proyecto").equalTo(uid_proyecto);
        //Query query = BASE_DE_DATOSLAP.child("uid_proyecto").child(uid_proyecto).orderByChild("fecha").equalTo(fechaActual);
        options = new FirebaseRecyclerOptions.Builder<Actividad>().setQuery(query, Actividad.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Actividad, ViewHolder_Actividad>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Actividad viewHolder_actividad, int position, @NonNull Actividad actividad) {

               // Toast.makeText(getActivity(), actividad.getFecha()+ "-" + fechaActual, Toast.LENGTH_SHORT).show();
                if(actividad.getFecha().equals(fechaActual)){
                   // Toast.makeText(getActivity(), "ENTRO", Toast.LENGTH_SHORT).show();
                    viewHolder_actividad.SetearDatos(
                            getActivity(),
                            actividad.getId_actividad(),
                            actividad.getUid_usuario(),
                            actividad.getTitulo(),
                            actividad.getDescripcion(),
                            actividad.getFecha_actividad(),
                            actividad.getFecha()
                    );
                }else{

                    //Toast.makeText(getActivity(), "AJUERA", Toast.LENGTH_SHORT).show();
                }
            }

            @NonNull
            @Override
            public ViewHolder_Actividad onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividad, parent, false);
                ViewHolder_Actividad viewHolder_actividad = new ViewHolder_Actividad(view);
                viewHolder_actividad.setOnClickListener(new ViewHolder_Actividad.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                return viewHolder_actividad;
            }
        };

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(false);
        // setReverseLayout() - TRUE - que se enliste desde el ultimo al primero/FALSE del primero al ultimo
        linearLayoutManager.setStackFromEnd(false);
        // setStackFromEnd()  - TRUE- se atasca en el final / FALSE - se atasca al principio


        firebaseRecyclerAdapter.startListening();
        recyclerviewActividades.setAdapter(firebaseRecyclerAdapter);
        recyclerviewActividades.setLayoutManager(linearLayoutManager);

    }

    private void PRU(String uid_proyecto){

        Query query = BASE_DE_DATOSLAP.orderByChild("fecha").equalTo(fechaActual);
        //Query query = BASE_DE_DATOSLAP.child("uid_proyecto").child(uid_proyecto).orderByChild("fecha").equalTo(fechaActual);
        options = new FirebaseRecyclerOptions.Builder<Actividad>().setQuery(query, Actividad.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Actividad, ViewHolder_Actividad>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Actividad viewHolder_actividad, int position, @NonNull Actividad actividad) {

                // Toast.makeText(getActivity(), actividad.getFecha()+ "-" + fechaActual, Toast.LENGTH_SHORT).show();
                //if(actividad.getFecha().equals(fechaActual)){
                // Toast.makeText(getActivity(), "ENTRO", Toast.LENGTH_SHORT).show();
                viewHolder_actividad.SetearDatos(
                        getActivity(),
                        actividad.getId_actividad(),
                        actividad.getUid_usuario(),
                        actividad.getTitulo(),
                        actividad.getDescripcion(),
                        actividad.getFecha_actividad(),
                        actividad.getFecha()
                );
                //}else{
                //   viewHolder_actividad.equals(false);
                //Toast.makeText(getActivity(), "AJUERA", Toast.LENGTH_SHORT).show();
                //}
            }

            @NonNull
            @Override
            public ViewHolder_Actividad onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividad, parent, false);
                ViewHolder_Actividad viewHolder_actividad = new ViewHolder_Actividad(view);
                viewHolder_actividad.setOnClickListener(new ViewHolder_Actividad.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
                return viewHolder_actividad;
            }
        };

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(false);
        // setReverseLayout() - TRUE - que se enliste desde el ultimo al primero/FALSE del primero al ultimo
        linearLayoutManager.setStackFromEnd(false);
        // setStackFromEnd()  - TRUE- se atasca en el final / FALSE - se atasca al principio


        firebaseRecyclerAdapter.startListening();
        recyclerviewActividades.setAdapter(firebaseRecyclerAdapter);
        recyclerviewActividades.setLayoutManager(linearLayoutManager);

    }

    private void InicializarVariables(View v) {
        buttonAnterior = v.findViewById(R.id.btn_anterior);
        buttonSiguiente = v.findViewById(R.id.btn_siguiente);
        tituloProyecto = v.findViewById(R.id.txtNombreProyecto);
        nombreDia = v.findViewById(R.id.txt_NombreDia);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        BASE_DE_DATOS = firebaseDatabase.getReference("Proyectos");
        BASE_DE_DATOSLAP = firebaseDatabase.getReference("Actividades");
        recyclerviewActividades = v.findViewById(R.id.recycleviewActividades);
        recyclerviewActividades.setHasFixedSize(true);

        dias[1] = "Domingo";
        dias[2] = "Lunes";
        dias[3] = "Martes";
        dias[4] = "Miercoles";
        dias[5] = "Jueves";
        dias[6] = "Viernes";
        dias[7] = "Sabado";

        ObtenerFechaActual();
        RevisarProyectoActivo();
    }

    private void ObtenerFechaActual() {

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        anio = calendar.get(Calendar.YEAR);


        if(mes < 10){
            mesF = "0"+(mes+1);
        }else{
            mesF = String.valueOf(mes+1);
        }

        fechaActual = dia + "/" + mesF +"/"+anio;

        switch (day) {
            case Calendar.SUNDAY:
                numDia = Calendar.SUNDAY;
                CambiarDia(dias[1]);
                break;
            case Calendar.MONDAY:
                numDia = Calendar.MONDAY;
                CambiarDia(dias[2]);
                break;
            case Calendar.TUESDAY:
                numDia = Calendar.TUESDAY;
                CambiarDia(dias[3]);
                break;
            case Calendar.WEDNESDAY:
                numDia = Calendar.WEDNESDAY;
                CambiarDia(dias[4]);
                break;
            case Calendar.THURSDAY:
                numDia = Calendar.THURSDAY;
                CambiarDia(dias[5]);
                break;
            case Calendar.FRIDAY:
                numDia = Calendar.FRIDAY;
                CambiarDia(dias[6]);
                break;
            case Calendar.SATURDAY:
                numDia = Calendar.SATURDAY;
                CambiarDia(dias[numDia]);
                break;
        }
        Toast.makeText(getActivity(), fechaActual + "", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStart() {
        super.onStart();
        if(firebaseRecyclerAdapter!= null){
            firebaseRecyclerAdapter.startListening();
            System.out.print("S");
        }
    }
}