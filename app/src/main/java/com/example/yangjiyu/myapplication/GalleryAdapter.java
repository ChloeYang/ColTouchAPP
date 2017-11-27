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

/**
 * Created by yangjiyu on 2017/11/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>
{
    String[] StringScenee= {"Whole","H-2Parts","V-2Parts", "Single"};
    String[] StringSignal={"1-YPbPr","1-VIDEO","1-SDI","1-VLINK",
            "2-YPbPr","2-VIDEO","2-SDI","2-VLINK",
            "3-YPbPr","3-VIDEO","3-SDI","3-VLINK",
            "4-YPbPr","4-VIDEO","4-SDI","4-VLINK"};

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

    public GalleryAdapter(Context context, List<Integer> data)
    {
        mInflater = LayoutInflater.from(context);
        mData = data;
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
            if(mData.size()==4){
                mTxt.setText(StringScenee[sourceId]);
            }
            else {
                mTxt.setText(StringSignal[sourceId]);
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
        ViewHolder viewHolder = new ViewHolder(view);

        if (mData.size()==4){
            for (int ii = 0;i<StringScenee.length;i++)
            {
                isClicks.add(false);
            }
        }
        else {
            for (int ii = 0;i<StringSignal.length;i++)
            {
                isClicks.add(false);
            }
        }

        viewHolder.mImg = (ImageView) view
                .findViewById(R.id.id_index_gallery_item_image);
        viewHolder.mTxt = (TextView)view.findViewById(R.id.id_index_gallery_item_text);
        return viewHolder;
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
            viewHolder.mTxt.setTextColor(Color.parseColor("#500000"));
        }
        else {
            viewHolder.mTxt.setTextColor(Color.parseColor("#000000"));
        }
        viewHolder.mImg.setImageResource(mData.get(i));
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

}
