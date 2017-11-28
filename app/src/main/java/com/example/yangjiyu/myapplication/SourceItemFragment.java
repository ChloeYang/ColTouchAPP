package com.example.yangjiyu.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SourceItemFragment extends Fragment /*implements SourceItemListFragment.OnSourceListSelectedListener*/{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    public List<Integer> mData;
    private TextView textView;
    private View view;

    int  nowIndex=0;

    //String[] StringSource = {"Whole","H-2Parts","V-2Parts", "Single"};


    public void onShow(int pos) {
        nowIndex= pos;
        //Toast.makeText(getActivity(),"pos="+pos,Toast.LENGTH_SHORT).show();
        getNewData(pos);
        mAdapter.notifyDataSetChanged();

    }
    void getNewData(int pos){
        if (pos==0)
        {
            sceneData();
            textView.setText("SceneList");
        }
        else {
            signalData();
            textView.setText("SignalList");
        }
    }


    //private OnFragmentInteractionListener mListener;
    public interface OnSourceSelectedListener{
        void onSourceSelected(int position);
    }
    private SourceItemFragment.OnSourceSelectedListener mOnSourceSelectedListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // 确认容器 Activity 已实现该回调接口。否则，抛出异常
        try {
            mOnSourceSelectedListener = (SourceItemFragment.OnSourceSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSourceListSelectedListener");
        }
    }
    public void setOnSourceSelectedListener(SourceItemFragment.OnSourceSelectedListener onSourceSelectedListener) {
        mOnSourceSelectedListener = onSourceSelectedListener;
    }

    public SourceItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_source_item, container, false);
        //得到控件
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_source_item);
        textView=(TextView)view.findViewById(R.id.source_item) ;
        //设置布局管理器
        //--LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(),4);

        //--linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(gridLayoutManager);//--linearLayoutManager
        //设置适配器
        mAdapter = new GalleryAdapter(view.getContext(), mData);
        mAdapter.setOnItemClickListener(new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(view.getContext(),position+"",Toast.LENGTH_SHORT).show();
                mOnSourceSelectedListener.onSourceSelected(position);
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
        mData = new ArrayList<>(Arrays.asList(
                R.drawable.scene_1,
                R.drawable.scene_2,
                R.drawable.scene_3,
                R.drawable.scene_4));
    }
    private void sceneData()
    {
        mAdapter.mData = new ArrayList<>(Arrays.asList(
                R.drawable.scene_1,
                R.drawable.scene_2,
                R.drawable.scene_3,
                R.drawable.scene_4));
    }
    private void signalData()
    {
        mAdapter.mData = new ArrayList<>(Arrays.asList(
                R.drawable.ypbpr,
                R.drawable.video,
                R.drawable.sdi,
                R.drawable.vlink,
                R.drawable.ypbpr,
                R.drawable.video,
                R.drawable.sdi,
                R.drawable.vlink,
                R.drawable.ypbpr,
                R.drawable.video,
                R.drawable.sdi,
                R.drawable.vlink,
                R.drawable.ypbpr,
                R.drawable.video,
                R.drawable.sdi,
                R.drawable.vlink));
    }
}
