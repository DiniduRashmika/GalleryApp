package com.testapp.gallery;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    CircleImageView profileImg;
    EditText nameTxt, emailTxt, passTxt;
    TextView submitBtn, loginBtn;
    SQLiteHelper dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        profileImg = findViewById(R.id.acc_img);
        nameTxt = findViewById(R.id.name_txt);
        emailTxt = findViewById(R.id.email_txt);
        passTxt = findViewById(R.id.password_txt);
        submitBtn = findViewById(R.id.submit_btn);
        loginBtn = findViewById(R.id.login_btn);

        dbHandler = new SQLiteHelper(RegisterActivity.this);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameTxt.getText().toString();
                String email = emailTxt.getText().toString();
                String pass = passTxt.getText().toString();
                registerUser(name,email,pass);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void registerUser(String name, String email, String pass) {

        if(!name.isEmpty() || !email.isEmpty() || !pass.isEmpty()){
            if(isValidEmail(email)){
                dbHandler.addUser("https://cdn-icons-png.flaticon.com/512/149/149071.png",name,email,pass);
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(RegisterActivity.this,"Enter a correct Email!",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(RegisterActivity.this,"Fill the fields!",Toast.LENGTH_SHORT).show();
        }

    }

    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}