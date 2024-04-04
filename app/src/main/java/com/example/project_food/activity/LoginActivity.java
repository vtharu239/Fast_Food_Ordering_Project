package com.example.project_food.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_food.R;
import com.example.project_food.model.ReadWriteUserDetails;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity
{
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 101;
    ImageView signinGoogle;
    EditText loginemail, loginpassword;
    Button btnlogin;
    TextView signupRedirectText, forgotpass;
    ProgressBar progressBar;
    FirebaseAuth authProfile;
    //ActivityLoginBinding binding;
    static final String TAG="LoginActivity";

    FirebaseDatabase database;
    //DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://projectfastfood-8a851-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginemail = findViewById(R.id.login_email);
        loginpassword = findViewById(R.id.login_password);
        btnlogin = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        forgotpass = findViewById(R.id.forgotPassword);
        progressBar = findViewById(R.id.login_progressBar);
        authProfile = FirebaseAuth.getInstance();

        // Show Hide Password using Eye Icon
        ImageView ivshowhidepass = findViewById(R.id.show_hide_password);
        ivshowhidepass.setImageResource(R.drawable.hidepass);
        ivshowhidepass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (loginpassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
                {
                    loginpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivshowhidepass.setImageResource(R.drawable.hidepass);
                }
                else
                {
                    loginpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivshowhidepass.setImageResource(R.drawable.showpass);
                }
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = loginemail.getText().toString();
                String password = loginpassword.getText().toString();

                if (email.isEmpty())
                {
                    loginemail.setError("Email không được để trống!");
                    loginemail.requestFocus();
                }
                else if (password.isEmpty())
                {
                    loginpassword.setError("Mặt khẩu không được để trống!");
                    loginpassword.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    loginemail.setError("Email không xác thực. Vui lòng thử lại!");
                    loginemail.requestFocus();
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(email, password);
                }
            }
        });
        signupRedirectText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(LoginActivity.this, "You can reset password now!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                finish();
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
                .requestEmail()
                .build();

        //mUser = authProfile.getCurrentUser();
        database = FirebaseDatabase.getInstance("https://projectfastfood-8a851-default-rtdb.firebaseio.com/");

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.revokeAccess();

        signinGoogle = findViewById(R.id.signinGoogle);
        signinGoogle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                googleSignIn();
            }
        });
    }

    private void googleSignIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try
            {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                authProfile.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>()
                {
                    @Override
                    public void onSuccess(AuthResult authResult)
                    {
                        if (task.isSuccessful())
                        {
//
                            FirebaseUser user = authProfile.getCurrentUser();
                            ReadWriteUserDetails user1 = new ReadWriteUserDetails();
                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                            referenceProfile.addListenerForSingleValueEvent(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot)
                                {
                                    if (!snapshot.exists())
                                    {
                                        Toast.makeText(LoginActivity.this, "Chào mừng lần đầu đăng nhập, Hãy cập nhật profile của bạn!", Toast.LENGTH_LONG).show();

                                        authProfile = FirebaseAuth.getInstance();
                                        authProfile.createUserWithEmailAndPassword(user.getEmail(), "123456").addOnCompleteListener(LoginActivity.this,
                                                new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task)
                                                    {
                                                        user1.setFullName(user.getDisplayName());
                                                        user1.setGender("");
                                                        user1.setBirthday("01/01/2011");
                                                        user1.setMobile("");


                                                        String txtFullName = user1.getFullName();
                                                        String txtBirthday = user1.getBirthday();
                                                        String txtPhone = user1.getMobile();
                                                        String txtGender = user1.getGender();

                                                        //Enter User Data into the Firebase realtime database
                                                        ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(txtFullName, txtBirthday, txtGender, txtPhone);

                                                        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users");
                                                        referenceProfile.child(user.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>()
                                                        {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {
                                                                Toast.makeText(LoginActivity.this,"Đăng nhập thành công!", Toast.LENGTH_LONG).show();
                                                                Intent intent = new Intent(LoginActivity.this, FingerVerifyActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        });
                                                    }
                                                });
                                        Intent intent = new Intent(LoginActivity.this, FingerVerifyActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        //database.getReference().child("Users").child(user.getUid());
                                        Toast.makeText(LoginActivity.this,"Đăng nhập thành công!", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(LoginActivity.this, FingerVerifyActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error)
                                {
                                    Toast.makeText(LoginActivity.this,"Có lỗi xảy ra!!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
            }
            catch (ApiException e)
            {
                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                startActivity(getIntent());
                finish();
            }
        }
    }

    private void loginUser(String email, String password)
    {
        authProfile.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    //Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_LONG).show();

                    //Get instance of the current User
                    FirebaseUser firebaseUser = authProfile.getCurrentUser();

                    // Check if Email is verified before user can access their profile
                    if (firebaseUser.isEmailVerified())
                    {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, FingerVerifyActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        firebaseUser.sendEmailVerification();
                        authProfile.signOut();
                        showAlertDialog();
                    }
                }
                else
                {
                    try
                    {
                        throw task.getException();
                    }
                    catch (FirebaseAuthInvalidUserException e)
                    {
                        loginemail.setError("Email không tồn tại hoặc chưa đăng ký. Vui lòng đăng ký tài khoản!");
                        loginemail.requestFocus();
                    }
                    catch (FirebaseAuthInvalidCredentialsException e)
                    {
                        Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(LoginActivity.this, "Có lỗi xảy ra. Vui lòng thử lại", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog()
    {
        //Setup the Alert Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email chưa được xác thực!");
        builder.setMessage("Vui lòng xác thực email!");

        // Open Email app if User click Continue Button
        builder.setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Trở về", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                startActivity(getIntent());
            }
        });

        // Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        // Show the AlertDialog
        alertDialog.show();
    }

    // Check if User is already logged in. In such case, starightaway take the User to the User's Profile
    @Override
    protected void onStart()
    {
        authProfile.signOut();
        super.onStart();
        if (authProfile.getCurrentUser() != null)
        {
            Toast.makeText(LoginActivity.this, "Bạn đang đăng nhập!", Toast.LENGTH_LONG).show();
            // Start the UserProfileActivity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        else
        {
            Toast.makeText(LoginActivity.this, "You can Login now!", Toast.LENGTH_LONG).show();
        }
    }
}