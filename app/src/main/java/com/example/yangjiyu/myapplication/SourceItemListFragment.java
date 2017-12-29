package com.example.yangjiyu.myapplication;

import android.app.Activity;
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
import java.util.Vector;


public class SourceItemListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SourceItemListAdapter mAdapter;
    private List<Integer> mData;

    public Vector<String> StringSource=new Vector<>();
//    private OnFragmentInteractionListener mListener;



    public interface OnSourceListSelectedListener{
        void onSourceListSelected(int position);
    }
    private OnSourceListSelectedListener mOnSourceListSelectedListener;

    public void setOnSourceListSelectedListener(OnSourceListSelectedListener onSourceListSelectedListener) {
        mOnSourceListSelectedListener = onSourceListSelectedListener;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // 确认容器 Activity 已实现该回调接口。否则，抛出异常
        try {
            mOnSourceListSelectedListener = (OnSourceListSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSourceListSelectedListener");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StringSource.clear();
        StringSource.add(getString(R.string.power_list));
        StringSource.add(getString(R.string.scene_list));
        StringSource.add(getString(R.string.signal_list));
        StringSource.add(getString(R.string.model_SceneAndSignal));
        StringSource.add(getString(R.string.color_mode));
        StringSource.add(getString(R.string.getSystemInfo));
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

        mAdapter = new SourceItemListAdapter(StringSource);


        mAdapter.setOnSourceListClickListener(new SourceItemListAdapter.OnSourceListClick() {
            @Override
            public void onSourceListClick(View view, int index) {
                mOnSourceListSelectedListener.onSourceListSelected(index);
            }

            @Override
            public void onSourceListLongClick(View view, int index) {
                mOnSourceListSelectedListener.onSourceListSelected(index);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }


}
