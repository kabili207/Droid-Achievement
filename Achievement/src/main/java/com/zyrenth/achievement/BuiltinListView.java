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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.zyrenth.achievement.data.AchievementProvider;
import com.zyrenth.achievement.data.AchievementTable;

public class BuiltinListView extends SherlockListActivity {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ArrayList<MenuArrayAdapter.MenuItem> menuItems = new ArrayList<MenuArrayAdapter.MenuItem>();
		menuItems.add(new MenuArrayAdapter.MenuItem("Art", R.raw.frtn_reg_art, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Chalkboard", R.raw.frtn_reg_chalkboard, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Chuck Norris", R.raw.frtn_reg_chucknorris, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Computers", R.raw.frtn_reg_computers, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Cookie", R.raw.frtn_reg_cookie, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Debian", R.raw.frtn_reg_debian, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Definitions", R.raw.frtn_reg_definitions, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Drugs", R.raw.frtn_reg_drugs, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Education", R.raw.frtn_reg_education, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Ethnic", R.raw.frtn_reg_ethnic, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Family Guy", R.raw.frtn_reg_familyguy, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Food", R.raw.frtn_reg_food, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Fortunes", R.raw.frtn_reg_fortunes, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Futurama", R.raw.frtn_reg_futurama, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Goedel", R.raw.frtn_reg_goedel, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Hitchhiker", R.raw.frtn_reg_hitchhiker, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Humorists", R.raw.frtn_reg_humorists, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Kernel Cookies", R.raw.frtn_reg_kernelcookies, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Kids", R.raw.frtn_reg_kids, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Law", R.raw.frtn_reg_law, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Linux", R.raw.frtn_reg_linux, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Linux Cookie", R.raw.frtn_reg_linuxcookie, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Literature", R.raw.frtn_reg_literature, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Love", R.raw.frtn_reg_love, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Magic", R.raw.frtn_reg_magic, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Medicine", R.raw.frtn_reg_medicine, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Men/Women", R.raw.frtn_reg_men_women, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Miscellaneous", R.raw.frtn_reg_miscellaneous, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("News", R.raw.frtn_reg_news, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Paradoxum", R.raw.frtn_reg_paradoxum, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("People", R.raw.frtn_reg_people, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Perl", R.raw.frtn_reg_perl, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Pets", R.raw.frtn_reg_pets, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Platitudes", R.raw.frtn_reg_platitudes, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Politics", R.raw.frtn_reg_politics, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Riddles", R.raw.frtn_reg_riddles, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Science", R.raw.frtn_reg_science, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Songs/Poems", R.raw.frtn_reg_songs_poems, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Sports", R.raw.frtn_reg_sports, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Star Trek", R.raw.frtn_reg_startrek, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Wisdom", R.raw.frtn_reg_wisdom, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Work", R.raw.frtn_reg_work, false));
		menuItems.add(new MenuArrayAdapter.MenuItem("Zippy", R.raw.frtn_reg_zippy, false));

		menuItems.add(new MenuArrayAdapter.MenuItem("Art", R.raw.frtn_off_art, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Astrology", R.raw.frtn_off_astrology, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Atheism", R.raw.frtn_off_atheism, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Black Humor", R.raw.frtn_off_black_humor, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Cookie", R.raw.frtn_off_cookie, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Debian", R.raw.frtn_off_debian, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Drugs", R.raw.frtn_off_drugs, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Ethnic", R.raw.frtn_off_ethnic, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Fortunes", R.raw.frtn_off_fortunes, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Homophobia", R.raw.frtn_off_hphobia, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Limerick", R.raw.frtn_off_limerick, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Linux", R.raw.frtn_off_linux, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Misandry", R.raw.frtn_off_misandry, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Miscellaneous", R.raw.frtn_off_miscellaneous, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Misogyny", R.raw.frtn_off_misogyny, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Politics", R.raw.frtn_off_politics, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Privates", R.raw.frtn_off_privates, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Racism", R.raw.frtn_off_racism, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Religion", R.raw.frtn_off_religion, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Riddles", R.raw.frtn_off_riddles, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Sex", R.raw.frtn_off_sex, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Songs/Poems", R.raw.frtn_off_songs_poems, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Vulgarity", R.raw.frtn_off_vulgarity, true));
		menuItems.add(new MenuArrayAdapter.MenuItem("Zippy", R.raw.frtn_off_zippy, true));

		setListAdapter(new MenuArrayAdapter(this, menuItems));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Object o = this.getListAdapter().getItem(position);
		if (o instanceof MenuArrayAdapter.MenuItem) {
			MenuArrayAdapter.MenuItem keyword = (MenuArrayAdapter.MenuItem) o;
			LoadBuiltin(keyword.resourceId, keyword.offensive);
		}
	}

	private void LoadBuiltin(int resourceId, boolean offensive) {
		InputStream is = this.getResources().openRawResource(resourceId);

		try {
			int oldCount = Fortune.getCount(this);
			ArrayList<String> fortunes = Fortune.makeFortunes(new InputStreamReader(is), 40, offensive);

            for (String fortune : fortunes) {
                ContentValues values = new ContentValues();
                values.put(AchievementTable.COLUMN_BODY, fortune);
                getContentResolver().insert(AchievementProvider.CONTENT_URI, values);
            }

			int newCount = Fortune.getCount(this);
			Toast.makeText(this, "Found " + (newCount - oldCount) + " new fortunes", Toast.LENGTH_SHORT).show();

		} catch (IOException e) {
		}

	}

}
