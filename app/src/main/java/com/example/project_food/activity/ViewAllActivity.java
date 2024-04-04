package com.example.project_food.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.project_food.R;
import com.example.project_food.adapter.ViewAllAdapter;
import com.example.project_food.model.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity
{
    FirebaseFirestore firestore;
    RecyclerView recyclerView;
    ViewAllAdapter viewAllAdapter;
    List<ViewAllModel> viewAllModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        firestore = FirebaseFirestore.getInstance();

        String type = getIntent().getStringExtra("type");
        recyclerView = findViewById(R.id.view_all_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewAllModelList = new ArrayList<>();
        viewAllAdapter = new ViewAllAdapter(this, viewAllModelList);
        recyclerView.setAdapter(viewAllAdapter);

        if (type != null && type.equalsIgnoreCase("chicken"))
        {
            firestore.collection("AllProducts").whereEqualTo("type", "chicken")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments())
                    {
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        if (type != null && type.equalsIgnoreCase("pizza"))
        {
            firestore.collection("AllProducts").whereEqualTo("type", "pizza")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments())
                            {
                                ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                                viewAllModelList.add(viewAllModel);
                                viewAllAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }

        if (type != null && type.equalsIgnoreCase("burger"))
        {
            firestore.collection("AllProducts").whereEqualTo("type", "burger")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments())
                            {
                                ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                                viewAllModelList.add(viewAllModel);
                                viewAllAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
        if (type != null && type.equalsIgnoreCase("drink"))
        {
            firestore.collection("AllProducts").whereEqualTo("type", "drink")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments())
                            {
                                ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                                viewAllModelList.add(viewAllModel);
                                viewAllAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }
}