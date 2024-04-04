package com.example.project_food.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project_food.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddCategoryActivity extends AppCompatActivity {
    EditText img_url, name, type;
    Button UploadCateInfoBtn;
    ProgressDialog pd;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        //Progress dialog
        pd = new ProgressDialog(this);
        //Call xml
        img_url = findViewById(R.id.txtProImg);
        name = findViewById(R.id.txtProName);
        type = findViewById(R.id.txtProtype);

        //firestore
        db = FirebaseFirestore.getInstance();

        //click upload
        UploadCateInfoBtn = findViewById(R.id.UploadCateInfoBtn);
        UploadCateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input data
                String img = img_url.getText().toString().trim();
                String namepro = name.getText().toString().trim();
                String typepro = type.getText().toString().trim();
                upLoadData(img, namepro, typepro);
            }
        });
    }

    private void upLoadData(String img, String nampro, String typepro) {
        //set tittle of progress bar
        pd.setTitle("Adding Data to FireStore");
        pd.show();

        String id = UUID.randomUUID().toString();

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("img_url", img);
        map.put("name", nampro);
        map.put("type", typepro);
        db.collection("HomeCategory").document(typepro).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(AddCategoryActivity.this, "Uploaded Success...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddCategoryActivity.this, ManageCategoryActivity.class);
                startActivity(intent);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddCategoryActivity.this, "Upload Failded...", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

    }
}
