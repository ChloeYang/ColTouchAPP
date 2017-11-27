package com.example.yangjiyu.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class SourceItemListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SourceItemListAdapter mAdapter;
    private List<Integer> mData;

//    private OnFragmentInteractionListener mListener;

    public SourceItemListFragment() {
        // Required empty public constructor
    }


    public interface OnSourceListSelectedListener{
        void onSourceListSelected(int position);
    }
    private OnSourceListSelectedListener mOnSourceListSelectedListener;

    public void setOnSourceListSelectedListener(OnSourceListSelectedListener onSourceListSelectedListener) {
        mOnSourceListSelectedListener = onSourceListSelectedListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_source_item_list, container, false);
        View v = inflater.inflate(R.layout.fragment_source_item_list, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_source_list);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(v.getContext());
        //mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new SourceItemListAdapter();


        mAdapter.setOnSourceListClickListener(new SourceItemListAdapter.OnSourceListClick() {
            @Override
            public void onSourceListClick(View view, int index) {
                
            }

            @Override
            public void onSourceListLongClick(View view, int index) {

            }
        });
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }


}
