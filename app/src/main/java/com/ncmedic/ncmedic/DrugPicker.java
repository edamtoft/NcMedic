package com.ncmedic.ncmedic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class DrugPicker extends Activity implements
		AdapterView.OnItemClickListener, OnTouchListener {
	
	public final static String EXTRA_DRUG_INDEX = "com.ncmedic.ncmedic.DRUG_INDEX";

	private AutoCompleteTextView drugSearch;
	private LinearLayout drugNameSearch;
	private TextView indicationsView;
	private TextView adultDosageView;
	private TextView pedDosageView;
	private TextView drugTitle;
	private LinearLayout drugInfoView;
	private ScrollView drugInfoScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drug_picker);
		setPreferences();
		String[] drugArray = getResources().getStringArray(R.array.drugs);
		indicationsView = (TextView) findViewById(R.id.indicationsView);
		adultDosageView = (TextView) findViewById(R.id.adultDosageView);
		pedDosageView = (TextView) findViewById(R.id.pedDosageView);
		drugTitle = (TextView) findViewById(R.id.emsDrugTitle);
		drugNameSearch = (LinearLayout) findViewById(R.id.drugNameSearch);
		drugSearch = (AutoCompleteTextView) findViewById(R.id.drugSearchBox);
		drugInfoView = (LinearLayout) findViewById(R.id.drugInfoView);
		drugInfoScrollView = (ScrollView) findViewById(R.id.drugInfoScrollView);
		drugInfoScrollView.setOnTouchListener(this);
		drugSearch.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView view, int actionID,
					KeyEvent event) {
				if (actionID == EditorInfo.IME_ACTION_DONE) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}
				return false;
			}

		});
		
		FilterArrayAdapter adapter = new FilterArrayAdapter(this,
				android.R.layout.simple_spinner_dropdown_item, Arrays.asList(drugArray));
		drugSearch.setAdapter(adapter);
		drugSearch.setThreshold(1);
		drugSearch.setOnItemClickListener(this);
		int drugIndex;
		Intent intent = getIntent();
		drugIndex = intent.getIntExtra(EXTRA_DRUG_INDEX, -1);

		if (drugIndex >= 0) {
			try {
				displayData(drugIndex);
				setEnabled(true);
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.drug_picker, menu);
		return true;

	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_search:
			if (drugNameSearch.getVisibility() == View.VISIBLE) {
				drugNameSearch.setVisibility(View.GONE);
			} else {
				drugNameSearch.setVisibility(View.VISIBLE);
				drugNameSearch.requestFocus();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(drugNameSearch, InputMethodManager.SHOW_IMPLICIT);
			}
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void displayData(int drugIndex) throws XmlPullParserException,
			IOException {
		XmlResourceParser parser = getResources().getXml(R.xml.drugs);

		List<String> indications = new ArrayList<String>();
		List<String> adultDosage = new ArrayList<String>();
		List<String> pedDosage = new ArrayList<String>();
		int eventType = parser.getEventType();
		int currentDrugIndex = 0;
		String drugName = new String();
		boolean collect = false;
		boolean collectIndication = false;
		boolean collectAdDose = false;
		boolean collectPedDose = false;
		while (eventType != XmlPullParser.END_DOCUMENT) {

			if (eventType == XmlPullParser.START_DOCUMENT) {
			} else if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("drug")
						&& (currentDrugIndex == drugIndex)) {
					collect = true;
					drugName = parser.getAttributeValue(0);
				}
				;
				if (parser.getName().equals("i") && (collect == true)) {
					collectIndication = true;
				} else if (parser.getName().equals("adult_dose")
						&& (collect == true)) {
					collectAdDose = true;
				} else if (parser.getName().equals("ped_dose")
						&& (collect == true)) {
					collectPedDose = true;
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("drug")) {
					collect = false;
					currentDrugIndex++;
				}
			} else if (eventType == XmlPullParser.TEXT) {
				if (collectIndication == true) {
					indications.add(parser.getText());
				} else if (collectAdDose == true) {
					adultDosage.add(parser.getText());
				} else if (collectPedDose == true) {
					pedDosage.add(parser.getText());
				}
				collectIndication = false;
				collectAdDose = false;
				collectPedDose = false;
			}
			eventType = parser.next();
		}
		parser.close();
		String indic = new String();
		int indicSize = indications.size();
		indic = "<p>";
		for (int i = 0; i < indicSize; i++) {
			indic = (indic + "&#8226;" + indications.get(i));
			if (i != indicSize - 1) {
				indic = indic + "</p><p>";
			}
		}
		indic += "</p>";
		String aDose = new String();
		int aDoseSize = adultDosage.size();
		aDose = "<p>";
		for (int i = 0; i < aDoseSize; i++) {
			aDose = (aDose + "&#8226;" + adultDosage.get(i));
			if (i != aDoseSize - 1) {
				aDose = aDose + "</p><p>";
			}
		}
		aDose += "</p>";
		String pDose = new String();
		int pDoseSize = pedDosage.size();
		pDose = "<p>";
		for (int i = 0; i < pDoseSize; i++) {
			pDose = (pDose + "&#8226;" + pedDosage.get(i));
			if (i != pDoseSize - 1) {
				pDose = pDose + "</p><p>";
			}
		}
		pDose += "</p>";
		indicationsView.setText(Html.fromHtml(indic));
		adultDosageView.setText(Html.fromHtml(aDose));
		pedDosageView.setText(Html.fromHtml(pDose));
		drugTitle.setText(drugName);
		setTitle(drugName);
		drugNameSearch.setVisibility(View.GONE);
		drugInfoView.setVisibility(View.VISIBLE);
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(drugSearch.getWindowToken(), 0);
		Intent intent = getIntent();
		intent.putExtra(EXTRA_DRUG_INDEX, drugIndex);
	}

	public void openDrugList(View view) {
		Intent intent = new Intent(this, ProtocolViewer.class);
		intent.putExtra(ProtocolViewer.EXTRA_PROTOCOL_INDEX, 999);
		startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int p, long array) {
		List<String> drugList = Arrays.asList(getResources().getStringArray(
				R.array.drugs));
		int position = drugList.indexOf(drugSearch.getText().toString());
		try {
			setEnabled(true);
			displayData(position);
			drugSearch.setText("");
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void setEnabled(boolean enabledValue) {
		findViewById(R.id.drugIndicationsTitle).setEnabled(enabledValue);
		findViewById(R.id.indicationsView).setEnabled(enabledValue);
		findViewById(R.id.drugAdultDosageTitle).setEnabled(enabledValue);
		findViewById(R.id.adultDosageView).setEnabled(enabledValue);
		findViewById(R.id.drugPediatricDosageTitle).setEnabled(enabledValue);
		findViewById(R.id.pedDosageView).setEnabled(enabledValue);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		if (!drugTitle.getText().toString().equals(getResources().getString(R.string.pending))) {
		drugNameSearch.setVisibility(View.GONE);
		}
		return false;
	}

}