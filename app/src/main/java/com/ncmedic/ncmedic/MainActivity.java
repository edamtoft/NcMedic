package com.ncmedic.ncmedic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends Activity implements
		AdapterView.OnItemClickListener {
	private AutoCompleteTextView omniSearch;
    private static Pattern protocolPattern = Pattern.compile("^(\\d+)\\s+(.*)");

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setPreferences();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		ArrayList<String> allArray = new ArrayList<String>();
		allArray.addAll(Arrays.asList(getResources().getStringArray(
				R.array.all_protocols)));
		allArray.addAll(Arrays.asList(getResources().getStringArray(
				R.array.drugs)));
		omniSearch = (AutoCompleteTextView) findViewById(R.id.omniSearchBox);
		FilterArrayAdapter adapter = new FilterArrayAdapter(this,
				android.R.layout.simple_spinner_dropdown_item, allArray);
		omniSearch.setAdapter(adapter);
		omniSearch.setThreshold(1);
		omniSearch.setOnItemClickListener(this);
		omniSearch.setOnEditorActionListener(new OnEditorActionListener() {

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

	}

	protected void onResume() {
		super.onResume();
		setPreferences();
	}
	private void setPreferences() {
		Context context = getApplicationContext();
		SharedPreferences sharedPref = context.getSharedPreferences(
				getString(R.string.shared_preferences_key),
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		int bg = sharedPref
				.getInt(getResources().getString(
						R.string.background_preference_key), 0);
		switch (bg) {
		case 0:
			getWindow().setBackgroundDrawable(
					getResources().getDrawable(R.drawable.blue_gradient));
			break;
		case 1:
			getWindow().setBackgroundDrawable(
					getResources().getDrawable(R.drawable.grey_gradient));
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void openProtocols(View view) {
		Intent intent = new Intent(this, MenuSelector.class);
		intent.putExtra(MenuSelector.EXTRA_PAGE_INDEX, 0);
		startActivity(intent);
	}

	public void openDrugs(View view) {
		Intent intent = new Intent(this, MenuSelector.class);
		intent.putExtra(MenuSelector.EXTRA_PAGE_INDEX, 1);
		startActivity(intent);
	}

	public void openCalcs(View view) {
		Intent intent = new Intent(this, MenuSelector.class);
		intent.putExtra(MenuSelector.EXTRA_PAGE_INDEX, 2);
		startActivity(intent);
	}

	public void openTools(View view) {
		Intent intent = new Intent(this, MenuSelector.class);
		intent.putExtra(MenuSelector.EXTRA_PAGE_INDEX, 3);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.about:
			PackageInfo pInfo = null;
			try {

				pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			String version = pInfo.versionName;
			int code = pInfo.versionCode;
			String message = "Version Code: " + code + "\n\nVersion: "
					+ version
					+ "\n\n(c) 2013 Eric Damtoft. All rights reserved";
			messageBox("About NCmedic", message, "Close");
			return true;
		case R.id.action_settings:
			Intent intent = new Intent(this, AppSettings.class);
			startActivity(intent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void messageBox(String title, String message, String button) {
		AlertDialog.Builder popupBuilder = new AlertDialog.Builder(this);
		popupBuilder.setMessage(message).setTitle(title);
		popupBuilder.setNeutralButton(button,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		AlertDialog messageBox = popupBuilder.create();
		messageBox.show();

	}

	private void openProtocol(int index) {
		Intent intent = new Intent(this, ProtocolViewer.class);
		intent.putExtra(ProtocolViewer.EXTRA_PROTOCOL_INDEX, index);
		startActivity(intent);
	}

	private void openDrugPage(int index) {
		Intent intent = new Intent(this, DrugPicker.class);
		intent.putExtra(DrugPicker.EXTRA_DRUG_INDEX, index);
		startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		String protocolString = omniSearch.getText().toString();
		Matcher matcher = protocolPattern.matcher(protocolString);
		omniSearch.setText("");
		if (matcher.find()) {
			openProtocol(Integer.parseInt(matcher.group(1)));
			return;
		}
		List<String> drugList = Arrays.asList(getResources()
					.getStringArray(R.array.drugs));
		int position = drugList.indexOf(protocolString);
		openDrugPage(position);
	}
}
