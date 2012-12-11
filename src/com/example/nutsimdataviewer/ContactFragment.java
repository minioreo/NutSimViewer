package com.example.nutsimdataviewer;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

public class ContactFragment extends ListFragment implements LoaderCallbacks<Cursor>
{

	private static final String logTag = "ContactFragment";

	private SimpleCursorAdapter cursorAdapter;

	private ProgressDialog progressDialog;

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		cursorAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_2, null, new String[] { "name", "number" }, new int[] { android.R.id.text1, android.R.id.text2 },
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		setListAdapter(cursorAdapter);
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setTitle("正在读取联系人");
		getLoaderManager().initLoader(0, null, this);
		progressDialog.show();
		registerForContextMenu(getListView());

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		menu.add(0, 0, 0, "复制姓名");
		menu.add(0, 1, 1, "复制号码");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		Log.d(logTag, "itemid:" + item.getItemId());
		AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		int position = adapterContextMenuInfo.position;
		Cursor cursor = cursorAdapter.getCursor();
		cursor.moveToPosition(position);
		ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
		switch (item.getItemId())
		{
		case 0:
			int nameColIndex = cursor.getColumnIndexOrThrow("name");
			String name = cursor.getString(nameColIndex);
			clipboardManager.setText(name);
			break;
		case 1:
			int numberColIndex = cursor.getColumnIndexOrThrow("number");
			String number = cursor.getString(numberColIndex);
			clipboardManager.setText(number);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		getActivity().openContextMenu(v);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1)
	{
		Uri uri = Uri.parse("content://icc/adn");
		String[] projection = new String[] {};
		String selection = null;
		String[] selectionArgs = new String[] {};
		String sortOrder = null;
		return new CursorLoader(getActivity(), uri, projection, selection, selectionArgs, sortOrder);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor c)
	{
		for (int i = 0; i < c.getColumnCount(); i++)
		{
			Log.d(logTag, i + ":" + c.getColumnName(i));
		}
		cursorAdapter.swapCursor(c);
		progressDialog.dismiss();
		Toast.makeText(getActivity(), "一共有" + c.getCount() + "个联系人", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0)
	{
		cursorAdapter.swapCursor(null);
	}
}
