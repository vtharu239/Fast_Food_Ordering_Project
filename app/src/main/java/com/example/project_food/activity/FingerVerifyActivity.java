package com.example.project_food.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.project_food.R;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class FingerVerifyActivity extends AppCompatActivity
{
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    ConstraintLayout mMainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_verify);
        mMainLayout=findViewById(R.id.finger_layout);

        BiometricManager biometricManager=BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getApplicationContext(),"Can't recognize",Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:

                Toast.makeText(getApplicationContext(),"Not working",Toast.LENGTH_SHORT).show();
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:

                Toast.makeText(getApplicationContext(),"No FingerPrint Assigned",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FingerVerifyActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
        }
        Executor executor= ContextCompat.getMainExecutor(this);

        biometricPrompt=new BiometricPrompt(FingerVerifyActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);

            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_SHORT).show();
                mMainLayout.setVisibility(View.VISIBLE);
                Intent intent = new Intent(FingerVerifyActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        promptInfo=new BiometricPrompt.PromptInfo.Builder().setTitle("Tech Projects").setDescription("Use FingerPrint to Login").setDeviceCredentialAllowed(true).build();
        biometricPrompt.authenticate(promptInfo);

    }
}