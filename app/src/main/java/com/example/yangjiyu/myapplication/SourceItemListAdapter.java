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
 * Created by Howie on 2017/8/9.
 */

class SourceItemListAdapter extends RecyclerView.Adapter<SourceItemListAdapter.SourceItemListViewHolder>{

    int[] ImageSourceId = {R.drawable.poweron_normal,R.drawable.scene, R.drawable.signal,R.drawable.model_normal,R.drawable.color_mode_normal,R.drawable.get_system_info_normal};

    int[] ImageClicked={R.drawable.poweron_pressdown,R.drawable.scene_press,R.drawable.signal_press,R.drawable.model_pressdown,R.drawable.color_mode_pressed,R.drawable.get_system_info_pressdown};

    //public String[] StringSource = {"场景布局列表", "信号源列表"};//R.string.scene_list   R.string.signal_list
    public Vector<String> StringSource=new Vector<>();
    private List<Boolean> isClicks =new ArrayList<>();

    public interface OnSourceListClick{
        void onSourceListClick(View view, int index);
        void onSourceListLongClick(View view, int index);
    }
    private OnSourceListClick mOnSourceListClick;

    public void setOnSourceListClickListener( OnSourceListClick onSourceListClickListener){
        this.mOnSourceListClick = onSourceListClickListener;
    }

    public SourceItemListAdapter(Vector<String> source){
        this.StringSource=source;
    }

    @Override
    public SourceItemListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int LayoutIdForSourceListItem = R.layout.video_source_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        for (int i = 0;i<StringSource.size();i++)
        {
            isClicks.add(false);
        }
        View view = inflater.inflate( LayoutIdForSourceListItem, parent, shouldAttachToParentImmediately);
        SourceItemListViewHolder viewHolder = new SourceItemListViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SourceItemListViewHolder holder, final int position) {
        holder.bind(position);
        if (isClicks.get(position))
        {
            holder.mTextViewSourceList.setTextColor(Color.parseColor("#500000"));
            holder.mImageViewSourceList.setImageResource(ImageClicked[position]);
            holder.itemView.setBackgroundResource(R.drawable.item_pressed);
        }
        else {
            holder.mTextViewSourceList.setTextColor(Color.parseColor("#ffffff"));
            holder.mImageViewSourceList.setImageResource(ImageSourceId[position]);
            holder.itemView.setBackgroundResource(R.drawable.item_normal);
        }

        if( mOnSourceListClick != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i=0;i<isClicks.size();i++)
                    {
                        isClicks.set(i,false);
                    }
                    isClicks.set(position, true);
                    notifyDataSetChanged();
                    mOnSourceListClick.onSourceListClick(holder.itemView, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnSourceListClick.onSourceListLongClick(holder.itemView, position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
//        return ImageSourceId.length > StringSource.length
//                ? StringSource.length
//                : ImageSourceId.length;
        return StringSource.size();
    }

    class SourceItemListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mImageViewSourceList;
        private TextView mTextViewSourceList;

        public SourceItemListViewHolder(View itemView) {
            super(itemView);
            mImageViewSourceList = (ImageView)itemView.findViewById(R.id.iv_source_list);
            mTextViewSourceList  = (TextView) itemView.findViewById(R.id.tv_source_list);
        }

        public void bind(int sourceId){
            mImageViewSourceList.setImageResource(ImageSourceId[sourceId]);
            mTextViewSourceList.setText(StringSource.get(sourceId));
        }

        public String getItemName(int nameId){
            return StringSource.get(nameId);
        }

        @Override
        public void onClick(View v) {
            int adapter_position = getAdapterPosition();
        }
    }
}
