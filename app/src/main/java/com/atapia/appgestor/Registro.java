package com.atapia.appgestor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registro extends AppCompatActivity {
    Button buttonRegistrar;
    TextView textViewIrLogin;
    EditText editTextNombre, editTextApellido, editTextCorreo, editTextContrasenia, editTextConfirmarContrasenia;

    private String nombre, apellido, correo, contrasenia, confirmaContrasenia;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    //referenciar a la vista
        buttonRegistrar = findViewById(R.id.buttonRegistrar);
        textViewIrLogin = findViewById(R.id.textViewIrLogin);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellido = findViewById(R.id.editTextApellido);
        editTextCorreo = findViewById(R.id.editTextCorreo);
        editTextContrasenia = findViewById(R.id.editTextContrasenia);
        editTextConfirmarContrasenia = findViewById(R.id.editTextConfirmarContrasenia);

        //inicializar firebase
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Registro.this);
        progressDialog.setMessage("Espere por favor...");
        progressDialog.setCancelable(false);

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();
            }
        });
        textViewIrLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void validarDatos() {
    nombre = editTextNombre.getText().toString().trim();
    apellido = editTextApellido.getText().toString().trim();
    correo = editTextCorreo.getText().toString().trim();
    contrasenia = editTextContrasenia.getText().toString().trim();
    confirmaContrasenia = editTextConfirmarContrasenia.getText().toString().trim();

    //validaciones
        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(this, "El campo nombre esta vacio", Toast.LENGTH_SHORT).show();
            editTextNombre.setError("Nombre requerido");
            editTextNombre.requestFocus();
        } else if (TextUtils.isEmpty(apellido)) {
            Toast.makeText(this, "El campo apellido esta vacio", Toast.LENGTH_SHORT).show();
            editTextApellido.setError("Apellido requerido");
            editTextApellido.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Ingrese un correo valido", Toast.LENGTH_SHORT).show();
            editTextCorreo.setError("Correo inválido");
            editTextCorreo.requestFocus();
        } else if (TextUtils.isEmpty(contrasenia)) {
            Toast.makeText(this, "El campo contraseña esta vacio", Toast.LENGTH_SHORT).show();
            editTextContrasenia.setError("Contraseña requerida");
            editTextContrasenia.requestFocus();
        } else if (contrasenia.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            editTextContrasenia.setError("Mínimo 6 caracteres");
            editTextContrasenia.requestFocus();
        } else if (TextUtils.isEmpty(confirmaContrasenia)) {
            Toast.makeText(this, "El campo confirmar contraseña esta vacio", Toast.LENGTH_SHORT).show();
            editTextConfirmarContrasenia.setError("Confirmar contraseña requerido");
            editTextConfirmarContrasenia.requestFocus();
        } else if (!contrasenia.equals(confirmaContrasenia)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            editTextConfirmarContrasenia.setError("Las contraseñas no coinciden");
            editTextConfirmarContrasenia.requestFocus();
        } else {
            registrarUsuario();
        }
    }
    private void registrarUsuario() {
        progressDialog.setMessage("Registrando usuario...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(correo, contrasenia)
        .addOnSuccessListener(new OnSuccessListener<AuthResult>(){
            @Override
            public void onSuccess(AuthResult authResult) {
                GuardarInformacionUsuario();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Registro.this, "Error al registrar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GuardarInformacionUsuario() {
        progressDialog.setMessage("Guardando información del usuario...");

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();

        Usuario usuario = new Usuario(
                uid,
                nombre,
                apellido,
                correo,
                contrasenia,
                true
        );

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        assert uid != null;
        databaseReference.child(uid)
                .setValue(usuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(Registro.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Registro.this, Dashboard.class));
                        finish(); //podria lanzar directamente al dashbo
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Registro.this, "Error al guardar información: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}