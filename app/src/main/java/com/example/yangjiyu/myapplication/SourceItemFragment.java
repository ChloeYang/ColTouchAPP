package com.example.yangjiyu.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class SourceItemFragment extends Fragment /*implements SourceItemListFragment.OnSourceListSelectedListener*/{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    public List<Integer> mData;
    private TextView textView;
    private View view;

    public Vector<String> Scene=new Vector<>();
    public Vector<String> Signal=new Vector<>();
    public Vector<String> Power=new Vector<>();
    public Vector<String> SystemInfo= new Vector<>();
    public Vector<String> ModelInfo= new Vector<>();
    public Vector<String> ColorMode= new Vector<>();
    int  nowIndex=0;

    //String[] StringSource = {"Whole","H-2Parts","V-2Parts", "Single"};


    public void onShow(int pos) {
        nowIndex= pos;
        //Toast.makeText(getActivity(),"pos="+pos,Toast.LENGTH_SHORT).show();
        getNewData(pos);
        mAdapter.initViewHolder();
        mAdapter.notifyDataSetChanged();

    }
    void getNewData(int pos){
        if (pos==2){
            sceneData();
            textView.setText(getString(R.string.scene_list));
            mAdapter.bIsSystemData=false;
            mAdapter.bIsPowerOnOff=false;
        }else if (pos==3){
            signalData();
            textView.setText(getString(R.string.signal_list));
            mAdapter.bIsSystemData=false;
            mAdapter.bIsPowerOnOff=false;
        }else if(pos==0){
            powerData();
            textView.setText(getString(R.string.power_list));
            mAdapter.bIsSystemData=false;
            mAdapter.bIsPowerOnOff=true;
        }else if(pos==5){
            checkSystemData();
            textView.setText(getString(R.string.getSystemInfo));
            mAdapter.bIsSystemData=true;
            mAdapter.bIsPowerOnOff=false;
        }else if(pos==1){
            checkModelData();
            textView.setText(getString(R.string.model_SceneAndSignal));
            mAdapter.bIsSystemData=false;
            mAdapter.bIsPowerOnOff=false;
        } else if(pos==4){
            checkColorModeData();
            textView.setText(getString(R.string.color_mode));
            mAdapter.bIsSystemData=false;
            mAdapter.bIsPowerOnOff=false;
        }
    }

    void setCurrentItem(int pos){
        mAdapter.onSetCurrent(mAdapter.mViewHolder,pos);
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
        mAdapter = new GalleryAdapter(view.getContext(), mData,Scene,Signal,Power,SystemInfo,ModelInfo,ColorMode);
        mAdapter.setOnItemClickListener(new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(view.getContext(),position+"",Toast.LENGTH_SHORT).show();
                mOnSourceSelectedListener.onSourceSelected(position);
            }
            public void onItemLongClick(View view, int position,int type){
                DialogRename dialogRename=new DialogRename(getContext(),type);
                dialogRename.Rename(type,position);
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
                R.drawable.scene_1_normal,
                R.drawable.scene_2_normal,
                R.drawable.scene_3_normal,
                R.drawable.scene_4_normal,
                R.drawable.scene,
                R.drawable.scene,
                R.drawable.clear_normal,
                R.drawable.scene_confirm));

        //mAdapter.StringScene.clear();
        Scene.add(getString(R.string.scene_whole));
        Scene.add(getString(R.string.scene_h2part));
        Scene.add(getString(R.string.scene_v2part));
        Scene.add(getString(R.string.scene_each));
        Scene.add(getString(R.string.scene_define1));
        Scene.add(getString(R.string.scene_define2));
        Scene.add(getString(R.string.clear));
        Scene.add(getString(R.string.confirm));

        Signal.add(getString(R.string.DVI_1_1));
        Signal.add(getString(R.string.DVI_1_2));
        Signal.add(getString(R.string.HDMI_1));
        Signal.add(getString(R.string.DP_1));
        Signal.add(getString(R.string.DVI_2_1));
        Signal.add(getString(R.string.DVI_2_2));
        Signal.add(getString(R.string.HDMI_2));
        Signal.add(getString(R.string.DP_2));
        Signal.add(getString(R.string.DVI_3_1));
        Signal.add(getString(R.string.DVI_3_2));
        Signal.add(getString(R.string.HDMI_3));
        Signal.add(getString(R.string.DP_3));
        Signal.add(getString(R.string.DVI_4_1));
        Signal.add(getString(R.string.DVI_4_2));
        Signal.add(getString(R.string.HDMI_4));
        Signal.add(getString(R.string.DP_4));
        Signal.add(getString(R.string.clear));

        Power.add(getString(R.string.power_on));
        Power.add(getString(R.string.power_off));

        SystemInfo.add(getString(R.string.get_interface_version));
        SystemInfo.add(getString(R.string.get_interface_status));
        SystemInfo.add(getString(R.string.get_engine_status));

        ModelInfo.add(getString(R.string.model_define1));
        ModelInfo.add(getString(R.string.model_define2));
        ModelInfo.add(getString(R.string.model_save));

        ColorMode.add(getString(R.string.color_mode)+1);
        ColorMode.add(getString(R.string.color_mode)+2);
        /*ColorMode.add(getString(R.string.color_mode)+3);
        ColorMode.add(getString(R.string.color_mode)+4);
        ColorMode.add(getString(R.string.color_mode)+5);
        ColorMode.add(getString(R.string.color_mode)+6);*/
    }
    private void sceneData()
    {
        mAdapter.mData = new ArrayList<>(Arrays.asList(
                R.drawable.scene_1_normal,
                R.drawable.scene_2_normal,
                R.drawable.scene_3_normal,
                R.drawable.scene_4_normal,
                R.drawable.scene,
                R.drawable.scene,
                R.drawable.clear_normal,
                R.drawable.scene_confirm));
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
                R.drawable.vlink,
                R.drawable.clear_normal));

    }
    private void powerData()
    {
        mAdapter.mData = new ArrayList<>(Arrays.asList(
                R.drawable.poweron_normal,
                R.drawable.poweroff_normal));

    }
    private void checkSystemData(){
        mAdapter.mData = new ArrayList<>(Arrays.asList(
                R.drawable.get_system_info_normal,
                R.drawable.get_system_info_normal,
                R.drawable.get_system_info_normal));
    }

    private void checkModelData(){
        mAdapter.mData = new ArrayList<>(Arrays.asList(
                R.drawable.model_normal,
                R.drawable.model_normal,
                R.drawable.scene_confirm));
    }
    private void checkColorModeData(){
        mAdapter.mData = new ArrayList<>(Arrays.asList(
                /*R.drawable.color_mode_normal,
                R.drawable.color_mode_normal,
                R.drawable.color_mode_normal,
                R.drawable.color_mode_normal,*/
                R.drawable.color_mode_normal,
                R.drawable.color_mode_normal));
    }
}
