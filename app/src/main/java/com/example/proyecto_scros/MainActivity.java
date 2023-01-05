package com.example.proyecto_scros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference Usuarios;

    TextView headerUsuario, headerCorreo;
    String usuario="",correo="";
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }


        //Inicializar los servicios de firebase
        firebaseAuth= FirebaseAuth.getInstance();
        user= firebaseAuth.getCurrentUser();// obtener el usuario actual
        Usuarios= FirebaseDatabase.getInstance().getReference("Usuarios");
        cargaDatosHeader();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
                break;
            case R.id.nav_amigos:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AmigosFragment()).commit();
                break;
            case R.id.nav_share:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MisProyectosFragment()).commit();
                break;
            case R.id.nav_cuenta:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CuentaFragment()).commit();
                break;
            case R.id.nav_logout:
                firebaseAuth.signOut();
                //Abre la actividad de login
                startActivity(new Intent(MainActivity.this, Login.class));
                Toast.makeText(this, "Cerraste sesión exitosamente!", Toast.LENGTH_SHORT).show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }


    private void cargaDatosHeader(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView= navigationView.getHeaderView(0);
        //variables de nav_ header
        headerUsuario = headerView.findViewById(R.id.headerUsuario);
        headerCorreo  = headerView.findViewById(R.id.headerCorreo);

        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {         //Del los datos almacenados en Usuario buscara la uid del usuario actual para obtener sus datos
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {                         //Funcion datasnapshop obtienes los hijo
                //si el usuario existe
                if(snapshot.exists()){                                                          //SI USAURIO EXISTE EN LA DB
                    //obtener los datos
                    usuario= snapshot.child("usuario").getValue().toString();                   // Le asignamos a la variable string el valor del atributo usuario
                    correo =snapshot.child("correo").getValue().toString();                      //getvalue().tostring() funciona para

                    //enviar los daos a sus respectivos textView
                    headerUsuario.setText(usuario);
                    headerCorreo.setText(correo);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}