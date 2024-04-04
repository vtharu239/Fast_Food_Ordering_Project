package com.example.project_food.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project_food.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateCagoryActivity extends AppCompatActivity {
    EditText txtCateImg,txtCateName,txtCatetype;
    Button btnsave;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_cagory);
        txtCateImg=findViewById(R.id.txtCateImg);
        txtCateName=findViewById(R.id.txtCateName);
        txtCatetype=findViewById(R.id.txtCatetype);
        btnsave=findViewById(R.id.UpdateCateInfoBtn);


        btnsave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (txtCateImg.getText().length() > 0 && txtCateName.getText().length() > 0 &&
                        txtCatetype.getText().length()>0) {
                    saveData(txtCateImg.getText().toString(), txtCateName.getText().toString(), txtCatetype.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "What happen!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Intent intent=getIntent();
        if(intent!=null) {
            id = intent.getStringExtra("id");
            txtCateImg.setText(intent.getStringExtra("img_url"));
            txtCateName.setText(intent.getStringExtra("name"));
            txtCatetype.setText(intent.getStringExtra("type"));

        }

    }
    private void saveData( String txtCateImg, String txtCateName,  String txtCatetype) {
        Map<String, Object> map = new HashMap<>();
        map.put("img_url", txtCateImg);
        map.put("name", txtCateName);
        map.put("type", txtCatetype);
        if (id!=null) {
            db.collection("HomeCategory").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(UpdateCagoryActivity.this, "Update Change Success...", Toast.LENGTH_SHORT).show();
                        Intent intent1=new Intent(UpdateCagoryActivity.this, ManageCategoryActivity.class);
                        startActivity(intent1);
                        finish();
                    }
                    else{
                        Toast.makeText(UpdateCagoryActivity.this, "Update Failded...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            db.collection("HomeCategory").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(UpdateCagoryActivity.this, "Update different category Success...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateCagoryActivity.this, ManageCategoryActivity.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateCagoryActivity.this, "Update Failded...", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}