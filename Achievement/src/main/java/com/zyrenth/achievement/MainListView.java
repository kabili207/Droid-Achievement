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
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceClickListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zyrenth.achievement.data.AchievementProvider;
import com.zyrenth.achievement.data.AchievementTable;

public class MainListView extends PreferenceActivity implements
// OnPreferenceChangeListener,
		OnSharedPreferenceChangeListener {

	private Preference mBtnTestPref;
	private Preference mBtnImport;
	private Preference mBtnAbout;
    private Preference mBtnClear;
    private Preference mBtnManage;

	private Preference mBtnBuiltIn;
	private Preference mBtnAdd;
	private static final int LOAD_BUILT_IN = 1;
	private static final int LOAD_FROM_FILE = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.achieve_pref);


		PreferenceScreen prefs = getPreferenceScreen();

		mBtnTestPref = (Preference) prefs.findPreference(getString(R.string.pref_btn_test));

		mBtnTestPref.setSummary("Total fortunes: " + Fortune.getCount(this));
		mBtnTestPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			public boolean onPreferenceClick(Preference arg0) {
				Fortune.DisplayFortune(MainListView.this);
				return false;
			}

		});

		mBtnImport = (Preference) prefs.findPreference(getString(R.string.pref_btn_import));
		mBtnImport.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			public boolean onPreferenceClick(Preference preference) {

				Intent i = new Intent();
				i.setAction(Intent.ACTION_GET_CONTENT);
				i.setType("text/plain");
				startActivityForResult(Intent.createChooser(i, "Select fortune file"), LOAD_FROM_FILE);
				return false;
			}

		});

		mBtnAdd = (Preference) prefs.findPreference(getString(R.string.pref_btn_add));
		mBtnAdd.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			public boolean onPreferenceClick(Preference preference) {

				addFortune();
				return false;
			}

		});

		mBtnBuiltIn = (Preference) prefs.findPreference(getString(R.string.pref_btn_builtin));
		mBtnBuiltIn.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			public boolean onPreferenceClick(Preference preference) {
				LoadBuiltIn();
				return false;
			}

		});

		mBtnAbout = (Preference) prefs.findPreference(getString(R.string.pref_btn_about));
		mBtnAbout.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			public boolean onPreferenceClick(Preference arg0) {
				AlertDialog builder;
				try {
					builder = AboutDialogBuilder.create(MainListView.this);
					builder.show();
				} catch (NameNotFoundException e) {
					// e.printStackTrace();
				}
				return false;
			}

		});

        mBtnClear = (Preference) prefs.findPreference(getString(R.string.pref_btn_clear));
        mBtnClear.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference arg0) {

                getContentResolver().delete(AchievementProvider.CONTENT_URI, null, null);

                mBtnTestPref.setSummary("Total fortunes: 0");
                return false;
            }

        });

        mBtnManage = (Preference) prefs.findPreference(getString(R.string.pref_btn_manage));
        mBtnManage.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference arg0) {
               // dbHelper.deleteAchievements();
               // mBtnTestPref.setSummary("Total fortunes: " + dbHelper.getCount());
                Intent i = new Intent(MainListView.this, AchievementListActivity.class);

                startActivity(i);
                return false;
            }

        });
	}

	private void LoadBuiltIn() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("WARNING!")
				.setMessage(
						"Built-in fortunes are the same fortunes used with " + "the Unix program fortune-mod. Certain fortune categories "
								+ "can be offensive and have been prefixed with the word " + "\"Offensive.\" Do not load these fortunes if you or others "
								+ "who may see your phone are offended by them.").setCancelable(true)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();

						Intent i = new Intent(MainListView.this, BuiltinListView.class);

                        startActivityForResult(i, LOAD_BUILT_IN);

					}
				});
		AlertDialog alert = builder.create();
		alert.show();

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
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Unregister the listener whenever a key changes
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(Constants.ENABLED)) {
			// mCheckBoxPreference.setSummary(sharedPreferences.getBoolean(key,
			// false) ? "Disable this setting" : "Enable this setting");
		} else if (key.equals(Constants.LOCATION)) {
			// mLocationPref.setSummary("Current value is "
			// + sharedPreferences.getString(key, "Default"));
		}

	}

	private void addFortune() {
		final Dialog commentDialog = new Dialog(this);
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
                    getContentResolver().insert(AchievementProvider.CONTENT_URI, values);

					Toast.makeText(MainListView.this, "Added Fortune", Toast.LENGTH_SHORT).show();

					int count = Fortune.getCount(MainListView.this);
					mBtnTestPref.setSummary("Total fortunes: " + count);
					commentDialog.dismiss();
				} else {
					Toast.makeText(MainListView.this, "Please enter fortune text", Toast.LENGTH_SHORT).show();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == LOAD_BUILT_IN) {
			mBtnTestPref.setSummary("Total fortunes: " + Fortune.getCount(this));
		} else if (requestCode == LOAD_FROM_FILE && resultCode == RESULT_OK) {
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
										for (int i = 0; i < fortunes.size(); i++) {

                                            ContentValues values = new ContentValues();
                                            values.put(AchievementTable.COLUMN_BODY, fortunes.get(i));
                                            getContentResolver().insert(AchievementProvider.CONTENT_URI, values);
										}
										Toast.makeText(MainListView.this, fortunes.size() + " Found", Toast.LENGTH_SHORT).show();

										int count = Fortune.getCount(MainListView.this);
										mBtnTestPref.setSummary("Total fortunes: " + count);

									}
								}).setNegativeButton("B", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.dismiss();
										for (int i = 0; i < fortunes.size(); i++) {
                                            ContentValues values = new ContentValues();
                                            values.put(AchievementTable.COLUMN_BODY, Fortune.Rot13(fortunes.get(i)));
                                            getContentResolver().insert(AchievementProvider.CONTENT_URI, values);
										}
										Toast.makeText(MainListView.this, fortunes.size() + " Found", Toast.LENGTH_SHORT).show();

										int count = Fortune.getCount(MainListView.this);
										mBtnTestPref.setSummary("Total fortunes: " + count);

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

			}
		}
	}

}
