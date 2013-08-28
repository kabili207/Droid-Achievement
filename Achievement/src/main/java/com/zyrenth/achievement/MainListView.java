package com.zyrenth.achievement;

/*
 * Copyright 2011 Andrew Nagle
 * 
 * This file is part of Fortune Unlock.
 *
 * Fortune Unlock is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Fortune Unlock is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Fortune Unlock.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.Preference.OnPreferenceChangeListener;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.zyrenth.achievement.data.AchievementProvider;
import com.zyrenth.achievement.data.AchievementTable;

public class MainListView extends SherlockFragmentActivity implements
        PreferenceListFragment.OnPreferenceAttachedListener {



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager
                .beginTransaction();
        SettingsFragment mPrefsFragment = new SettingsFragment();
        mFragmentTransaction.replace(android.R.id.content, mPrefsFragment);
        mFragmentTransaction.commit();

	}



	@Override
	protected void onResume() {
		super.onResume();

		// Setup the initial values
		// mCheckBoxPreference.setSummary(sharedPreferences.getBoolean(key,
		// false) ? "Disable this setting" : "Enable this setting");
		// mListPreference.setSummary("Current value is " +
		// sharedPreferences.getValue(key, ""));

		// Set up a listener whenever a key changes
        //mPrefsFragment.getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Unregister the listener whenever a key changes
        //mPrefsFragment.getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	public void onPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(Constants.ENABLED)) {
			// mCheckBoxPreference.setSummary(sharedPreferences.getBoolean(key,
			// false) ? "Disable this setting" : "Enable this setting");
		} else if (key.equals(Constants.LOCATION)) {
			// mLocationPref.setSummary("Current value is "
			// + sharedPreferences.getString(key, "Default"));
		}

	}

    //setup your onPreferenceClickListener/onPreferenceChangeListener here
    @Override
    public void onPreferenceAttached(PreferenceScreen root, int xmlId){
        if(root == null)
            return; //for whatever reason in very rare cases this is null
        //if(xmlId == R.xml.widget_settings){        //example id
        //    root.findPreference("somePreference").setOnPreferenceClickListener(this);
        //}
    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SettingsFragment.LOAD_BUILT_IN) {
			//mBtnTestPref.setSummary("Total fortunes: " + Fortune.getCount(this));
		} else if (requestCode == SettingsFragment.LOAD_FROM_FILE && resultCode == RESULT_OK) {
			Uri uri = data.getData();
			if (uri != null) {
				try {
					FileReader reader = new FileReader(uri.getPath());

					final ArrayList<String> fortunes = Fortune.makeFortunes(reader, 40, false);
					if (fortunes.size() > 0) {
						String fortuneA = fortunes.get(0);
						String fortuneB = Fortune.Rot13(fortunes.get(0));
						AlertDialog.Builder builder = new AlertDialog.Builder(this);
						builder.setMessage("Which fortune looks correct to you?\n" + "A) " + fortuneA + "\nB) " + fortuneB).setCancelable(true)
								.setPositiveButton("A", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.dismiss();
                                        for (String fortune : fortunes) {

                                            ContentValues values = new ContentValues();
                                            values.put(AchievementTable.COLUMN_BODY, fortune);
                                            getContentResolver().insert(AchievementProvider.CONTENT_URI, values);
                                        }
										Toast.makeText(MainListView.this, fortunes.size() + " Found", Toast.LENGTH_SHORT).show();

										int count = Fortune.getCount(MainListView.this);
										//mBtnTestPref.setSummary("Total fortunes: " + count);

									}
								}).setNegativeButton("B", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.dismiss();
                                        for (String fortune : fortunes) {
                                            ContentValues values = new ContentValues();
                                            values.put(AchievementTable.COLUMN_BODY, Fortune.Rot13(fortune));
                                            getContentResolver().insert(AchievementProvider.CONTENT_URI, values);
                                        }
										Toast.makeText(MainListView.this, fortunes.size() + " Found", Toast.LENGTH_SHORT).show();

										int count = Fortune.getCount(MainListView.this);
										//mBtnTestPref.setSummary("Total fortunes: " + count);

									}
								})
						// .setNeutralButton("Try another", new
						// DialogInterface.OnClickListener() {
						// public void onClick(DialogInterface dialog, int id) {
						// dialog.cancel();
						// }
						// })
						;
						AlertDialog alert = builder.create();
						alert.show();

					}

				} catch (IOException e) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage(e.getMessage()).create().show();

				}
			} else {
                // Do nothing?
			}
		}
	}

}
