package com.ncmedic.ncmedic;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentViewPagerAdapter extends FragmentPagerAdapter {
	
	private ArrayList<Fragment> fragments;
	
	public FragmentViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> pages) {
		super(fm);
		fragments = new ArrayList<Fragment>(3);
		fragments.addAll(pages);
	}

	
	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return fragments.get(position).toString();
	}
}
