package com.ncmedic.ncmedic;

import com.ncmedic.ncmedic.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PillId extends Activity implements
		SeekBar.OnSeekBarChangeListener, AdapterView.OnItemSelectedListener {

	public final static String EXTRA_XMLREQUEST_URL = "com.ncmedic.ncmedic.XMLREQUEST_URL";

	private String imprint = "";
	private String shapeCode = "";
	private String colorCode = "";
	private int score = 0;
	private int size = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pill_id);
		setPreferences();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		LinearLayout shapeView = (LinearLayout) findViewById(R.id.pillShapeLayout);
		ImageAdapter adapter = new ImageAdapter(this, getResources()
				.getStringArray(R.array.drug_shapes));
		for (int i = 0; i < adapter.getCount(); i++) {
			View item = adapter.getView(i, null, null);
			shapeView.addView(item);
		}
		SeekBar sizeBar = (SeekBar) findViewById(R.id.pillSizeSeekBar);
		sizeBar.setOnSeekBarChangeListener(this);
		SeekBar scoringBar = (SeekBar) findViewById(R.id.pillScoreSeekBar);
		scoringBar.setOnSeekBarChangeListener(this);
		Spinner colorSpinner = (Spinner) findViewById(R.id.pillColorSpinner);
		colorSpinner.setOnItemSelectedListener(this);
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
		getMenuInflater().inflate(R.menu.pill_id, menu);
		return true;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public void onProgressChanged(SeekBar bar, int position, boolean arg2) {
		if (bar.getId() == R.id.pillSizeSeekBar) {

			TextView sizeText = (TextView) findViewById(R.id.pillSizeTextView);
			int lowerBound = position - 2;
			if (lowerBound <= 0) {
				lowerBound = 0;
			}
			int upperBound = position + 2;
			if (position == 0) {
				sizeText.setText(getResources().getString(R.string.any_size));
			} else {
				sizeText.setText(lowerBound + "-" + upperBound + "mm");
			}
			size = position;
		} else if (bar.getId() == R.id.pillScoreSeekBar) {
			ImageView icon = (ImageView) findViewById(R.id.pillScoringIcon);
			TextView anySize = (TextView) findViewById(R.id.pillScoreTextView);
			switch (position) {
			case 0:
				icon.setVisibility(View.GONE);
				anySize.setVisibility(View.VISIBLE);
				break;
			case 1:
				icon.setVisibility(View.VISIBLE);
				anySize.setVisibility(View.GONE);
				icon.setImageDrawable(getResources().getDrawable(
						R.drawable.pillscore1));
				break;
			case 2:
				icon.setVisibility(View.VISIBLE);
				anySize.setVisibility(View.GONE);
				icon.setImageDrawable(getResources().getDrawable(
						R.drawable.pillscore2));
				break;
			case 3:
				icon.setVisibility(View.VISIBLE);
				anySize.setVisibility(View.GONE);
				icon.setImageDrawable(getResources().getDrawable(
						R.drawable.pillscore3));
				break;
			case 4:
				icon.setVisibility(View.VISIBLE);
				anySize.setVisibility(View.GONE);
				icon.setImageDrawable(getResources().getDrawable(
						R.drawable.pillscore4));
				break;
			}
			score = position;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long arg3) {
		switch (position) {
		case 0:
			colorCode = "";
			break;
		default:
			colorCode = getResources().getStringArray(R.array.drug_colors)[position - 1];
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	public void searchPills(View view) {
		String urlString = "http://pillbox.nlm.nih.gov/PHP/pillboxAPIService.php?key=YUK5984SO0";
		EditText imprintText = (EditText) findViewById(R.id.pillSearchImprint);
		imprint = imprintText.getText().toString();
		int factors = 0;
		if (!imprint.equals("")) {
			String[] imprints = imprint.split("[^a-zA-Z0-9]");
			String imprintString = "";
			for (int i = 0; i < imprints.length; i++) {
				if (imprints[i].length()>0) {
					imprintString += "+" + imprints[i];
				}
			}
			urlString += "&imprint=" + imprintString.substring(1);
			factors++;
		}
		if (shapeCode != "") {
			urlString += "&shape=" + shapeCode;
			factors++;
		}
		if (colorCode != "") {
			urlString += "&color=" + colorCode;
			factors++;
		}
		if (score != 0) {
			urlString += "&score=" + score;
			factors++;
		}
		if (size != 0) {
			urlString += "&size=" + size;
			factors++;
		}
		boolean netState = isNetworkAvailable();
		if ((factors > 0) && netState) {
			Intent intent = new Intent(this, PillSearchResults.class);
			intent.putExtra(EXTRA_XMLREQUEST_URL, urlString);
			startActivity(intent);
		} else if (!netState) {
			Toast.makeText(getApplicationContext(),
					"Check Network Connection.", Toast.LENGTH_SHORT)
					.show();
		}else if (factors <= 0) {
		
			Toast.makeText(getApplicationContext(),
					"You must select at least one variable.", Toast.LENGTH_SHORT)
					.show();
		}

	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	@SuppressWarnings("deprecation")
	public void shapeClick(View view) {
		LinearLayout shapeView = (LinearLayout) findViewById(R.id.pillShapeLayout);
		for (int i = 0; i < shapeView.getChildCount(); i++) {
			shapeView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
		}
		if (!shapeCode.equals(view.getContentDescription())) {
		view.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_bg));
		shapeCode=view.getContentDescription().toString();
		} else {
			shapeCode = "";
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.disclaimer:
			messageBox("Pill Search Disclaimer","This product uses publicly available data from the U.S. National Library of Medicine (NLM), National Institutes of Health, Department of Health and Human Services; NLM is not responsible for the product and does not endorse or recommend this or any other product.", "Close");
			return true;
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
}
