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

public class SourceItemFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    private List<Integer> mData;


    //private OnFragmentInteractionListener mListener;
    public interface OnSourceSelectedListener{
        void onSourceSelected(int position);
    }
    private SourceItemFragment.OnSourceSelectedListener mOnSourceSelectedListener;

    public void setOnSourceListSelectedListener(SourceItemFragment.OnSourceSelectedListener onSourceSelectedListener) {
        mOnSourceSelectedListener = onSourceSelectedListener;
    }

    public SourceItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_source_item, container, false);
        initData();
        //得到控件
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_source_item);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        //--GridLayoutManager gridLayoutManager = new GridLayoutManager(this,9);

        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(linearLayoutManager);//--gridLayoutManager
        //设置适配器
        mAdapter = new GalleryAdapter(view.getContext(), mData);
        mAdapter.setOnItemClickListener(new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(view.getContext(),position+"",Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView.setAdapter(mAdapter);
/*        mRecyclerView.setOnScrollChangeListener(new setOnItemScrollChangeListener()
        {
            @Override
            public void onChange(View view, int position)
            {
                mImg.setImageResource(mData.get(position));
            };
        });*/
        return view;
        //return inflater.inflate(R.layout.fragment_source_item, container, false);
    }
    private void initData()
    {
        mData = new ArrayList<>(Arrays.asList(R.drawable.a,
                R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e,
                R.drawable.f, R.drawable.g, R.drawable.h, R.drawable.l));
    }

}
