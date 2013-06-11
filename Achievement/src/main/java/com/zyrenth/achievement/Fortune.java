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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zyrenth.achievement.data.AchievementProvider;
import com.zyrenth.achievement.data.AchievementTable;

import org.jetbrains.annotations.NotNull;

public class Fortune {
	public static int gravity;
	public static int offsetX;
	public static int offsetY;
	public static int theme;

	public static void DisplayFortune(Context context) {
		UpdateLocation(context);
		DisplayFortune(context, offsetX, offsetY);
	}

    public static String GetRandomFortune(Context context) {

        String[] projection = { AchievementTable.COLUMN_BODY };
        Uri uri = AchievementProvider.CONTENT_URI;

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, "RANDOM() LIMIT 1");
        cursor.moveToFirst();

        String message = "";
        if (cursor.getCount() > 0)
            message = cursor.getString(0);
        if (message.trim().equals(""))
            message = "You forget to load fortunes!";

        cursor.close();

        return message;
    }

    public static int getCount(Context context) {
        String[] projection = { AchievementTable.SPECIAL_COLUMN_COUNT };
        Uri uri = AchievementProvider.CONTENT_URI;

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int count = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

	public static void DisplayFortune(Context context, int offsetX, int offsetY) {
		DisplayFortune(context, offsetX, offsetY, null);
	}

	public static void DisplayFortune(Context context, int offsetX, int offsetY, String location) {

		String message = GetRandomFortune(context);

		// and do whatever you need to do here
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(theme, null);

		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(text.getText() + message);

		Toast toast = new Toast(context);

		if (location != null)
			toast.setGravity(getGravity(location), offsetX, offsetY);
		else
			toast.setGravity(gravity, offsetX, offsetY);

		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}



	public static @NotNull String Rot13(@NotNull String s) {
		char[] ch = s.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (c >= 'a' && c <= 'm')
				c += 13;
			else if (c >= 'n' && c <= 'z')
				c -= 13;
			else if (c >= 'A' && c <= 'M')
				c += 13;
			else if (c >= 'A' && c <= 'Z')
				c -= 13;
			ch[i] = c;
		}
		return new String(ch);
	}

	public static void UpdateLocation(Context context) {
		SharedPreferences settings = // context.getSharedPreferences(Constants.PREF_STRING,
										// Context.MODE_PRIVATE);
		PreferenceManager.getDefaultSharedPreferences(context);
		gravity = getGravity(settings.getString(Constants.LOCATION, "Default"));
		offsetX = settings.getInt(Constants.OFFSETX, 0);
		offsetY = settings.getInt(Constants.OFFSETY, 0);
		theme = getTheme(settings.getString(Constants.THEME, "fortune"));
	}

	private static int getGravity(String location) {
		if (location.equals("Top"))
			return Gravity.TOP;

		if (location.equals("Bottom"))
			return Gravity.BOTTOM;

		if (location.equals("Left"))
			return Gravity.LEFT;

		if (location.equals("Right"))
			return Gravity.RIGHT;

		if (location.equals("Center"))
			return Gravity.CENTER;

		return Gravity.NO_GRAVITY;
	}

	private static int getTheme(String theme) {
		if (theme.equals("fortune"))
			return R.layout.toast_fortune;
		if (theme.equals("achievement"))
			return R.layout.toast_360;

		return R.layout.toast_fortune;
	}

	public static ArrayList<String> makeFortunes(Reader reader, int maxLength, boolean rot13) throws IOException {

		BufferedReader myNumReader = new BufferedReader(reader);

		String input = "";
		String temp = "";
		ArrayList<String> fortunes = new ArrayList<String>();
		boolean ignore = false;
		while ((input = myNumReader.readLine()) != null) {
			if (input.equals("%")) {
				temp = temp.trim();

				if (!ignore && !temp.equals("") && temp.length() <= maxLength)
					fortunes.add(rot13 ? Rot13(temp) : temp);

				temp = "";
				ignore = false;
			} else {
				if (temp.length() < maxLength)
					temp += input.trim() + " ";
				else
					ignore = true;
			}

		}

		return fortunes;
	}



}
