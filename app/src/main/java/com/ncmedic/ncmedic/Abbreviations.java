package com.ncmedic.ncmedic;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.ncmedic.ncmedic.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

public class Abbreviations extends Activity implements TextWatcher {

	ArrayList<String[]> abbreviations = new ArrayList<String[]>(180);
	ListView abbrevList;
	EditText filterEditText;
	AbbreviationAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_abbreviations);
		// Show the Up button in the action bar.
		setPreferences();
		setupActionBar();
		abbrevList = (ListView) findViewById(R.id.abbreviationsListView1);
		filterEditText = (EditText) findViewById(R.id.abbreviationsFilterEditText);
		filterEditText.addTextChangedListener(this);
		try {
			getAbbreviations();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adapter = new AbbreviationAdapter(getBaseContext() , abbreviations);
		abbrevList.setAdapter(adapter);
		abbrevList.setTextFilterEnabled(true);

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
		getMenuInflater().inflate(R.menu.abbreviations, menu);
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

	private void getAbbreviations() throws XmlPullParserException, IOException {
		XmlResourceParser parser = getResources().getXml(R.xml.abbreviations);
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			String tag = parser.getName();
			if (eventType == XmlPullParser.START_TAG && tag.equals("abbrev")) {
				String[] abbrev = new String[2];
				for (int i = 0; i < parser.getAttributeCount(); i++) {
					if (parser.getAttributeName(i).equals("item")) {
						abbrev[0] = parser.getAttributeValue(i);
					} else if (parser.getAttributeName(i).equals("def")) {
						abbrev[1] = parser.getAttributeValue(i);
					}
				}
				abbreviations.add(abbrev);
			}
			eventType = parser.next();
		}
		parser.close();
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence c, int arg1, int arg2, int arg3) {
		adapter.getFilter().filter(c);
	}

}
