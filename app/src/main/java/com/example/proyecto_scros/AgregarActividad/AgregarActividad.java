package com.example.proyecto_scros.AgregarActividad;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto_scros.Objetos.Actividad;
import com.example.proyecto_scros.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AgregarActividad extends AppCompatActivity {

    TextView Uid_Usuario, Correo_usuario, Fecha_hora_actual,Fecha, Estado;
    EditText Titulo, Descripcion;
    Button Btn_Calendario, Btn_Crear_Actividad;

    int dia, mes, anio;

    DatabaseReference BD_Firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_actividad);
        /*ActionBar actionBar = getSupportActionBar();//1P creamos la flecha
        actionBar.setTitle("Crear Actividad");
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);//1P*/

        IniciarVariables();
        ObtenerDatos();
        Obtener_Fecha_Hora_Actual();

        Btn_Calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendario = Calendar.getInstance();

                dia = calendario.get(Calendar.DAY_OF_MONTH);
                mes = calendario.get(Calendar.MONTH);
                anio = calendario.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AgregarActividad.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int AnioSelecionado, int MesSeleccionado, int DiaSeleccionado) {

                        String diaFormateado, mesFormateado;

                        //Obtener dia
                        if(DiaSeleccionado < 10){
                            diaFormateado = "0"+ DiaSeleccionado;
                        }else{
                            diaFormateado = String.valueOf(DiaSeleccionado);
                        }

                        //mes
                        int Mes = MesSeleccionado + 1;
                        if(Mes < 10){
                            mesFormateado = "0"+String.valueOf(Mes);
                        }else{
                            mesFormateado = String.valueOf(Mes);
                        }

                        Fecha.setText(diaFormateado + "/" + mesFormateado + "/" + AnioSelecionado);
                    }
                }
                        ,anio, mes, dia);
                datePickerDialog.show();
            }
        });

        Btn_Crear_Actividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AgregarBD_Actividad();
            }
        });
    }

    private void IniciarVariables(){
        Uid_Usuario = findViewById(R.id.Uid_Usuario);
        Correo_usuario = findViewById(R.id.Correo_usuario);
        Fecha_hora_actual = findViewById(R.id.Fecha_hora_actual);
        Fecha = findViewById(R.id.Fecha);
        Estado = findViewById(R.id.Estado);

        Titulo = findViewById(R.id.Titulo);
        Descripcion = findViewById(R.id.Descripcion);

        Btn_Calendario = findViewById(R.id.Btn_Calendario);
        Btn_Crear_Actividad = findViewById(R.id.Btn_Crear_Actividad);

        BD_Firebase = FirebaseDatabase.getInstance().getReference();
    }

    private void ObtenerDatos(){
        String uid_Recuperado = getIntent().getStringExtra("Uid");
        String correo_Recuperado = getIntent().getStringExtra("Correo");

        Uid_Usuario.setText(uid_Recuperado);
        Correo_usuario.setText(correo_Recuperado);
    }

    private void Obtener_Fecha_Hora_Actual(){
        String fecha_hora_registro = new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss a",
                Locale.getDefault()).format(System.currentTimeMillis());

        Fecha_hora_actual.setText(fecha_hora_registro);
    }

    private void AgregarBD_Actividad(){
        String uid_Usuario = Uid_Usuario.getText().toString();
        String correo_usuario = Correo_usuario.getText().toString();
        String fecha_hora_actual = Fecha_hora_actual.getText().toString();
        String titulo = Titulo.getText().toString();
        String descripcion = Descripcion.getText().toString();
        String fecha = Fecha.getText().toString();
        String estado = Estado.getText().toString();
        String uid_Proyecto = getIntent().getStringExtra("uid_Proyecto");

        if(!uid_Usuario.equals("") || !correo_usuario.equals("") || fecha_hora_actual.equals("") ||
                titulo.equals("") || descripcion.equals("") || fecha.equals("") || estado.equals("")){

            Actividad actividad = new Actividad(
                    correo_usuario+"/"+fecha_hora_actual,
                    uid_Usuario,
                    uid_Proyecto,
                    titulo,
                    descripcion,
                    fecha_hora_actual,
                    fecha,
                    estado
            );

            String Actividad_usuario = BD_Firebase.push().getKey();
            //establecer name db
            String Nombre_BD = "Actividades";

            BD_Firebase.child(Nombre_BD).child(Actividad_usuario).setValue(actividad);

            Toast.makeText(this, "La actividad fue creado.", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }else{
            Toast.makeText(this, "Llenar todos los campos.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}