package com.example.project_food.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_food.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UpdateEmailActivity extends AppCompatActivity
{
    FirebaseAuth authProfile;
    FirebaseUser firebaseUser;
    ProgressBar progressBar;
    TextView txtViewAuthenticated;
    Button btn_authenticated, btn_savenewEmail;
    EditText currentEmail, newEmail, curentPassword;
    String userOldEmail, userPassword;
    String txtFullName, txtBirthday, txtGender, txtPhone, txtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        currentEmail = findViewById(R.id.input_currentEmail);
        curentPassword = findViewById(R.id.input_password);
        newEmail = findViewById(R.id.input_newEmail);
        txtViewAuthenticated = findViewById(R.id.txt_update_email_authentication);

        btn_authenticated = findViewById(R.id.btn_verify);
        btn_savenewEmail = findViewById(R.id.btn_saveEmail);

        progressBar = findViewById(R.id.updateEmail_progessBar);

        btn_savenewEmail.setEnabled(false);
        newEmail.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        //userOldEmail = firebaseUser.getEmail();

        if (firebaseUser.equals(""))
        {
            Toast.makeText(UpdateEmailActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateEmailActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            reAuthenticate(firebaseUser);
        }
    }

    private void reAuthenticate(FirebaseUser firebaseUser)
    {
        btn_authenticated.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                userOldEmail = currentEmail.getText().toString();
                userPassword = curentPassword.getText().toString();

                if (userOldEmail.isEmpty())
                {
                    currentEmail.setError("Email không được để trống!");
                    currentEmail.requestFocus();
                }
                else if (userPassword.isEmpty())
                {
                    curentPassword.setError("Mật khẩu không được để trống!");
                    curentPassword.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(userOldEmail).matches())
                {
                    currentEmail.setError("Email không chính xác!");
                    currentEmail.requestFocus();
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), userPassword);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                progressBar.setVisibility(View.GONE);

                                newEmail.setEnabled(true);
                                curentPassword.setEnabled(false);
                                btn_authenticated.setEnabled(false);
                                btn_savenewEmail.setEnabled(true);

                                Toast.makeText(UpdateEmailActivity.this, "Mật khẩu đã được xác thực. Bạn có thể thay đổi Email mới!", Toast.LENGTH_SHORT).show();

                                txtViewAuthenticated.setText("Bạn đã xác thực thành công. Hãy cập nhật email mới");


                                // Đổi màu button thay đổi emai
                                btn_savenewEmail.setBackgroundTintList(ContextCompat.getColorStateList(UpdateEmailActivity.this, R.color.dargGreen));

                                btn_savenewEmail.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        updateEmail(firebaseUser);
                                    }
                                });
                            }
                            else
                            {
                                try
                                {
                                    throw task.getException();
                                }
                                catch (Exception e)
                                {
                                    //Toast.makeText(UpdateEmailActivity.this, "Mật khẩu không chính xác!", Toast.LENGTH_LONG).show();
                                    curentPassword.setError("Mật khẩu không chính xác!");
                                    curentPassword.requestFocus();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void updateEmail(FirebaseUser firebaseUser)
    {
        String userNewEmail = newEmail.getText().toString();

        if (userNewEmail.isEmpty())
        {
            newEmail.setError("Email mới không được để trống");
            newEmail.requestFocus();
        }
//        else if (!Patterns.EMAIL_ADDRESS.matcher(userNewEmail).matches())
//        {
//            Toast.makeText(UpdateEmailActivity.this, "Vui lòng điền Email đã tồn tại", Toast.LENGTH_SHORT).show();
//        }
        else if (userOldEmail.matches(userNewEmail))
        {
            newEmail.setError("Email mới không được trùng với email cũ. Vui lòng nhập email khác!");
            newEmail.requestFocus();
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
            firebaseUser.updateEmail(userNewEmail).addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isComplete())
                    {
                        // Verify Email
                        firebaseUser.sendEmailVerification();
                        Toast.makeText(UpdateEmailActivity.this, "Email mới đã được cập nhật. Vui lòng xác thực email!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateEmailActivity.this, UpdateProfileActivity.class);
                        startActivity(intent);
                        finish();;
                    }
                    else
                    {
                        try
                        {
                            throw task.getException();
                        }
                        catch (Exception e)
                        {
                            newEmail.setError("Email không tồn tại");
                            newEmail.requestFocus();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}