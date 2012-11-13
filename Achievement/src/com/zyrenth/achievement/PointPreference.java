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

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class PointPreference extends DialogPreference {

	private Context mContext;
	private TextView txtOffsetX;
	private TextView txtOffsetY;
	private Point initialValue;

	public PointPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public PointPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	protected View onCreateDialogView() {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.point_pref, null);

		if (shouldPersist()) {
			initialValue = getPersistedPoint(new Point(0, 0));
			txtOffsetX = (TextView) layout.findViewById(R.id.txtOffsetX);
			txtOffsetY = (TextView) layout.findViewById(R.id.txtOffsetY);

			if (initialValue != null) {
				txtOffsetX.setText(initialValue.x + "");
				txtOffsetY.setText(initialValue.y + "");
			}
		}

		return layout;
	}

	@Override
	public boolean hasKey() {
		// TODO Auto-generated method stub
		return this.getSharedPreferences().contains(getKey() + Constants.X_SUFFIX) && this.getSharedPreferences().contains(getKey() + Constants.Y_SUFFIX);

	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		// TODO Auto-generated method stub
		super.onSetInitialValue(restorePersistedValue, defaultValue);
		Point def = (defaultValue instanceof Point) ? (Point) defaultValue : new Point(0, 0);
		if (restorePersistedValue) {
			this.initialValue = getPersistedPoint(def);
		} else
			this.initialValue = (Point) defaultValue;
	}

	private Point getPersistedPoint(Point def) {
		// TODO Auto-generated method stub
		if (this.isPersistent()) {
			int x = this.getSharedPreferences().getInt(getKey() + Constants.X_SUFFIX, 0);
			int y = this.getSharedPreferences().getInt(getKey() + Constants.Y_SUFFIX, 0);
			return new Point(x, y);
		}
		return def;
	}

	@Override
	protected void onBindDialogView(View view) {
		// TODO Auto-generated method stub
		super.onBindDialogView(view);

		txtOffsetX = (TextView) view.findViewById(R.id.txtOffsetX);
		txtOffsetY = (TextView) view.findViewById(R.id.txtOffsetY);

		if (initialValue != null) {
			txtOffsetX.setText(initialValue.x + "");
			txtOffsetY.setText(initialValue.y + "");
		} else {
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		// TODO Auto-generated method stub
		return new Point(0, 0);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		super.onClick(dialog, which);
		if (which == DialogInterface.BUTTON_POSITIVE) {
			int offX = 0, offY = 0;

			try {
				offX = Integer.parseInt(txtOffsetX.getText().toString());
			} catch (NumberFormatException nfe) {
			}
			try {
				offY = Integer.parseInt(txtOffsetY.getText().toString());
			} catch (NumberFormatException nfe) {
			}

			if (initialValue == null)
				initialValue = new Point(offX, offY);
			else
				initialValue.set(offX, offY);

			persistPoint(initialValue);
			callChangeListener(initialValue);
		}
	}

	protected boolean persistPoint(Point p) {
		String x = getKey() + Constants.X_SUFFIX;
		String y = getKey() + Constants.Y_SUFFIX;
		SharedPreferences.Editor ed = getEditor();
		ed.putInt(x, p.x);
		ed.putInt(y, p.y);
		if (shouldCommit())
			return ed.commit();
		return false;
	}

}
