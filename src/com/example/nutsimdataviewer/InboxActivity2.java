package com.example.nutsimdataviewer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.android.debug.hv.ViewServer;

public class InboxActivity2 extends FragmentActivity
{

	private static String logTag = "InboxActivity2";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		attachListFragment();
		ViewServer.get(this).addWindow(this);

	}

	protected void attachListFragment()
	{
		// setContentView(R.layout.activity_inbox);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		InboxFragment inboxFragment = new InboxFragment();
		transaction.add(android.R.id.content, inboxFragment, "inbox");
		transaction.commit();
		// fragmentManager.executePendingTransactions();
		log("inbox is:" + (fragmentManager.findFragmentByTag("inbox")));
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		log("inbox is:" + (getSupportFragmentManager().findFragmentByTag("inbox")));
	}

	protected void log(Object str)
	{
		if (str == null)
		{
			Log.d(logTag, "null");
		}
		else
		{
			Log.d(logTag, str.toString());
		}

	}
}
