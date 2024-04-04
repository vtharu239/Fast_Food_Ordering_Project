package com.example.project_food.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class UpdateProActivity extends AppCompatActivity {
    EditText txtProImg,txtProName,txtProDes,txtProPrice,txtProRating,txtProtype;
    Button btnsave;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private String id;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pro);
        txtProImg=findViewById(R.id.txtProImg);
        txtProName=findViewById(R.id.txtProName);
        txtProDes=findViewById(R.id.txtProDes);
        txtProPrice=findViewById(R.id.txtProPrice);
        txtProRating=findViewById(R.id.txtProRating);
        txtProtype=findViewById(R.id.txtProtype);
        btnsave=findViewById(R.id.UpdateProInfoBtn);


        btnsave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (txtProDes.getText().length() > 0 && txtProImg.getText().length() > 0 &&
                        txtProName.getText().length() > 0 &&  txtProPrice.getText().length() > 0 &&
                        txtProRating.getText().length() > 0 && txtProtype.getText().length() > 0) {
                    Integer pricepro=Integer.parseInt(txtProPrice.getText().toString());
                    saveData(txtProDes.getText().toString(), txtProImg.getText().toString(), txtProName.getText().toString(),
                            pricepro, txtProRating.getText().toString(), txtProtype.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "What happen!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Intent intent=getIntent();
        if(intent!=null) {
            id = intent.getStringExtra("id");
            txtProDes.setText(intent.getStringExtra("description"));
            txtProImg.setText(intent.getStringExtra("img_url"));
            txtProName.setText(intent.getStringExtra("name"));
            //fix Price
            int price1 = intent.getIntExtra("price", 1);
            txtProPrice.setText(""+price1);
            txtProRating.setText(intent.getStringExtra("rating"));
            txtProtype.setText(intent.getStringExtra("type"));

        }

    }
    private void saveData(String txtProDes, String txtProImg, String txtProName, int pricepro, String txtProRating, String txtProtype) {
        Map<String, Object> map = new HashMap<>();
        map.put("description", txtProDes);
        map.put("img_url", txtProImg);
        map.put("name", txtProName);
        map.put("price", pricepro);
        map.put("rating", txtProRating);
        map.put("type", txtProtype);
        if (id!=null) {
            db.collection("AllProducts").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(UpdateProActivity.this, "Update Success Change...", Toast.LENGTH_SHORT).show();
                        Intent intent2=new Intent(UpdateProActivity.this, ManageProActivity.class);
                        startActivity(intent2);
                        finish();
                    }
                    else{
                        Toast.makeText(UpdateProActivity.this, "Update Failded...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
//            } else {
//                db.collection("AllProducts").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Toast.makeText(UpdateProActivity.this, "Update Success Different...", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(UpdateProActivity.this, ManageProActivity.class);
//                        startActivity(intent);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(UpdateProActivity.this, "Update Failded...", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
        }
    }
}