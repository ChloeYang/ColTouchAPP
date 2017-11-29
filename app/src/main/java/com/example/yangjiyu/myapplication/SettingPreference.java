package com.example.yangjiyu.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

/**
 * Created by yangjiyu on 2017/11/22.
 */

public class SettingPreference extends AppCompatPreferenceActivity {

    private static String strIp="";
    private static int iSid=-1;
    private static int iRow=0;
    private static int iCol=0;

    private static EditTextPreference ip=null;
    private static EditTextPreference sid=null;
    private static EditTextPreference row=null;
    private static EditTextPreference col=null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

        android.app.ActionBar actionBar = this.getActionBar();
        if( actionBar != null ){
            actionBar.setDisplayHomeAsUpEnabled( true );
        }

        SharedPreferences preferences = getSharedPreferences(getString(R.string.pref_setting),Context.MODE_PRIVATE);
        strIp=preferences.getString(getString(R.string.pref_data_vclordip),"");
        iSid=preferences.getInt(getString(R.string.pref_data_sid),-1);
        iRow=preferences.getInt(getString(R.string.pref_data_row),0);
        iCol=preferences.getInt(getString(R.string.pref_data_col),0);
    }

/*    public void onBackPressed(){
        Toast.makeText(getApplicationContext(),"You have pressed onBackPressed", Toast.LENGTH_LONG)
                .show();
       super.onBackPressed();
    }*/
    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void onBuildHeaders(List<Header>target){
        loadHeadersFromResource(R.xml.preference_head,target);
    }
    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GenerationFragment.class.getName().equals(fragmentName)
                || DataFragment.class.getName().equals(fragmentName);
    }
    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GenerationFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            //bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_gen_display_name)));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_data);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.

            ip = (EditTextPreference)getPreferenceScreen().findPreference("prf_data_ip") ;
            sid = (EditTextPreference)getPreferenceScreen().findPreference("prf_data_sid") ;
            row = (EditTextPreference)getPreferenceScreen().findPreference("prf_data_row") ;
            col = (EditTextPreference)getPreferenceScreen().findPreference("prf_data_col") ;

            ip.setSummary(strIp);
            sid.setSummary(""+iSid);
            row.setSummary(""+iRow);
            col.setSummary(""+iCol);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

}
