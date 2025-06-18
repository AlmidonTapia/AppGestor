package com.atapia.appgestor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Registro extends AppCompatActivity {
    Button buttonRegistrar;
    TextView textViewIrLogin;
    EditText editTextNombre, editTextApellido, editTextCorreo, editTextContrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    //referenciar a la vista
        buttonRegistrar = findViewById(R.id.buttonRegistrar);
        textViewIrLogin = findViewById(R.id.textViewIrLogin);

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellido = findViewById(R.id.editTextApellido);
        editTextCorreo = findViewById(R.id.editTextCorreo);
        editTextContrasenia = findViewById(R.id.editTextContrasenia);

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Toast.makeText(Registro.this,getNombre()+" "+getApellido()+" "+getCorreo()+" "+getContrasenia(),Toast.LENGTH_SHORT).show();
            }
        });
        textViewIrLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public String getNombre(){
        if (editTextNombre != null){
            return editTextNombre.getText().toString();
        }else {
            Toast.makeText(this, "Debe ingresar un nombre", Toast.LENGTH_SHORT).show();
        }
        return "";
    }
    public String getApellido(){
        if (editTextApellido != null){
            return editTextApellido.getText().toString();
        }else {
            Toast.makeText(this, "Debe ingresar un apellido", Toast.LENGTH_SHORT).show();
        }
        return "";
    }
    public String getCorreo(){
        if (editTextCorreo != null){
            return editTextCorreo.getText().toString();
        }else {
            Toast.makeText(this, "Debe ingresar un correo", Toast.LENGTH_SHORT).show();
        }
        return "";
    }
    public String getContrasenia(){
        if (editTextContrasenia != null){
            return editTextContrasenia.getText().toString();
        }else {
            Toast.makeText(this, "Debe ingresar una contraseña", Toast.LENGTH_SHORT).show();
        }
        return "";
    }


}