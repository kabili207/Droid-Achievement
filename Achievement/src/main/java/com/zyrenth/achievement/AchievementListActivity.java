package com.zyrenth.achievement;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.zyrenth.achievement.data.AchievementProvider;
import com.zyrenth.achievement.data.AchievementTable;


/**
 * Created by kabili on 6/9/13.
 */

public class AchievementListActivity extends SherlockFragmentActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = Menu.FIRST + 1;
    // private Cursor cursor;
    private SimpleCursorAdapter adapter;
    private ListView mListView;


    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_list);
        setContentView(android.R.layout.list_content);
        mListView = (ListView)this.findViewById(android.R.id.list);
        mListView.setDividerHeight(2);
        fillData();
        registerForContextMenu(mListView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent i = new Intent(this, TodoDetailActivity.class);
                //Uri todoUri = Uri.parse(MyTodoContentProvider.CONTENT_URI + "/" + id);
                //i.putExtra(MyTodoContentProvider.CONTENT_ITEM_TYPE, todoUri);

                //startActivity(i);
            }
        });


    }

    // Create the menu based on the XML defintion
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.listmenu, menu);
        return true;
    }

    // Reaction to the menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert:
                createTodo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

  /*  @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                        .getMenuInfo();
                Uri uri = Uri.parse(AchievementProvider.CONTENT_URI + "/"
                        + info.id);
                getContentResolver().delete(uri, null, null);
                fillData();
                return true;
        }

        return super.onContextItemSelected(item);
    }*/

    private void createTodo() {
        //Intent i = new Intent(this, TodoDetailActivity.class);
       // startActivity(i);
    }



    private void fillData() {

        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[] { AchievementTable.COLUMN_BODY };
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.body };


        getSupportLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.row_achievement, null, from,
                to, 0);

        mListView.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    // Creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { AchievementTable.COLUMN_ID, AchievementTable.COLUMN_BODY };
        CursorLoader cursorLoader = new CursorLoader(this,
                AchievementProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        adapter.swapCursor(null);
    }

}