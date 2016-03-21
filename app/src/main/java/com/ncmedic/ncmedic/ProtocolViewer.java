package com.ncmedic.ncmedic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class ProtocolViewer extends FragmentActivity {

	public final static String EXTRA_PROTOCOL_INDEX = "com.ncmedic.ncmedic.PROTOCOL_INDEX";

	private FragmentViewPagerAdapter pageAdapter;
	private ArrayList<Fragment> fragments;
	private int protocolIndex;
	private ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_protocol_viewer);
		setPreferences();
		setupActionBar();
		Intent intent = getIntent();
		protocolIndex = intent.getIntExtra(EXTRA_PROTOCOL_INDEX, 1);
		fragments = new ArrayList<Fragment>(3);
		List<String> protocolsList;
		try {
			protocolsList = Arrays.asList(getResources().getAssets().list("protocols/"+protocolIndex));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Log.d("Protocols","Opening Protocol "+protocolIndex);
		Log.d("Protocols","Found "+protocolsList.size() + " pages.");
		for (int page = 0; page < protocolsList.size(); page++) {
			Fragment f = Fragment.instantiate(this, ProtocolPage.class.getName());
			Bundle args = new Bundle();
			args.putInt(ProtocolPage.EXTRA_PROTOCOL_INDEX, protocolIndex);
			args.putInt(ProtocolPage.EXTRA_PROTOCOL_PAGE, page);
			f.setArguments(args);
			fragments.add(f);
		}
		pager = (ViewPager) findViewById(R.id.protocolPager);
		pageAdapter = new FragmentViewPagerAdapter(
				super.getSupportFragmentManager(), fragments);
		pager.setAdapter(pageAdapter);

	}
	
	private void setPreferences() {
		Context context = getApplicationContext();
		SharedPreferences sharedPref = context.getSharedPreferences(
				getString(R.string.shared_preferences_key),
				Context.MODE_PRIVATE);
		int bg = sharedPref.getInt(getResources().getString(R.string.background_preference_key), 0);
		switch(bg) {
		case 0:
			getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_gradient));
			break;
		case 1:
			getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_gradient));
			break;
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.protocol_viewer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
