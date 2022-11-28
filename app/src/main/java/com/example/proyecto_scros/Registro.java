package com.example.proyecto_scros;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registro extends AppCompatActivity {
    //Crear objeto de la referencia de la base de datos para acceder a Firebase
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://scros-bce88-default-rtdb.firebaseio.com/");
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    EditText usuarioEt, correoEt, nombreEt, apellidoPaternoEt, apellidoMaternoEt, contraseñaEt, confirmContraseñaEt;
    Button btnRegistrar;
    TextView txtIniciar;
    String usuario = " ", correo = " ", nombre= " ", apellidoPat=" ", apellidoMat=" ", contraseña="", confirmarConstraseña="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        usuarioEt =findViewById(R.id.usuario);
        correoEt =findViewById(R.id.correo);
        nombreEt = findViewById(R.id.nombre);
        apellidoPaternoEt = findViewById(R.id.apellidoP);
        apellidoMaternoEt = findViewById(R.id.apellidoM);
        contraseñaEt = findViewById(R.id.contrasena);
        confirmContraseñaEt = findViewById(R.id.confirmContra);

        btnRegistrar = findViewById(R.id.btnregistrar);
        txtIniciar = findViewById(R.id.txtLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Registro.this);
        progressDialog.setTitle("Espere por favor.");
        progressDialog.setCanceledOnTouchOutside(false);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Obtenemos los datos de registro
                usuario = usuarioEt.getText().toString().trim();
                correo = correoEt.getText().toString().trim();
                nombre = nombreEt.getText().toString().trim();
                apellidoPat = apellidoPaternoEt.getText().toString().trim();
                apellidoMat = apellidoMaternoEt.getText().toString().trim();
                contraseña = contraseñaEt.getText().toString().trim();
                confirmarConstraseña = confirmContraseñaEt.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                    Toast.makeText(Registro.this, "Ingrese correo.", Toast.LENGTH_SHORT).show();
                }
                //Verificamos que todos los campos esten llenos TextUtils.isEmpty
                else if(usuario.isEmpty() || correo.isEmpty() || nombre.isEmpty() || apellidoPat.isEmpty() || apellidoMat.isEmpty() ||contraseña.isEmpty() || confirmarConstraseña.isEmpty()){
                    Toast.makeText(Registro.this, "Error, falta datos por llenar.", Toast.LENGTH_SHORT).show();
                }
                //Verificar la confirmacion de contraseñas.
                else if (!contraseña.equals(confirmarConstraseña)){
                    Toast.makeText(Registro.this, "Error, las contraseñas no son iguales.", Toast.LENGTH_SHORT).show();
                }
                else{
                    CrearCuenta();

                }
            }
        });

        txtIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    public void CrearCuenta(){
        progressDialog.setMessage("Creando su cuenta.");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        GuardarInfo();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Registro.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void GuardarInfo() {
        progressDialog.setMessage("Guardando Info.");
        progressDialog.dismiss();

        String uid = firebaseAuth.getUid();

        HashMap<String, String> Datos = new HashMap<>();

        Datos.put("contraseña", contraseña);
        Datos.put("apellidoMat", apellidoMat);
        Datos.put("apellidoPat", apellidoPat);
        Datos.put("nombre", nombre);
        Datos.put("correo", correo);
        Datos.put("usuario", usuario);
        Datos.put("uid", uid);

        databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.child(uid)
                .setValue(Datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        //Abrimos la pantalla de menu principal
                        startActivity(new Intent(Registro.this, Login.class));
                        Toast.makeText(Registro.this, "Usuario registrado correctamente.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registro.this, " "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}