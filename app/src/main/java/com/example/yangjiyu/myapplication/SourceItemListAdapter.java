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
 * Created by Howie on 2017/8/9.
 */

class SourceItemListAdapter extends RecyclerView.Adapter<SourceItemListAdapter.SourceItemListViewHolder>{

    int[] ImageSourceId = {R.drawable.scene, R.drawable.signal};

    int[] ImageClicked={R.drawable.scene_press,R.drawable.signal_press};

    String[] StringSource = {"Scene", "Signal"};
    private List<Boolean> isClicks =new ArrayList<>();

    public interface OnSourceListClick{
        void onSourceListClick(View view, int index);
        void onSourceListLongClick(View view, int index);
    }
    private OnSourceListClick mOnSourceListClick;

    public void setOnSourceListClickListener( OnSourceListClick onSourceListClickListener){
        this.mOnSourceListClick = onSourceListClickListener;
    }


    @Override
    public SourceItemListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int LayoutIdForSourceListItem = R.layout.video_source_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        for (int i = 0;i<StringSource.length;i++)
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
        }
        else {
            holder.mTextViewSourceList.setTextColor(Color.parseColor("#000000"));
            holder.mImageViewSourceList.setImageResource(ImageSourceId[position]);
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
        return StringSource.length;
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
            mTextViewSourceList.setText(StringSource[sourceId]);
        }

        public String getItemName(int nameId){
            return StringSource[nameId];
        }

        @Override
        public void onClick(View v) {
            int adapter_position = getAdapterPosition();
        }
    }
}
