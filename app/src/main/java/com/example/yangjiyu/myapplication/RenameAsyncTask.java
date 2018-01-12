package com.example.yangjiyu.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import java.util.Vector;

/**
 * Created by yangjiyu on 2018/1/10.
 */

public class RenameAsyncTask extends AsyncTask {

    Context mContext;
    int mType=0;
    int mPosition=0;
    View mView;
    SourceItemFragment sourceItemFragment;

    public RenameAsyncTask(SourceItemFragment frag,Context context, View view,int type, int positon){
        mContext=context;
        mType=type;
        mPosition=positon;
        mView=view;
        sourceItemFragment=frag;
    }

    protected void onPreExecute() {
        DialogRename dialogRename=new DialogRename(mContext,mType);
        dialogRename.Rename(mView,mType,mPosition);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object o) {
        sourceItemFragment.setCurrentItem(mType);
        mView.invalidate();
    }
}
