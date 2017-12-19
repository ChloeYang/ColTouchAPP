package com.example.yangjiyu.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by yangjiyu on 2017/11/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>
{
    public boolean bIsSystemData=false;
    int[] ImgSceneNormal = {R.drawable.scene_1_normal,
            R.drawable.scene_2_normal,
            R.drawable.scene_3_normal,
            R.drawable.scene_4_normal,
            R.drawable.scene,
            R.drawable.scene,
            R.drawable.clear_normal,
            R.drawable.scene_confirm};
    int[] ImgScenePressed = {R.drawable.scene_1_pressed,
            R.drawable.scene_2_pressed,
            R.drawable.scene_3_pressed,
            R.drawable.scene_4_pressed,
            R.drawable.scene_press,
            R.drawable.scene_press,
            R.drawable.clear_pressdown,
            R.drawable.scene_confirm};

    int[]ImgSignalNormal={R.drawable.dvi1_normal,
            R.drawable.dvi2_normal,
            R.drawable.hdmi_normal,
            R.drawable.dp_normal,
            R.drawable.dvi1_normal,
            R.drawable.dvi2_normal,
            R.drawable.hdmi_normal,
            R.drawable.dp_normal,
            R.drawable.dvi1_normal,
            R.drawable.dvi2_normal,
            R.drawable.hdmi_normal,
            R.drawable.dp_normal,
            R.drawable.dvi1_normal,
            R.drawable.dvi2_normal,
            R.drawable.hdmi_normal,
            R.drawable.dp_normal,
            R.drawable.clear_normal};
    int[]ImgSignalPressed={R.drawable.dvi1_pressed,
            R.drawable.dvi2_pressed,
            R.drawable.hdmi_pressed,
            R.drawable.dp_pressed,
            R.drawable.dvi1_pressed,
            R.drawable.dvi2_pressed,
            R.drawable.hdmi_pressed,
            R.drawable.dp_pressed,
            R.drawable.dvi1_pressed,
            R.drawable.dvi2_pressed,
            R.drawable.hdmi_pressed,
            R.drawable.dp_pressed,
            R.drawable.dvi1_pressed,
            R.drawable.dvi2_pressed,
            R.drawable.hdmi_pressed,
            R.drawable.dp_pressed,
            R.drawable.clear_pressdown,};
    int[]ImgSignalOffline={R.drawable.dvi1_offline,
            R.drawable.dvi2_offline,
            R.drawable.hdmi_offline,
            R.drawable.dp_offline,
            R.drawable.dvi1_offline,
            R.drawable.dvi2_offline,
            R.drawable.hdmi_offline,
            R.drawable.dp_offline,
            R.drawable.dvi1_offline,
            R.drawable.dvi2_offline,
            R.drawable.hdmi_offline,
            R.drawable.dp_offline,
            R.drawable.dvi1_offline,
            R.drawable.dvi2_offline,
            R.drawable.hdmi_offline,
            R.drawable.dp_offline,
            R.drawable.clear_normal};
    int[] ImgPowerNormal = {R.drawable.poweron_normal,
            R.drawable.poweroff_normal};
    int[] ImgPowerPressed = {R.drawable.poweron_pressdown,
            R.drawable.poweroff_pressdown};

    int[] ImgSystemInfoNormal = {R.drawable.get_system_info_normal,R.drawable.get_system_info_normal};
    int[] ImgSystemInfoPressed = {R.drawable.get_system_info_pressdown,R.drawable.get_system_info_pressdown};

    public Vector<String> StringScene=new Vector<>();
    public Vector<String> StringSignal=new Vector<>();
    public Vector<String> StringPower=new Vector<>();
    public Vector<String> StringSystemInfo =new Vector<>();

    private int mSceneNum=8;
    private int mSignalNum=17;
    private int mPowerNum=2;
    private int mSystemInfoNum =2;

    public ViewHolder mViewHolder;
    private List<Boolean> isClicks =new ArrayList<>();

    public interface OnItemClickListener
    {
        void onItemClick(View view,int position);
    }
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    private android.view.LayoutInflater mInflater;
    public java.util.List<Integer> mData;
    private SharedAppData sharedAppData;

    public GalleryAdapter(Context context, List<Integer> data,Vector<String> Scene,Vector<String> Signal,Vector<String> Power,Vector<String>SystemInfo)
    {
        sharedAppData=SharedAppData.newInstance(context);
        mInflater = LayoutInflater.from(context);
        mData = data;
        StringScene=Scene;
        StringSignal=Signal;
        StringPower=Power;
        StringSystemInfo =SystemInfo;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView mImg;
        TextView mTxt;
        public ViewHolder(View arg0)
        {
            super(arg0);
            mTxt  = (TextView) itemView.findViewById(R.id.id_index_gallery_item_text);
        }

        public void bind(int sourceId){
            //mImageViewSourceList.setImageResource(ImageSourceId[sourceId]);
            if(mData.size()==mSceneNum){
                mImg.setImageResource(ImgSceneNormal[sourceId]);
                mTxt.setText(StringScene.get(sourceId));
            }else if (mData.size()==mPowerNum && bIsSystemData==false){
                mImg.setImageResource(ImgPowerNormal[sourceId]);
                mTxt.setText(StringPower.get(sourceId));
            }else if (mData.size()== mSystemInfoNum && bIsSystemData==true){
                mImg.setImageResource(ImgSystemInfoNormal[sourceId]);
                mTxt.setText(StringSystemInfo.get(sourceId));
            }else /*if(mData.size()==mSignalNum)*/{
                if (sharedAppData.getSignalFlag(sourceId)==1 || sourceId==(mSignalNum-1)) {
                    mImg.setImageResource(ImgSignalNormal[sourceId]);
                    mTxt.setText(StringSignal.get(sourceId));
                    mTxt.setTextColor(Color.parseColor("#000000"));
                }else {
                    mImg.setImageResource(ImgSignalOffline[sourceId]);
                    mTxt.setText(StringSignal.get(sourceId));
                    mTxt.setTextColor(Color.parseColor("#5e5e5e"));
                }
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = mInflater.inflate(R.layout.itemlayout,
                viewGroup, false);
        mViewHolder = new ViewHolder(view);

        if (mData.size()==mSceneNum){
            for (int ii = 0; ii< StringScene.size(); ii++)
            {
                isClicks.add(false);
            }
        }else if (mData.size()==mPowerNum && bIsSystemData==false){
            for (int ii = 0;ii<StringPower.size();ii++)
            {
                isClicks.add(false);
            }
        }else if (mData.size()==mSystemInfoNum && bIsSystemData==true) {
            for (int ii = 0;ii<StringSystemInfo.size();ii++)
            {
                isClicks.add(false);
            }
        }else/*if (mData.size()==mSignalNum)*/{
            for (int ii = 0;ii<StringSignal.size();ii++)
            {
                isClicks.add(false);
            }
        }

        mViewHolder.mImg = (ImageView) view
                .findViewById(R.id.id_index_gallery_item_image);
        mViewHolder.mTxt = (TextView)view.findViewById(R.id.id_index_gallery_item_text);
        return mViewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
        viewHolder.bind(i);
        if (isClicks.get(i))
        {
            if (mData.size()==mSceneNum){
                viewHolder.mImg.setImageResource(ImgScenePressed[i]);
                viewHolder.mTxt.setTextColor(Color.parseColor("#500000"));
            }else if (mData.size()==mPowerNum && bIsSystemData==false){
                viewHolder.mImg.setImageResource(ImgPowerPressed[i]);
                viewHolder.mTxt.setTextColor(Color.parseColor("#500000"));
            }else if (mData.size()== mSystemInfoNum && bIsSystemData==true){
                viewHolder.mImg.setImageResource(ImgSystemInfoPressed[i]);
                viewHolder.mTxt.setTextColor(Color.parseColor("#500000"));
            }else /*if (mData.size()==mSignalNum)*/{
                if (sharedAppData.getSignalFlag(i)==1 || i==(mSignalNum-1)) {
                    viewHolder.mImg.setImageResource(ImgSignalPressed[i]);
                    viewHolder.mTxt.setTextColor(Color.parseColor("#500000"));
                }else {
                }
            }
        }
        else {
            if (mData.size()==mSceneNum){
                viewHolder.mImg.setImageResource(ImgSceneNormal[i]);
                viewHolder.mTxt.setTextColor(Color.parseColor("#000000"));
            }else if (mData.size()==mPowerNum && bIsSystemData==false){
                viewHolder.mImg.setImageResource(ImgPowerNormal[i]);
                viewHolder.mTxt.setTextColor(Color.parseColor("#000000"));
            }else if (mData.size()== mSystemInfoNum && bIsSystemData==true){
                viewHolder.mImg.setImageResource(ImgSystemInfoNormal[i]);
                viewHolder.mTxt.setTextColor(Color.parseColor("#000000"));
            }else/* if (mData.size()==mSignalNum)*/{
                if (sharedAppData.getSignalFlag(i)==1 || i==(mSignalNum-1)) {
                    viewHolder.mImg.setImageResource(ImgSignalNormal[i]);
                    viewHolder.mTxt.setTextColor(Color.parseColor("#000000"));
                }else {
                }
            }
        }

        //viewHolder.mImg.setImageResource(mData.get(i));//img unbind
        //// TODO: 2017/11/17 listener
        if (mOnItemClickListener!=null)
        {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i=0;i<isClicks.size();i++)
                    {
                        isClicks.set(i,false);
                    }
                    isClicks.set(i, true);
                    notifyDataSetChanged();
                    mOnItemClickListener.onItemClick(viewHolder.itemView,i);
                }
            });
        }
    }

    public void onSetCurrent(final ViewHolder viewHolder, final int i)
    {
        viewHolder.bind(i);
        if (isClicks.get(i))
        {
            if (mData.size()==mSceneNum){
                viewHolder.mImg.setImageResource(ImgScenePressed[i]);
                viewHolder.mTxt.setTextColor(Color.parseColor("#500000"));
            }else if (mData.size()==mPowerNum && bIsSystemData==false){
                viewHolder.mImg.setImageResource(ImgPowerPressed[i]);
                viewHolder.mTxt.setTextColor(Color.parseColor("#500000"));
            }else if (mData.size()== mSystemInfoNum && bIsSystemData==true){
                viewHolder.mImg.setImageResource(ImgSystemInfoPressed[i]);
                viewHolder.mTxt.setTextColor(Color.parseColor("#500000"));
            }else /*if (mData.size()==mSignalNum)*/{
                if (sharedAppData.getSignalFlag(i)==1|| i==(mSignalNum-1)) {
                    viewHolder.mImg.setImageResource(ImgSignalPressed[i]);
                    viewHolder.mTxt.setTextColor(Color.parseColor("#500000"));
                }else {
                }
            }
        }
        else {
            if (mData.size()==mSceneNum){
                viewHolder.mImg.setImageResource(ImgSceneNormal[i]);
                viewHolder.mTxt.setTextColor(Color.parseColor("#000000"));
            }else if (mData.size()==mPowerNum && bIsSystemData==false){
                viewHolder.mImg.setImageResource(ImgPowerNormal[i]);
                viewHolder.mTxt.setTextColor(Color.parseColor("#000000"));
            }else if (mData.size()== mSystemInfoNum && bIsSystemData==true){
                viewHolder.mImg.setImageResource(ImgSystemInfoNormal[i]);
                viewHolder.mTxt.setTextColor(Color.parseColor("#000000"));
            }else /*if (mData.size()==mSignalNum)*/{
                if (sharedAppData.getSignalFlag(i)==1|| i==(mSignalNum-1)) {
                    viewHolder.mImg.setImageResource(ImgSignalPressed[i]);
                    viewHolder.mTxt.setTextColor(Color.parseColor("#000000"));
                }else {
                }
            }
        }
        //viewHolder.mImg.setImageResource(mData.get(i));//img unbind
        //// TODO: 2017/11/17 listener

        for (int ii=0;ii<isClicks.size();ii++)
        {
            isClicks.set(ii,false);
        }
        isClicks.set(i, true);
        notifyDataSetChanged();
        mOnItemClickListener.onItemClick(viewHolder.itemView,i);

    }

    public ViewHolder initViewHolder()
    {
        isClicks.clear();
        if (mData.size()==mSceneNum){
            for (int ii = 0; ii< StringScene.size(); ii++)
            {
                isClicks.add(false);
            }
        }else if (mData.size()==mPowerNum && bIsSystemData==false){
            for (int ii = 0;ii<StringPower.size();ii++)
            {
                isClicks.add(false);
            }
        }else if (mData.size()==mSystemInfoNum && bIsSystemData==true){
            for (int ii = 0;ii<StringSystemInfo.size();ii++)
            {
                isClicks.add(false);
            }
        }else  /*if (mData.size()==mSignalNum)*/{
            for (int ii = 0;ii<StringSignal.size();ii++)
            {
                isClicks.add(false);
            }
        }
        return mViewHolder;
    }
}
