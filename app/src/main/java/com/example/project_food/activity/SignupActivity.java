package com.example.project_food.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_food.R;
import com.example.project_food.model.ReadWriteUserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity
{
    EditText signupfullName, signupEmail, signupBirthday, signupPhone, signupPassword, confirmPassword;
    Button btnSignup;
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView loginRedirectText;
    ProgressBar progressBar;
    private  DatePickerDialog picker;
    private  static final  String TAG = "SignupAvtivity";
    FirebaseDatabase database;
    //DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://projectfastfood-8a851-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //getSupportActionBar().setTitle("Signup");
        Toast.makeText(SignupActivity.this,"You can signup now!", Toast.LENGTH_LONG).show();

        signupfullName = findViewById(R.id.signup_fullName);
        signupEmail = findViewById(R.id.signup_email);
        signupBirthday = findViewById(R.id.signup_birthday);
        signupPhone = findViewById(R.id.signup_phone);
        signupPassword = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.confirmpassword);

        progressBar = findViewById(R.id.progressBar);

        btnSignup = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        //RadioButton for Gender
        radioGroup = findViewById(R.id.signup_chooseGender);
        radioGroup.clearCheck();

        // Show Hide Password using Eye Icon
        ImageView ivshowhidepass = findViewById(R.id.signup_show_hide_password);
        ivshowhidepass.setImageResource(R.drawable.hidepass);
        ivshowhidepass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (signupPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
                {
                    signupPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivshowhidepass.setImageResource(R.drawable.hidepass);
                }
                else
                {
                    signupPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivshowhidepass.setImageResource(R.drawable.showpass);
                }
            }
        });

        // Show Hide ConfirmPassword using Eye Icon
        ImageView ivshowhidecheckpass = findViewById(R.id.signup_show_hide_checkpassword);
        ivshowhidecheckpass.setImageResource(R.drawable.hidepass);
        ivshowhidecheckpass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (signupPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
                {
                    confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivshowhidecheckpass.setImageResource(R.drawable.hidepass);
                }
                else
                {
                    confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivshowhidecheckpass.setImageResource(R.drawable.showpass);
                }
            }
        });

        //Setting up DatePicker on EditText
        signupBirthday.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(calendar.DAY_OF_MONTH);
                int month = calendar.get(calendar.MONTH);
                int year = calendar.get(calendar.YEAR);

                //DAte Picker Dialog
                picker = new DatePickerDialog(SignupActivity.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                    {
                        signupBirthday.setText(dayOfMonth +"/" + (month+1)+"/"+year);
                    }
                }, year, month, day);
                picker.show();
            }
        });
        database = FirebaseDatabase.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int selectedGender = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedGender);

                String txtFullName = signupfullName.getText().toString();
                String txtEmail = signupEmail.getText().toString();
                String txtBirthday = signupBirthday.getText().toString();
                String txtPhone = signupPhone.getText().toString();
                String password = signupPassword.getText().toString();
                String checkpassword = confirmPassword.getText().toString();
                String txtGender;

                // validate Mobile Number using Matcher and Pattern (Regular Expression)
                String mobileRegex = "[0][0-9]{9}"; // First no. just can be {0} and rest 9 nos. can be any no.
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher = mobilePattern.matcher(txtPhone);

                if (txtFullName.isEmpty())
                {
                    signupfullName.setError("Tên người dùng không được để trống!");
                    signupfullName.requestFocus();                }
                else if (txtEmail.isEmpty())
                {
                    signupEmail.setError("Email không được để trống!");
                    signupEmail.requestFocus();
                }
                else if (txtBirthday.isEmpty())
                {
                    Toast.makeText(SignupActivity.this, "Vui lòng chọn ngày sinh!", Toast.LENGTH_SHORT).show();
                }
                else if (radioGroup.getCheckedRadioButtonId() == -1)
                {
                    Toast.makeText(SignupActivity.this, "Vui lòng chọn giới tính!", Toast.LENGTH_SHORT).show();
                }
                else if (txtPhone.isEmpty())
                {
                    signupPhone.setError("Số điện thoại không được để trống!");
                    signupPhone.requestFocus();
                }
                else if (txtPhone.length() != 10)
                {
                    signupPhone.setError("Số điện thoại phải đủ 10 số. Vui lòng nhập lại!");
                    signupPhone.requestFocus();
                }
                else if (!mobileMatcher.find())
                {
                    signupPhone.setError("Số điện thoại không hợp lệ. Vui lòng nhập lại!");
                    signupPhone.requestFocus();
                }
                else if (password.isEmpty())
                {
                    signupPassword.setError("Mật khẩu không được để trống!");
                    signupPassword.requestFocus();
                }
                else if (password.length() < 6)
                {
                    signupPassword.setError("Mật khẩu phải có ít nhất 6 ký tự. Vui lòng nhập lại!");
                    signupPassword.requestFocus();
                }
                else if (checkpassword.isEmpty())
                {
                    confirmPassword.setError("Mật khẩu xác nhận không được để trống!");
                    confirmPassword.requestFocus();
                }

                else if (!password.equals(checkpassword))
                {
                    confirmPassword.setError("Mật khẩu không chính xác!");
                    confirmPassword.requestFocus();
                    confirmPassword.clearComposingText();;
                }
                else
                {
                    txtGender = radioButton.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    signupUser (txtFullName, txtBirthday, txtGender, txtPhone, txtEmail, password);
                }

            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void signupUser (String txtFullName, String txtBirthday, String txtGender, String txtPhone, String txtEmail, String password)
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(txtEmail, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    //Enter User Data into the Firebase realtime database
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(txtFullName, txtBirthday, txtGender, txtPhone);

                    // Extracting user reference from DB for "Users"
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Users");

                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                //Send Verification Email
                                firebaseUser.sendEmailVerification();

                                Toast.makeText(SignupActivity.this, "Đăng ký tài khoản thành công! Vui lòng xác thực email của bạn trước khi đăng nhập.", Toast.LENGTH_LONG).show();

                                //Open Main Activity after successful signup
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);

                                // To prevent User from returning back to Signup Activity on pressing back button
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(SignupActivity.this, "Đăng ký tài khoản không thành công. Vui lòng thử lại!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                else
                {
                    try
                    {
                        throw  task.getException();
                    }
                    catch (FirebaseAuthWeakPasswordException e)
                    {
                        signupPassword.setError("Mật khẩu yếu. Vui lòng đăng ký mật khẩu với ít nhất 1 chữ in hoa, 1 kí tự đặc biệt...");
                        signupPassword.requestFocus();
                    }
                    catch (FirebaseAuthInvalidCredentialsException e)
                    {
                        signupEmail.setError("Email không tồn tại hoặc đã được sử dụng. Vui lòng đăng ký email mới!");
                        signupEmail.requestFocus();
                    }
                    catch (FirebaseAuthUserCollisionException e)
                    {
                        signupEmail.setError("Người dùng đã tồn tại. Vui lòng đăng ký email mới!");
                        signupEmail.requestFocus();
                    }
                    catch (Exception e)
                    {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
}