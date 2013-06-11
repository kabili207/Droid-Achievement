package com.zyrenth.achievement.data;

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

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AchievementTable {


    // Database table
    public static final String TABLE_ACHIEVEMENTS = "achievements";
    public static final String COLUMN_ID = "_id";
    public static final String SPECIAL_COLUMN_COUNT = "COUNT(" + COLUMN_ID + ")";
    public static final String COLUMN_BODY = "body";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_ACHIEVEMENTS
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_BODY + " text not null unique"
            + ");";
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(AchievementTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_ACHIEVEMENTS);
		onCreate(database);
	}
}