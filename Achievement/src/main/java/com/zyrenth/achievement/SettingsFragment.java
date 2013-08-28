package com.zyrenth.achievement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zyrenth.achievement.data.AchievementProvider;
import com.zyrenth.achievement.data.AchievementTable;

/**
 * Created by kabili on 8/27/13.
 */
public class SettingsFragment extends PreferenceListFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener,
        PreferenceListFragment.OnPreferenceAttachedListener {

    public static final int LOAD_BUILT_IN = 15001;
    public static final int LOAD_FROM_FILE = 15002;


    private Preference mBtnTestPref;
    private Preference mBtnImport;
    private Preference mBtnAbout;
    private Preference mBtnClear;
    private Preference mBtnManage;

    private Preference mBtnBuiltIn;
    private Preference mBtnAdd;


    @Override
    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.achieve_pref);

    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        prefs.registerOnSharedPreferenceChangeListener(this);


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
    }

    @Override
    public void onPreferenceAttached(PreferenceScreen root, int xmlId) {
        if (root == null)
            return;

        PreferenceScreen prefScreen = root;

        mBtnTestPref = (Preference) prefScreen.findPreference(getString(R.string.pref_btn_test));

        mBtnTestPref.setSummary("Total fortunes: " + Fortune.getCount(getActivity()));
        mBtnTestPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference arg0) {
                Fortune.DisplayFortune(getActivity());
                return false;
            }

        });

        mBtnImport = (Preference) prefScreen.findPreference(getString(R.string.pref_btn_import));
        mBtnImport.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("text/plain");
                startActivityForResult(Intent.createChooser(i, "Select fortune file"), LOAD_FROM_FILE);
                return false;
            }

        });

        mBtnAdd = (Preference) prefScreen.findPreference(getString(R.string.pref_btn_add));
        mBtnAdd.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {

                addFortune();
                return false;
            }

        });

        mBtnBuiltIn = (Preference) prefScreen.findPreference(getString(R.string.pref_btn_builtin));
        mBtnBuiltIn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {
                LoadBuiltIn();
                return false;
            }

        });

        mBtnAbout = (Preference) prefScreen.findPreference(getString(R.string.pref_btn_about));
        mBtnAbout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference arg0) {
                AlertDialog builder;
                try {
                    builder = AboutDialogBuilder.create(getActivity());
                    builder.show();
                } catch (PackageManager.NameNotFoundException e) {
                    // e.printStackTrace();
                }
                return false;
            }

        });

        mBtnClear = (Preference) prefScreen.findPreference(getString(R.string.pref_btn_clear));
        mBtnClear.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference arg0) {

                getActivity().getContentResolver().delete(AchievementProvider.CONTENT_URI, null, null);

                mBtnTestPref.setSummary("Total fortunes: 0");
                return false;
            }

        });

        mBtnManage = (Preference) prefScreen.findPreference(getString(R.string.pref_btn_manage));
        mBtnManage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference arg0) {
                // dbHelper.deleteAchievements();
                // mBtnTestPref.setSummary("Total fortunes: " + dbHelper.getCount());
                Intent i = new Intent(getActivity(), AchievementListActivity.class);

                startActivity(i);
                return false;
            }

        });

    }

    private void LoadBuiltIn() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("WARNING!")
                .setMessage(
                        "Built-in fortunes are the same fortunes used with " + "the Unix program fortune-mod. Certain fortune categories "
                                + "can be offensive and have been prefixed with the word " + "\"Offensive.\" Do not load these fortunes if you or others "
                                + "who may see your phone are offended by them.").setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        Intent i = new Intent(getActivity(), BuiltinListView.class);

                        startActivityForResult(i, LOAD_BUILT_IN);

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void addFortune() {
        final Dialog commentDialog = new Dialog(getActivity());
        commentDialog.setTitle("Add Fortune");
        commentDialog.setContentView(R.layout.dialog_fortune);
        Button okBtn = (Button) commentDialog.findViewById(R.id.ok);
        final TextView txtFortune = (TextView) commentDialog.findViewById(R.id.txtFortune);
        final TextView lblRemaining = (TextView) commentDialog.findViewById(R.id.lblRemainingChar);
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String text = txtFortune.getText().toString();
                if (text.length() > 0) {

                    ContentValues values = new ContentValues();
                    values.put(AchievementTable.COLUMN_BODY, txtFortune.getText().toString());
                    getActivity().getContentResolver().insert(AchievementProvider.CONTENT_URI, values);

                    Toast.makeText(getActivity(), "Added Fortune", Toast.LENGTH_SHORT).show();

                    int count = Fortune.getCount(getActivity());
                    mBtnTestPref.setSummary("Total fortunes: " + count);
                    commentDialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Please enter fortune text", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button cancelBtn = (Button) commentDialog.findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                commentDialog.dismiss();
            }
        });
        txtFortune.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                int remaining = 40 - s.length();
                lblRemaining.setText(remaining + "");
                if (remaining == 0)
                    lblRemaining.setTextColor(Color.RED);
                else if (remaining <= 5)
                    lblRemaining.setTextColor(Color.rgb(255, 127, 0));
                else if (remaining <= 10)
                    lblRemaining.setTextColor(Color.YELLOW);
                else
                    lblRemaining.setTextColor(Color.WHITE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });
        commentDialog.show();
    }


}
