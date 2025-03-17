package com.example.asm_ad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editPassword;
    private Button btnLogin;
    private TextView txtthongbao;
    private FirebaseAuth mAuth;
    private Button btnSign;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtthongbao = findViewById(R.id.txtthongbao);
        btnSign = findViewById(R.id.btnSign);
        btnLogin.setOnClickListener(v -> loginUser());
        btnSign.setOnClickListener(v -> navigateToRegister());
    }

    private void loginUser() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            txtthongbao.setText("Vui lòng nhập đầy đủ thông tin");
            txtthongbao.setVisibility(View.VISIBLE);
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Lưu trạng thái đăng nhập
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();

                        txtthongbao.setText("Đăng nhập thành công");
                        txtthongbao.setVisibility(View.VISIBLE);

                        navigateToHome();

                    } else {
                        txtthongbao.setText("Thông Tin Tài Hoặc Tài Khoản Không Chính Xác ");
                        txtthongbao.setVisibility(View.VISIBLE);
                    }
                });
    }
    public void Register(View view) {
        navigateToRegister();
    }

    private void navigateToRegister() {
        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);
    }

    private void navigateToHome() {
        Intent intent = new Intent(MainActivity.this, Home.class);
        startActivity(intent);
        finish();
    }
}
