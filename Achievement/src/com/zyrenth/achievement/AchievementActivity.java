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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AchievementActivity extends Activity
{
	private AchievementDbAdapter dbHelper;
	private Spinner spinOffset;
	private LinearLayout layOffset;
	
	private TextView txtOffsetX;
	private TextView txtOffsetY;
	private CheckBox chkEnabled;
	private Button btnApply;
	private TextView lblFortuneCount;
	
	private static final int LOAD_FROM_FILE = 1;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		dbHelper = new AchievementDbAdapter(this);
		dbHelper.open();
		
		lblFortuneCount = (TextView) findViewById(R.id.lblFortuneCount);
		
		Button btnBuiltIn = (Button) findViewById(R.id.btnLoadBuiltIn);
		btnBuiltIn.setOnClickListener(new Button.OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				fillData();
			}
		});
		
		Button btnLoadFile = (Button) findViewById(R.id.btnLoadFile);
		btnLoadFile.setOnClickListener(new Button.OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				
				Intent i = new Intent(AchievementActivity.this,
						MainListView.class);
				// i.putExtra("com.zyrenth.client.book", result.get(0));
				
				// Set the request code to any code you like, you can identify
				// the
				// callback via this code
				startActivityForResult(i, 0);
				
				// Intent i = new Intent();
				// i.setAction(Intent.ACTION_GET_CONTENT);
				// i.setType("text/plain");
				//startActivityForResult(Intent.createChooser(i,
				//		"Select fortune file"), LOAD_FROM_FILE);
			}
			
		});
		
		Button btnTest = (Button) findViewById(R.id.btnTest);
		btnTest.setOnClickListener(new Button.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				int offX = 0, offY = 0;
				
				try
				{
					offX = Integer.parseInt(txtOffsetX.getText().toString());
				}
				catch (NumberFormatException nfe)
				{
				}
				try
				{
					offY = Integer.parseInt(txtOffsetY.getText().toString());
				}
				catch (NumberFormatException nfe)
				{
				}
				Fortune.DisplayFortune(AchievementActivity.this, offX, offY, spinOffset
						.getSelectedItem().toString());
				
			}
			
		});
		
		txtOffsetX = (TextView) findViewById(R.id.txtOffsetX);
		txtOffsetY = (TextView) findViewById(R.id.txtOffsetY);
		chkEnabled = (CheckBox) findViewById(R.id.chkEnabled);
		layOffset = (LinearLayout) findViewById(R.id.layoutOffset);
		spinOffset = (Spinner) findViewById(R.id.spinOffset);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.pref_location_values,
				android.R.layout.simple_spinner_item);
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinOffset.setAdapter(adapter);
		spinOffset.setOnItemSelectedListener(new MyOnItemSelectedListener());
		
		btnApply = (Button) findViewById(R.id.btnApply);
		btnApply.setOnClickListener(mApplyListener);
		
		// SharedPreferences settings = getSharedPreferences("AchievePrefs", 0);
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		chkEnabled.setChecked(settings.getBoolean("enabled", false));
		spinOffset.setSelection(adapter.getPosition(settings.getString(
				"location", "Default")));
		txtOffsetX.setText(settings.getInt("offsetX", 0) + "");
		txtOffsetY.setText(settings.getInt("offsetY", 0) + "");
		lblFortuneCount.setText("Fortune count: " + dbHelper.getCount());
	}
	
	public class MyOnItemSelectedListener implements OnItemSelectedListener
	{
		
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id)
		{
			String terms = parent.getItemAtPosition(pos).toString();
			layOffset.setVisibility(terms.equals("Default") ? View.GONE
					: View.VISIBLE);
		}
		
		public void onNothingSelected(AdapterView<?> parent)
		{
			layOffset.setVisibility(View.GONE);
		}
	};
	
	private void fillData()
	{
		
		int count = dbHelper.getCount();
		lblFortuneCount.setText("Fortune count: " + count);
		
		if (count == 0)
		{
			InputStream is = this.getResources().openRawResource(R.raw.frtn_off_sex);
			
			try
			{
				ArrayList<String> fortunes = Fortune.makeFortunes(
						new InputStreamReader(is), 40, true);
				
				for (int i = 0; i < fortunes.size(); i++)
				{
					dbHelper.createAchievement(fortunes.get(i));
				}
				Toast.makeText(this, fortunes.size() + " Found",
						Toast.LENGTH_SHORT).show();
				
				count = dbHelper.getCount();
				lblFortuneCount.setText("Fortune count: " + count);
				
			}
			catch (IOException e)
			{
			}
		}
		
		// SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
		// R.layout.todo_row, cursor, from, to);
		// setListAdapter(notes);
	}
	
	public Button.OnClickListener mApplyListener = new Button.OnClickListener()
	{
		
		@Override
		public void onClick(View arg0)
		{
			
			SharedPreferences settings = // getSharedPreferences("AchievePrefs",
											// MODE_PRIVATE);
			PreferenceManager
					.getDefaultSharedPreferences(AchievementActivity.this);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("location", spinOffset.getSelectedItem()
					.toString());
			int offX = 0, offY = 0;
			
			try
			{
				offX = Integer.parseInt(txtOffsetX.getText().toString());
			}
			catch (NumberFormatException nfe)
			{
			}
			try
			{
				offY = Integer.parseInt(txtOffsetY.getText().toString());
			}
			catch (NumberFormatException nfe)
			{
			}
			
			editor.putInt("offsetX", offX);
			editor.putInt("offsetY", offY);
			editor.putBoolean("enabled", chkEnabled.isChecked());
			editor.commit();
			
		}
		
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		
		if (requestCode == LOAD_FROM_FILE)
		{
			Uri uri = data.getData();
			if (uri != null)
			{
				try
				{
					FileReader reader = new FileReader(uri.getPath());
					
					ArrayList<String> fortunes = // new ArrayList<String>();
					Fortune.makeFortunes(reader, 40, true);
					
					for (int i = 0; i < fortunes.size(); i++)
					{
						dbHelper.createAchievement(fortunes.get(i));
					}
					Toast.makeText(this, fortunes.size() + " Found",
							Toast.LENGTH_SHORT).show();
					
					int count = dbHelper.getCount();
					lblFortuneCount.setText("Fortune count: " + count);
					
				}
				catch (IOException e)
				{
				}
			}
			else
			{
				
			}
		}
	}
}