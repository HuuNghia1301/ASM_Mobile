package com.example.asm_ad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editEmail ;
    private EditText editPassword ;
    private Button btnLogin ;
    private TextView txtthongbao ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtthongbao = findViewById(R.id.txtthongbao);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(editEmail.getText().toString().equals("admin")
                       && editPassword.getText().toString().equals("admin")) {
                   navigateToRegister();
                   txtthongbao.setText("Đăng nhập thành công");
               }else{
                   txtthongbao.setText("Đăng nhập thất bại");
               }
            }
        });

    }
    private void navigateToRegister(){
        Intent intent = new Intent(MainActivity.this,Home.class);
        startActivity(intent);
        finish();
    }
}