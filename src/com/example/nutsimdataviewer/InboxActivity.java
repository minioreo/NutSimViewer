package com.example.nutsimdataviewer;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;

public class InboxActivity extends ListActivity
{

	private static String logTag = "InboxActivity";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		loadInboxSMS();
	}

	private void loadInboxSMS()
	{
		Uri uri = Uri.parse("content://sms/icc");
		String[] projection = new String[] { "body" };
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = null;
		Cursor cursor = managedQuery(uri, projection, selection, selectionArgs, sortOrder);
		Log.d(logTag, "inbox sms count:" + cursor.getCount());
		Log.d(logTag, "move2First:" + cursor.moveToFirst());
		CursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, new String[] { "address", "body" }, new int[] { android.R.id.text1, android.R.id.text2 });
		setListAdapter(adapter);
		registerForContextMenu(getListView());
		// int addrIndex = cursor.getColumnIndexOrThrow("address");
		// int bodyIndex = cursor.getColumnIndexOrThrow("body");
		// while(cursor.moveToNext())
		// {
		// logItem(cursor);
		// String addr = cursor.getString(addrIndex);
		// String body = cursor.getString(bodyIndex);
		// }
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, 0, 0, "复制号码");
		menu.add(0, 1, 0, "复制内容");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		log("itemid:" + item.getItemId());
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		int position = menuInfo.position;
		log("position:" + position);
		SimpleCursorAdapter adapter = (SimpleCursorAdapter) getListAdapter();
		Cursor cursor = adapter.getCursor();
		cursor.moveToPosition(position);
		int bodyIndex = cursor.getColumnIndexOrThrow("body");
		int addrIndex = cursor.getColumnIndexOrThrow("address");
		String body = cursor.getString(bodyIndex);
		String address = cursor.getString(addrIndex);
		log("body:" + body);
		log("address:" + address);
		ClipboardManager m = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		switch (item.getItemId())
		{
		case 0:
			m.setText(address);
			break;
		case 1:
			m.setText(body);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		Object itemAtPosition = l.getItemAtPosition(position);
		log(itemAtPosition.getClass().getName());
		openContextMenu(v);
	}

	private void log(Object str)
	{
		Log.d(logTag, str.toString());
	}

	private void logItem(Cursor cursor)
	{
		log("begin to log item");
		for (int i = 0; i < cursor.getColumnCount(); i++)
		{
			String value = cursor.getString(i);
			String columnName = cursor.getColumnName(i);
			log(columnName + ":" + value);
		}
		log("finish to log item");
	}

}
