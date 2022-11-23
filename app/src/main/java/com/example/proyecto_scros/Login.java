package com.example.proyecto_scros;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://scros-bce88-default-rtdb.firebaseio.com/");

    EditText correoUsuario, contraUsuario;
    Button btnLogin;
    TextView registrarAhora;

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    //Validar los datos
    String correo="", contrasena="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        correoUsuario = findViewById(R.id.usuario);
        contraUsuario = findViewById(R.id.contrasena);
        btnLogin= findViewById(R.id.btnlogin);
        registrarAhora =findViewById(R.id.txtRegistro);

        firebaseAuth =FirebaseAuth.getInstance(); //Inicializar FIREBASE AUTHENTICATION

        progressDialog = new ProgressDialog(Login.this); //Inicializar instancia de progressdialog
        progressDialog.setTitle("Espere por favor.");
        progressDialog.setCanceledOnTouchOutside(false);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                correo = correoUsuario.getText().toString().trim();
                contrasena = contraUsuario.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                    Toast.makeText(Login.this, "Error, correo inválido.", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(contrasena)){
                    Toast.makeText(Login.this, "Por favor, ingrese su contraseña.", Toast.LENGTH_SHORT).show();
                }
                else{
                    LoginDeUsuario();

                }
            }
        });

        registrarAhora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Abre la actividad de registro
                startActivity(new Intent(Login.this, Registro.class));
            }
        });

    }

    private void LoginDeUsuario(){
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(correo,contrasena)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //verificar si la tarea se completo exitosamente
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            //Abrimos la pantalla de menu principal
                            startActivity(new Intent(Login.this, MainActivity.class));

                            Toast.makeText(Login.this, "Bienvenido(a): "+user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "Error, verifique si los datos son correctos.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
        });
    }
}