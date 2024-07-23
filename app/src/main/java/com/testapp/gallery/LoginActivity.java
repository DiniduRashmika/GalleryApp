package com.testapp.gallery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    EditText emailTxt, passTxt;
    TextView submitBtn, regBtn;
    SQLiteHelper dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTxt = findViewById(R.id.email_txt);
        passTxt = findViewById(R.id.password_txt);
        submitBtn = findViewById(R.id.submit_btn);
        regBtn = findViewById(R.id.register_btn);

        dbHandler = new SQLiteHelper(LoginActivity.this);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTxt.getText().toString();
                String pass = passTxt.getText().toString();
                loginUser(email,pass);
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void loginUser(String email, String pass) {

        if(dbHandler.checkUserExists(email,pass)){

            SharedPreferences sharedPreferences = getSharedPreferences("Startup", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("isStarted", "yes");
            editor.putString("email", email);
            editor.apply();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(LoginActivity.this,"Login Failed!",Toast.LENGTH_SHORT).show();
        }
    }
}