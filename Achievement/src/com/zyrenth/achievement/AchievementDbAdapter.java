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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AchievementDbAdapter {

	// Database fields
	public static final String KEY_ROWID = "_id";
	public static final String KEY_BODY = "body";
	private static final String DB_TABLE = "achievements";
	private Context context;
	private SQLiteDatabase db;
	private AchievementDbHelper dbHelper;

	public AchievementDbAdapter(Context context) {
		this.context = context;
	}

	public AchievementDbAdapter open() throws SQLException {
		dbHelper = new AchievementDbHelper(context);
		db = dbHelper.getWritableDatabase();
		//dbHelper.onUpgrade(db, 1, 1);
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	/**
	 * Create a new todo If the todo is successfully created return the new
	 * rowId for that note, otherwise return a -1 to indicate failure.
	 */
	public long createAchievement(String body) {
		ContentValues values = createContentValues(body);

		return db.insert(DB_TABLE, null, values);// SQLiteDatabase.CONFLICT_IGNORE );
	}

	/**
	 * Update the todo
	 */
	public boolean updateAchievement(long rowId, String body) {
		ContentValues values = createContentValues(body);

		return db.update(DB_TABLE, values, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Deletes todo
	 */
	public boolean deleteAchievement(long rowId) {
		return db.delete(DB_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Deletes todo
	 */
	public boolean deleteAchievements() {
		return db.delete(DB_TABLE, null, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all todo in the database
	 * 
	 * @return Cursor over all notes
	 */
	public Cursor fetchAllAchievements() {
		return db.query(DB_TABLE, new String[] { KEY_ROWID, KEY_BODY }, null, null, null, null, null);
	}

	/**
	 * Return a Cursor positioned at the defined todo
	 */
	public Cursor fetchAchievement(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, DB_TABLE, new String[] { KEY_ROWID,
				KEY_BODY }, KEY_ROWID + "="
				+ rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/**
	 * Return a Cursor positioned at the defined todo
	 */
	public Cursor fetchAchievement() throws SQLException {
		Cursor mCursor = db.query(true, DB_TABLE, new String[] { KEY_ROWID,
				KEY_BODY }, null, null, null, null, "RANDOM()", "1");
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * Return a Cursor positioned at the defined todo
	 */
	public int getCount() throws SQLException {
		Cursor mCursor = db.query(true, DB_TABLE, new String[] {
				"COUNT("+KEY_ROWID+")"}, null, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
			return mCursor.getInt(0);
		}
		return 0;
	}

	private ContentValues createContentValues(String body) {
		ContentValues values = new ContentValues();
		values.put(KEY_BODY, body);
		return values;
	}

}