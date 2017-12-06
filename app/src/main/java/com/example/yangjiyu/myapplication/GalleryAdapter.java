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

    int[]ImgSignalNormal={R.drawable.ypbpr,
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
            R.drawable.clear_normal};

    public Vector<String> StringScene=new Vector<>();
    public Vector<String> StringSignal=new Vector<>();

    /*public String[] StringScene = {"整墙",
            "H-二分",
            "V-二分",
            "单屏",
            "自定义1",
            "自定义2",
            "清除",
            "确认"};//"Whole","H-2Parts","V-2Parts", "Single","Define1","Define2","Clear","Defined-Confirm"
    public String[] StringSignal={"1-DVI_1","1-DVI_2","1-HDMI","1-DP",
            "2-DVI_1","2-DVI_2","2-HDMI","2-DP",
            "3-DVI_1","3-DVI_2","3-HDMI","3-DP",
            "4-DVI_1","4-DVI_2","4-HDMI","4-DP",
            "清除"};*/
    private int mSceneNum=8;
    private int mSignalNum=12;

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

    public GalleryAdapter(Context context, List<Integer> data,Vector<String> Scene,Vector<String> Signal)
    {
        mInflater = LayoutInflater.from(context);
        mData = data;
        StringScene=Scene;
        StringSignal=Signal;
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
            }
            else {
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
        }
        else {
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
            if (mData.size()==mSceneNum)
            {
                viewHolder.mImg.setImageResource(ImgScenePressed[i]);
            }
            viewHolder.mTxt.setTextColor(Color.parseColor("#500000"));
        }
        else {
            if (mData.size()==mSceneNum)
            {
                viewHolder.mImg.setImageResource(ImgSceneNormal[i]);
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
            if (mData.size()==mSceneNum)
            {
                viewHolder.mImg.setImageResource(ImgScenePressed[i]);
            }
            viewHolder.mTxt.setTextColor(Color.parseColor("#500000"));
        }
        else {
            if (mData.size()==mSceneNum)
            {
                viewHolder.mImg.setImageResource(ImgSceneNormal[i]);
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
        }
        else {
            for (int ii = 0;ii<StringSignal.size();ii++)
            {
                isClicks.add(false);
            }
        }
        return mViewHolder;
    }
}
