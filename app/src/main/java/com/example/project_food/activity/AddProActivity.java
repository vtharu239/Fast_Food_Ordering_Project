
package com.example.project_food.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
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

public class AddProActivity extends AppCompatActivity {
    EditText description, img_url, name, price, rating, type;
    Button UploadProInfoBtn;
    ProgressDialog pd;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pro);


        //Progress dialog

        //Progress dialog

        pd = new ProgressDialog(this);
        //Call xml
        description = findViewById(R.id.txtProDes);
        img_url = findViewById(R.id.txtProImg);
        name = findViewById(R.id.txtProName);
        price = findViewById(R.id.txtProPrice);
        rating = findViewById(R.id.txtProRating);
        type = findViewById(R.id.txtProtype);

        //firestore
        db = FirebaseFirestore.getInstance();

        //click upload
        UploadProInfoBtn = findViewById(R.id.UploadProInfoBtn);
        UploadProInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input data
                String des = description.getText().toString().trim();
                String img = img_url.getText().toString().trim();
                String namepro = name.getText().toString().trim();
                Integer pricepro = Integer.parseInt(price.getText().toString());
                String ratingpro = rating.getText().toString().trim();
                String typepro = type.getText().toString().trim();
                upLoadData(des, img, namepro, pricepro, ratingpro, typepro);
            }
        });
    }

    private void upLoadData(String des, String img, String nampro, int pricepro, String ratingpro, String typepro) {
        //set tittle of progress bar
        pd.setTitle("Adding Data to FireStore");
        pd.show();
        String id = UUID.randomUUID().toString();

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("description", des);
        map.put("img_url", img);
        map.put("name", nampro);
        map.put("price", pricepro);
        map.put("rating", ratingpro);
        map.put("type", typepro);
        db.collection("AllProducts").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(AddProActivity.this, "Uploaded Success...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddProActivity.this, ManageProActivity.class);
                startActivity(intent);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddProActivity.this, "Upload Failded...", Toast.LENGTH_SHORT).show();

                finish();


            }
        });

    }
}
