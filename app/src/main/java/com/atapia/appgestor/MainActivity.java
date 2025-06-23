package com.atapia.appgestor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Button btnweb, btnLogin;
    private TextView tvForgetPassword, tvnewuser;
    private Button btnGoogleSignIn;
    private FirebaseAuth mAuth;
    private EditText etUser, etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //referencia a la vista
        btnweb = findViewById(R.id.btnweb);
        btnLogin = findViewById(R.id.btnLogin);
        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        tvForgetPassword = findViewById(R.id.tvForgetPassword);
        tvnewuser = findViewById(R.id.tvnewuser);
//        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btnweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PaginaWeb.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();
            }
        });

        tvnewuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Registro.class));
            }
        });

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RecuperarContrasenia.class));
            }
        });

//        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "No Esta Implementado Todavia :v,Regresa mañana", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
    public void validarDatos(){
        String email = etUser.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etUser.requestFocus();
            Toast.makeText(MainActivity.this, "Por favor, ingrese correo electrónico", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            etPassword.requestFocus();
            Toast.makeText(MainActivity.this, "Por favor, ingrese correo y contraseña", Toast.LENGTH_SHORT).show();
        }else{
            iniciarSesion(email, password);
        }

    }
    private void iniciarSesion(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Inicio de sesión exitoso.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Error al iniciar sesión: " + Objects.requireNonNull(task.getException()).getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //si el usuario esta loggeado mandar directamente al dashboard
    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, Dashboard.class));
            finish();
        }
    }
}