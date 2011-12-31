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

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.Html;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.widget.TextView;

public final class AboutDialogBuilder {
	private static String versionInfo;

	public static AlertDialog create( Context context ) throws NameNotFoundException {
		
		PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
		versionInfo = pInfo.versionName;

		String aboutTitle = String.format("About %s", context.getString(R.string.app_name));
		String versionString = String.format("Version: %s", versionInfo);
		String aboutText = context.getString(R.string.about_text);

		
		final TextView message = new TextView(context);
		
		final SpannableString s = new SpannableString(aboutText);

		
		message.setPadding(5, 5, 5, 5);
		
		message.setText(Html.fromHtml(versionString + "\n\n" + s));
		
		Linkify.addLinks(message, Linkify.ALL);

		return new AlertDialog.Builder(context).setTitle(aboutTitle).setCancelable(true).setIcon(R.drawable.fortune).setPositiveButton(
			 context.getString(android.R.string.ok), null).setView(message).create();
	}
}
