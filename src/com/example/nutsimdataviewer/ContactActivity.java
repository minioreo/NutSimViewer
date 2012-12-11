package com.example.nutsimdataviewer;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class ContactActivity extends InboxActivity2
{
	@Override
	protected void attachListFragment()
	{
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		ContactFragment contactFragment = new ContactFragment();
		transaction.add(android.R.id.content, contactFragment, "contact");
		transaction.commit();
	}
}
