package com.example.project_food.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.project_food.R;
import com.example.project_food.adapter.CategoryEditDeleteAdapter;
import com.example.project_food.adapter.ViewAllEditDeleteAdapter;
import com.example.project_food.model.HomeCategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManageCategoryActivity extends AppCompatActivity {

    Button btnAddCate;
    FirebaseFirestore db;

    RecyclerView rvDeAndEdit;
    CategoryEditDeleteAdapter cateAdapter;

    List<HomeCategory> CateList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);
        btnAddCate=findViewById(R.id.btnAddCate);
        db = FirebaseFirestore.getInstance();

        btnAddCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageCategoryActivity.this, AddCategoryActivity.class);
                startActivity(intent);
                finish();;
            }
        });
        rvDeAndEdit=findViewById(R.id.rvDeAndEditCate);
        rvDeAndEdit.setLayoutManager(new LinearLayoutManager(this));
        CateList= new ArrayList<>();
        cateAdapter=new CategoryEditDeleteAdapter(ManageCategoryActivity.this,CateList);
        rvDeAndEdit.setAdapter(cateAdapter);
        cateAdapter.setDialog(new CategoryEditDeleteAdapter.Dialog() {
            @Override
            public void OnClick(int pos) {
                final CharSequence[] dialogItem={"Edit","Delete"};
                AlertDialog.Builder dialog=new AlertDialog.Builder(ManageCategoryActivity.this);
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){
                            case 0:
                                Intent intent=new Intent(ManageCategoryActivity.this,UpdateCagoryActivity.class);
                                intent.putExtra("id",CateList.get(pos).getId());
                                intent.putExtra("name",CateList.get(pos).getName());
                                intent.putExtra("img_url",CateList.get(pos).getImg_url());
                                intent.putExtra("type",CateList.get(pos).getType());
                                startActivity(intent);
                                finish();
                                break;
                            case 1:
                                deleteData(CateList.get(pos).getId());
                                Toast.makeText(ManageCategoryActivity.this, "Delete SuccessFull", Toast.LENGTH_SHORT).show();
                                Intent intent1=new Intent(ManageCategoryActivity.this,ManageCategoryActivity.class);
                                startActivity(intent1);
                                finish();
                                break;

                        }
                    }
                });
                dialog.show();
            }
        });
        getData();
    }


    private void getData() {
        db.collection("HomeCategory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                HomeCategory homeCate = document.toObject(HomeCategory.class);
                                homeCate.setId(document.getId());
                                CateList.add(homeCate);
                                cateAdapter.notifyDataSetChanged();
                            }

                        }
                        else
                        {
                            Toast.makeText(ManageCategoryActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });




    }
    private void deleteData(String id) {
        db.collection("HomeCategory").document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(ManageCategoryActivity.this, "Delete SuccessFull", Toast.LENGTH_SHORT).show();
                            Intent intent1=new Intent(ManageCategoryActivity.this, ManageCategoryActivity.class);
                            startActivity(intent1);
                        }

                        getData();

                    }

                });


    }
}