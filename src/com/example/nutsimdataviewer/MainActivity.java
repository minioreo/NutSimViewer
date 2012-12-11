package com.example.nutsimdataviewer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

	private void loadData()
	{
		String[] items = new String[]{"收件箱","联系人"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
		this.setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		switch (position)
		{
		case 0:
			Intent inboxIntent = new Intent(this,InboxActivity2.class);
			startActivity(inboxIntent);
			break;
		case 1:
			Intent contactIntent = new Intent(this,ContactActivity.class);
			startActivity(contactIntent);
			break;
		default:
			break;
		}
	}
}
