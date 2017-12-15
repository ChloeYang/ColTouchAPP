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

    public Vector<String> StringScene=new Vector<>();
    public Vector<String> StringSignal=new Vector<>();
    public Vector<String> StringPower=new Vector<>();

    private int mSceneNum=8;
    private int mSignalNum=12;
    private int mPowerNum=2;

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

    public GalleryAdapter(Context context, List<Integer> data,Vector<String> Scene,Vector<String> Signal,Vector<String> Power)
    {
        mInflater = LayoutInflater.from(context);
        mData = data;
        StringScene=Scene;
        StringSignal=Signal;
        StringPower=Power;
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
            }else if (mData.size()==mPowerNum){
                mImg.setImageResource(ImgPowerNormal[sourceId]);
                mTxt.setText(StringPower.get(sourceId));
            }else /*if(mData.size()==mSignalNum)*/{
                mImg.setImageResource(ImgSignalNormal[sourceId]);
                mTxt.setText(StringSignal.get(sourceId));
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
        }else if (mData.size()==mPowerNum){
            for (int ii = 0;ii<StringPower.size();ii++)
            {
                isClicks.add(false);
            }
        }else /*if (mData.size()==mSignalNum)*/{
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
            }else if (mData.size()==mPowerNum){
                viewHolder.mImg.setImageResource(ImgPowerPressed[i]);
            }else /*if (mData.size()==mSignalNum)*/{
                viewHolder.mImg.setImageResource(ImgSignalPressed[i]);
            }

            viewHolder.mTxt.setTextColor(Color.parseColor("#500000"));
        }
        else {
            if (mData.size()==mSceneNum){
                viewHolder.mImg.setImageResource(ImgSceneNormal[i]);
            }else if (mData.size()==mPowerNum){
                viewHolder.mImg.setImageResource(ImgPowerNormal[i]);
            }else/* if (mData.size()==mSignalNum)*/{
                viewHolder.mImg.setImageResource(ImgSignalNormal[i]);
            }
            viewHolder.mTxt.setTextColor(Color.parseColor("#000000"));
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
            }else if (mData.size()==mPowerNum){
                viewHolder.mImg.setImageResource(ImgPowerPressed[i]);
            }else /*if (mData.size()==mSignalNum)*/{
                viewHolder.mImg.setImageResource(ImgSignalPressed[i]);
            }
            viewHolder.mTxt.setTextColor(Color.parseColor("#500000"));
        }
        else {
            if (mData.size()==mSceneNum){
                viewHolder.mImg.setImageResource(ImgSceneNormal[i]);
            }else if (mData.size()==mPowerNum){
                viewHolder.mImg.setImageResource(ImgPowerNormal[i]);
            }else /*if (mData.size()==mSignalNum)*/{
                viewHolder.mImg.setImageResource(ImgSignalNormal[i]);
            }
            viewHolder.mTxt.setTextColor(Color.parseColor("#000000"));
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
        }else if (mData.size()==mPowerNum){
            for (int ii = 0;ii<StringPower.size();ii++)
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
