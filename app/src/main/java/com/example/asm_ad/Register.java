package com.example.asm_ad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editPhone;
    private Button btnSign;
    private TextView txtthongbao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editFirstName =findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editPhone = findViewById(R.id.editPhone);
        btnSign = findViewById(R.id.btnSign);
        txtthongbao = findViewById(R.id.txtthongbao);
        btnSign.setOnClickListener(v -> userRegiter());

    }
    private void userRegiter(){

        String mail = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String fistName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();

        if (mail.isEmpty() || password.isEmpty()|| fistName.isEmpty()|| lastName.isEmpty()|| phone.isEmpty()) {
            txtthongbao.setText("Vui lòng nhập đầy đủ thông tin");
            txtthongbao.setVisibility(View.VISIBLE);
            return;
        }
        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            txtthongbao.setText("Đăng ký thành công");
                            txtthongbao.setVisibility(View.VISIBLE);
                            navigateToLogin();
                        } else {
                            txtthongbao.setText("Tài khoản đã tồn tại");
                            txtthongbao.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
    private void navigateToLogin() {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
    }
}
