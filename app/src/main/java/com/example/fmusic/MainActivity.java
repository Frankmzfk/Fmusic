package com.example.fmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextView email;
    TextView login_view;
    TextView password;
    Button login;
    Button register;
    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        email = (TextView) findViewById(R.id.email);
        login_view = (TextView) findViewById(R.id.login_view);
        password = (TextView) findViewById(R.id.password);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Нет почты", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Нет пароля", Toast.LENGTH_LONG).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(MainActivity.this, MainMenu.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, "Войти не удалось",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Нет почты", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Нет пароля", Toast.LENGTH_LONG).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    login_view.setText(mAuth.getCurrentUser().getEmail());
                                    Intent intent = new Intent(MainActivity.this, MainMenu.class);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Что-то пошло не так",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        start = (Button) findViewById(R.id.start_);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null)
                {
                    Intent intent = new Intent(MainActivity.this, MainMenu.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Сначала нужно войти/зарегаться",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null)
        {
            login_view.setText(mAuth.getCurrentUser().getEmail());
            Data.set_my_id(mAuth.getUid());
            Data.init();
        }
    }
}