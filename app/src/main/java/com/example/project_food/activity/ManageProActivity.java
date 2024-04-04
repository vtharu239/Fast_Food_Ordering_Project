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
import com.example.project_food.model.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManageProActivity extends AppCompatActivity {

    Button btnAddPro;
    FirebaseFirestore db;

    RecyclerView rvDeAndEdit;
    ViewAllEditDeleteAdapter proAdapter;

    List<ViewAllModel> ProList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_pro);
        btnAddPro=findViewById(R.id.btnAddPro);
        db = FirebaseFirestore.getInstance();

        btnAddPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageProActivity.this, AddProActivity.class);
                startActivity(intent);
                finish();;
            }
        });
        rvDeAndEdit=findViewById(R.id.rvDeAndEdit);
        rvDeAndEdit.setLayoutManager(new LinearLayoutManager(this));
        ProList= new ArrayList<>();
        proAdapter=new ViewAllEditDeleteAdapter(ManageProActivity.this,ProList);
        rvDeAndEdit.setAdapter(proAdapter);
        proAdapter.setDialog(new ViewAllEditDeleteAdapter.Dialog() {
            @Override
            public void OnClick(int pos) {
                final CharSequence[] dialogItem={"Edit","Delete"};
                AlertDialog.Builder dialog=new AlertDialog.Builder(ManageProActivity.this);
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialoginterface, int i) {
                        switch (i){
                            case 0:
                                Intent intent=new Intent(ManageProActivity.this,UpdateProActivity.class);
                                intent.putExtra("id",ProList.get(pos).getId());
                                intent.putExtra("name",ProList.get(pos).getName());
                                intent.putExtra("description",ProList.get(pos).getDescription());
                                intent.putExtra("rating",ProList.get(pos).getRating());
                                intent.putExtra("price",ProList.get(pos).getPrice());//int
                                intent.putExtra("img_url",ProList.get(pos).getImg_url());
                                intent.putExtra("type",ProList.get(pos).getType());
                                startActivity(intent);
                                finish();
                                break;
                            case 1:
                                deleteData(ProList.get(pos).getId());
                                Toast.makeText(ManageProActivity.this, "Delete SuccessFull", Toast.LENGTH_SHORT).show();
                                Intent intent1=new Intent(ManageProActivity.this, ManageProActivity.class);
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
        db.collection("AllProducts")
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
                                ViewAllModel homep = document.toObject(ViewAllModel.class);
                                homep.setId(document.getId());
                                ProList.add(homep);
                                proAdapter.notifyDataSetChanged();
                            }

                        }
                        else
                        {
                            Toast.makeText(ManageProActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });




    }
    private void deleteData(String id) {
        db.collection("AllProducts").document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Delete SuccessFull", Toast.LENGTH_SHORT).show();
                            Intent intent1=new Intent(ManageProActivity.this, ManageProActivity.class);
                            startActivity(intent1);
                        }

                        getData();

                    }

                });


    }
}