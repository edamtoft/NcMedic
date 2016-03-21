package com.ncmedic.ncmedic;

import java.util.ArrayList;

import com.ncmedic.ncmedic.R;

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
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class CalculatorPage extends FragmentActivity {

	public final static String EXTRA_CALC_INDEX = "com.ncmedic.ncmedic.CALC_INDEX";

	private FragmentViewPagerAdapter pageAdapter;
	private ArrayList<Fragment> fragments;
	private int page;
	private ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_selector);
		setPreferences();
		setupActionBar();
		Intent intent = getIntent();
		page = intent.getIntExtra(EXTRA_CALC_INDEX, 0);
		fragments = getFragments();
		pager = (ViewPager) findViewById(R.id.menu_selector_pager);
		pageAdapter = new FragmentViewPagerAdapter(
				super.getSupportFragmentManager(), fragments);
		pager.setAdapter(pageAdapter);
		pager.setCurrentItem(page);
		setTitle(pageAdapter.getItem(page).toString());
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int position) {
				page=position;
				setTitle(pageAdapter.getItem(page).toString());
				
			}
			
		});
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_selector, menu);
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
	
	private ArrayList<Fragment> getFragments() {
		ArrayList<Fragment> result = new ArrayList<Fragment>();
		result.add(Fragment.instantiate(this,
				GcsCalculator.class.getName()));
		result.add(Fragment.instantiate(this,
				VialCalculator.class.getName()));
		result.add(Fragment.instantiate(this,
				DripCalculator.class.getName()));
		result.add(Fragment.instantiate(this,
				ApgarCalculator.class.getName()));
		result.add(Fragment.instantiate(this,
				KingCalculator.class.getName()));
		return result;
	}


}
