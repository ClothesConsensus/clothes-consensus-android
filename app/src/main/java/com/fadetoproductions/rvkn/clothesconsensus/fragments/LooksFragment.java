//package com.fadetoproductions.rvkn.clothesconsensus.fragments;
//
//import android.databinding.DataBindingUtil;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.fadetoproductions.rvkn.clothesconsensus.R;
//import com.fadetoproductions.rvkn.clothesconsensus.adapter.LooksAdapter;
//import com.fadetoproductions.rvkn.clothesconsensus.databinding.LooksFragmentBinding;
//import com.fadetoproductions.rvkn.clothesconsensus.models.Look;
//import com.fadetoproductions.rvkn.clothesconsensus.utils.DividerItemDecoration;
//import com.fadetoproductions.rvkn.clothesconsensus.utils.EndlessRecyclerViewScrollListener;
//import com.fadetoproductions.rvkn.clothesconsensus.utils.ItemClickSupport;
//
//import java.util.ArrayList;
//
///**
// * Created by sdass on 9/2/16.
// */
//
//public class LooksFragment extends Fragment {
//    // The onCreateView method is called when Fragment should create its View object hierarchy,
//    // either dynamically or via XML layout inflation.
//    LooksFragmentBinding looksFragmentBinding;
//    RecyclerView rvLooks;
//    protected ArrayList<Look> looks;
//    LooksAdapter lookAdapter;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        rvLooks = looksFragmentBinding.rvLooks;
//        lookAdapter = new LooksAdapter(getActivity(), looks, getActivity().getSupportFragmentManager() );
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
//        // Defines the xml file for the fragment
//        looksFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.looks_fragment, parent, false);
//        rvLooks.setAdapter(lookAdapter);
//        return looksFragmentBinding.getRoot();
//    }
//
//    // This event is triggered soon after onCreateView().
//    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        // Setup any handles to view objects here
//        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
//        rvLooks.setLayoutManager(linearLayoutManager);
//        RecyclerView.ItemDecoration itemDecoration = new
//                DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL_LIST);
//        rvLooks.addItemDecoration(itemDecoration);
//        ItemClickSupport.addTo(rvLooks).setOnItemClickListener(
//                new ItemClickSupport.OnItemClickListener() {
//                    @Override
//                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//
//                    }
//                }
//        );
//        rvLooks.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//                // Triggered only when new data needs to be appended to the list
//                // Add whatever code is needed to append new items to the bottom of the list
////                customLoadMoreDataFromApi(page);
//            }
//        });
//
//
//
//    }
//
//
//
//    protected void fillUpAdapterWithData(ArrayList<Look> looks) {
//        lookAdapter.addAll(looks);
//    }
//
//
//}