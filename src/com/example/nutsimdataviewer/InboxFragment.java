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
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

public class InboxFragment extends ListFragment implements LoaderCallbacks<Cursor>
{
	private static final String logTag = "InboxFragment";

	private SimpleCursorAdapter cursorAdapter;

	private ProgressDialog progressDlg;

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		cursorAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_2, null, new String[] { "address", "body" }, new int[] { android.R.id.text1, android.R.id.text2 },
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		setListAdapter(cursorAdapter);
		progressDlg = new ProgressDialog(getActivity());
		progressDlg.setMessage("读取中");
		getLoaderManager().initLoader(1, null, this);
		progressDlg.show();
		registerForContextMenu(getListView());
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, 0, 0, "复制号码");
		menu.add(0, 1, 1, "复制内容");
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
		ClipboardManager m = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
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

	private void log(Object str)
	{
		Log.d(logTag, str.toString());
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		getActivity().openContextMenu(v);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1)
	{
		Log.d(logTag, "onCreateLoader");
		Uri uri = Uri.parse("content://sms/icc");
		String[] projection = new String[] { "body" };
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = null;
		return new CursorLoader(getActivity(), uri, projection, selection, selectionArgs, sortOrder);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor c)
	{
		Log.d(logTag, "onLoadFinished");
		Log.d(logTag, "count:" + c.getCount());
		cursorAdapter.swapCursor(c);
		progressDlg.dismiss();
		Toast.makeText(getActivity(), "一共有" + c.getCount() + "条短信", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0)
	{
		Log.d(logTag, "onLoaderReset");
		cursorAdapter.swapCursor(null);
	}
}
