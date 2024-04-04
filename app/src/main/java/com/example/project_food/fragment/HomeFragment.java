package com.example.project_food.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_food.R;
import com.example.project_food.adapter.HomeAdapter;
import com.example.project_food.adapter.ProductAdapter;
import com.example.project_food.adapter.ViewAllAdapter;
import com.example.project_food.model.HomeCategory;
import com.example.project_food.model.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment
{
    FirebaseFirestore db;
    RecyclerView homeCateRec;
    RecyclerView homeProRec;

    // Home Category
    List<HomeCategory> categoryList;
    HomeAdapter homeAdapter;
    // Home Products
    List<ViewAllModel> productList;
    ProductAdapter productAdapter;

    //Search Items
    EditText search_box;
    private List<ViewAllModel> viewAllModelList;
    private RecyclerView recyclerViewSearch;
    private  ViewAllAdapter viewAllAdapter;

    public HomeFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(com.example.project_food.R.layout.fragment_home, container, false);

        db = FirebaseFirestore.getInstance();
        homeCateRec = rootview.findViewById(com.example.project_food.R.id.homeRecyclerViewOne);
        //Category items
        homeCateRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        categoryList = new ArrayList<>();
        homeAdapter = new HomeAdapter(getActivity(), categoryList);
        homeCateRec.setAdapter(homeAdapter);

        homeProRec = rootview.findViewById(R.id.homeRecyclerViewTwo);
        //Food items
        homeProRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(getActivity(), productList);
        homeProRec.setAdapter(productAdapter);


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
                                HomeCategory homeCategory = document.toObject(HomeCategory.class);
                                categoryList.add(homeCategory);
                                homeAdapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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
                                ViewAllModel homeProduct = document.toObject(ViewAllModel.class);
                                productList.add(homeProduct);
                                productAdapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        viewAllModelList = new ArrayList<>();
        viewAllAdapter = new ViewAllAdapter(getContext(),viewAllModelList);

        // Search View
        search_box = rootview.findViewById(R.id.search_box);
        recyclerViewSearch = rootview.findViewById(R.id.search_rec);

        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSearch.setAdapter(viewAllAdapter);
        recyclerViewSearch.setHasFixedSize(true);
        search_box.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.toString().isEmpty())
                {
                    viewAllModelList.clear();
                    viewAllAdapter.notifyDataSetChanged();
                }
                else
                {
                    searchProduct(s.toString());
                }
            }
        });
        return rootview;

    }

    private void searchProduct(String type)
    {
         if (!type.isEmpty())
         {
             db.collection("AllProducts").whereEqualTo("type", type).get()
                     .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                     {
                         @Override
                         public void onComplete(@NonNull Task<QuerySnapshot> task)
                         {
                             if (task.isSuccessful() && task.getResult() != null)
                             {
                                 viewAllModelList.clear();
                                 viewAllAdapter.notifyDataSetChanged();
                                 for (DocumentSnapshot doc : task.getResult().getDocuments())
                                 {
                                     ViewAllModel viewAllModel = doc.toObject(ViewAllModel.class);
                                     viewAllModelList.add(viewAllModel);
                                     viewAllAdapter.notifyDataSetChanged();
                                 }
                             }
                             else
                             {
                                 Toast.makeText(getContext(), "Không tồn tại sản phẩm!", Toast.LENGTH_SHORT).show();
                             }
                         }
                     });
         }
    }

    public void onViewCreated(View view, @NonNull Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

    }


}
