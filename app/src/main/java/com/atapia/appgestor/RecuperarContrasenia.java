package com.atapia.appgestor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Patterns;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarContrasenia extends AppCompatActivity {
    private EditText editTextEmailRecuperar;
    private Button buttonRecuperar;
    private TextView textViewIrLogin;

    private String emailRecuperar;
    private FirebaseAuth authProfile;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasenia);
        //ref
        editTextEmailRecuperar = findViewById(R.id.editTextEmailRecuperar);
        buttonRecuperar = findViewById(R.id.buttonRecuperar);
        textViewIrLogin = findViewById(R.id.textViewIrLogin);
        progressDialog = new ProgressDialog(RecuperarContrasenia.this);
        //firebase
        authProfile = FirebaseAuth.getInstance();
        //listener
        buttonRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editTextEmailRecuperar.getText().toString())){
                    Toast.makeText(RecuperarContrasenia.this, "Ingrese un correo", Toast.LENGTH_SHORT).show();
                    editTextEmailRecuperar.setError("Se requiere correo");
                    editTextEmailRecuperar.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(editTextEmailRecuperar.getText().toString()).matches()){
                    Toast.makeText(RecuperarContrasenia.this, "Ingrese un correo válido", Toast.LENGTH_SHORT).show();
                    editTextEmailRecuperar.setError("Se requiere correo válido");
                    editTextEmailRecuperar.requestFocus();
                }else{
                    progressDialog.setMessage("Espere por favor...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    emailRecuperar = editTextEmailRecuperar.getText().toString().trim();
                    recuperarContrasenia(emailRecuperar);
                }


            }
        });
        textViewIrLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void recuperarContrasenia(String emailRecuperar) {
        authProfile.setLanguageCode("es");
        authProfile.sendPasswordResetEmail(emailRecuperar).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RecuperarContrasenia.this, "Se ha enviado un correo para restablecer la contraseña.", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(RecuperarContrasenia.this, "Error al enviar el correo de restablecimiento de contraseña.", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();

            }
        });
    }
}