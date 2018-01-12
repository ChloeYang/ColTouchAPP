package com.example.yangjiyu.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by yangjiyu on 2017/12/29.
 */

public class DialogRename {
    private Context mContext;
    private SharedAppData sharedAppData;
    private int mtype;
    public DialogRename(Context context,int type) {
        mContext = context;
        sharedAppData = SharedAppData.newInstance(context);
        mtype = type;
    }
    public void Rename(View view, int type, int position){
        switch (type){
            case 3:
                SignalRename(view,position);
                break;
            case 1:
                SignalModelRename(view,position);
                break;
            case 4:
                //ColorModeRename(position);
                break;
            default:
                break;
        }
    }
    public void SignalRename(final View view, final int position) {
        final EditText editText = new EditText(mContext);
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext,3);
        builder.setTitle(mContext.getString(R.string.rename_dialog));
        builder.setIcon(R.mipmap.ic_launcher_self);
        builder.setView(editText);
        builder.setPositiveButton(mContext.getString(R.string.Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, editText.getText().toString() + "", Toast.LENGTH_LONG).show();
                sharedAppData.saveSignalName(position,editText.getText().toString());
                view.invalidate();
            }
        });
        builder.setNegativeButton(mContext.getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
    public void SignalModelRename(final View view, final int position) {
        final EditText editText = new EditText(mContext);
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext,3);
        builder.setTitle(mContext.getString(R.string.rename_dialog));
        builder.setIcon(R.mipmap.ic_launcher_self);
        builder.setView(editText);
        builder.setPositiveButton(mContext.getString(R.string.Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, editText.getText().toString() + "", Toast.LENGTH_LONG).show();
                sharedAppData.saveSignalModelName((byte)position,editText.getText().toString());
                view.invalidate();
            }
        });
        builder.setNegativeButton(mContext.getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
    public void ColorModeRename(final int position){
        final EditText editText = new EditText(mContext);
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext,3);
        builder.setTitle(mContext.getString(R.string.rename_dialog));
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setView(editText);
        builder.setPositiveButton(mContext.getString(R.string.Ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, editText.getText().toString() + "", Toast.LENGTH_LONG).show();
                sharedAppData.saveColorModeName((byte)position,editText.getText().toString());
            }
        });
        builder.setNegativeButton(mContext.getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
}
