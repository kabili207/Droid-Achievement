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

import java.util.ArrayList;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MenuArrayAdapter extends ArrayAdapter<MenuArrayAdapter.MenuItem> {
	private final Activity context;
	private final ArrayList<MenuItem> menuItems;

	public static class MenuItem {
		public int resourceId;
		public String title;
		public boolean offensive;
		public MenuItem(String title, int resourceId, boolean offensive) {
			super();
			this.resourceId = resourceId;
			this.title = title;
			this.offensive = offensive;
		}
	
	}
	
	public MenuArrayAdapter(Activity context, ArrayList<MenuItem> menuItems) {
		super(context, R.layout.menu_icon, menuItems);
		this.context = context;
		this.menuItems = menuItems;
	}

	// static to save the reference to the outer class and to avoid access to
	// any members of the containing class
	static class ViewHolder {
		public TextView textView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		

		ViewHolder holder;
		MenuItem menuItem = menuItems.get(position);
		

		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
			holder = new ViewHolder();
			holder.textView = (TextView) rowView.findViewById(android.R.id.text1);
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}

		holder.textView.setText((menuItem.offensive ? "Offensive - " : "") + menuItem.title);
		
		return rowView;
	}
	
	
}
