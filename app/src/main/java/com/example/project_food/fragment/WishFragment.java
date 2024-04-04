package com.example.project_food.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project_food.R;
import com.example.project_food.adapter.FavoriteAdapter;
import com.example.project_food.model.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class WishFragment extends Fragment
{

    FirebaseFirestore db;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    FavoriteAdapter favAdapter;
    List<ViewAllModel> favModelList;


    public WishFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_wish, container, false);

        recyclerView=view.findViewById(R.id.recyclerviewFavorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        db =  FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        favModelList = new ArrayList<>();
        favAdapter = new FavoriteAdapter(getActivity(), favModelList);
        recyclerView.setAdapter(favAdapter);

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToWishlist").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments())
                            {
                                String documentId = documentSnapshot.getId();

                                ViewAllModel favModel = documentSnapshot.toObject(ViewAllModel.class);
                                favModel.setId(documentId);
                                favModelList.add(favModel);
                                favAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                });
        return view;

    }
}