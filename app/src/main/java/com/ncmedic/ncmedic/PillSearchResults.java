package com.ncmedic.ncmedic;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.ncmedic.ncmedic.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class PillSearchResults extends Activity {
	private List<Pill> pills;
	private ProgressBar loading;
	private ListView pillList;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pill_search_results);
		setPreferences();
		pills = new ArrayList<Pill>(300);
		loading = (ProgressBar) findViewById(R.id.pillSearchResultsLoading);
		pillList = (ListView) findViewById(R.id.pillResultsListView);
		pillList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				showDrugInfo(pills.get(position));
			}

		});
		@SuppressWarnings("deprecation")
		final Object data = getLastNonConfigurationInstance();
		if (data != null) {
			pills = (ArrayList<Pill>) data;
		}
		else {
			if (isNetworkAvailable()) {
				Intent intent = getIntent();
				String urlString = intent
						.getStringExtra(PillId.EXTRA_XMLREQUEST_URL);
				new GetXMLTask().execute(urlString);
	
			} else {
				Toast.makeText(getApplicationContext(),
						"Check Network Connection.", Toast.LENGTH_LONG).show();
				super.onBackPressed();
			}
		}
		pillList.setAdapter(new PillViewAdapter(getBaseContext(), pills));
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
		getMenuInflater().inflate(R.menu.pill_search_results, menu);
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
	private class GetXMLTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urlString) {
			loading.setVisibility(View.VISIBLE);
			String xml = null;
			StringBuffer output = new StringBuffer("");

			InputStream stream = null;
			URL url;
			try {
				url = new URL(urlString[0]);
				URLConnection connection = url.openConnection();

				HttpURLConnection httpConnection = (HttpURLConnection) connection;
				httpConnection.setRequestMethod("GET");
				httpConnection.connect();

				if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					stream = httpConnection.getInputStream();
					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(stream));
					String s = "";
					while ((s = buffer.readLine()) != null)
						output.append(s);
				}
			} catch (MalformedURLException e) {
				Toast.makeText(
						getApplicationContext(),
						"An Error Occured. Please Try Again. (Malformed URL Error)",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(),
						"An Error Occured. Please Try Again. (IO Exception)",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}

			xml = output.toString();
			return xml;
		}

		@Override
		protected void onPostExecute(String xml) {
			if (!xml.equalsIgnoreCase("No records found")) {
				InputStream in = new ByteArrayInputStream(xml.getBytes());

				try {
					XmlPullParser parser = Xml.newPullParser();
					parser.setInput(in, "utf-8");
					int eventType = parser.getEventType();
					String XmlText = "";
					Pill pill = new Pill();
					while (eventType != XmlPullParser.END_DOCUMENT) {
						if (eventType == XmlPullParser.END_TAG) {
							if (parser.getName().equals("pill")) {
								if (!pills.contains(pill)) {
									pills.add(pill);
								}
								pill = new Pill();
							} else if (parser.getName().equals("SPLIMPRINT")) {
								pill.setImprint(XmlText);
							} else if (parser.getName().equals("INGREDIENTS")) {
								pill.setActiveIngredients(XmlText);
							} else if (parser.getName().equals("RXSTRING")) {
								pill.setDosages(XmlText);
							} else if (parser.getName().equals("SPLCOLOR")) {
								pill.setColorCode(XmlText);
							} else if (parser.getName().equals("SPLSHAPE")) {
								pill.setShapeCode(XmlText);
							} else if (parser.getName().equals("SPLSCORE")) {
								try {
									pill.setScore(Integer.parseInt(XmlText));
								} catch (NumberFormatException e) {
									e.printStackTrace();
								}
							} else if (parser.getName().equals("SPLSIZE")) {
								try {
									pill.setSize(Double.parseDouble(XmlText));
								} catch (NumberFormatException e) {
									e.printStackTrace();
								}
							} else if (parser.getName().equalsIgnoreCase("dea")) {
								pill.setDeaSchedule(XmlText);
							}

						} else if (eventType == XmlPullParser.TEXT) {
							XmlText = parser.getText();
						}
						eventType = parser.next();

					}
					in.close();
					ListView listView = (ListView) findViewById(R.id.pillResultsListView);
					listView.setAdapter(new PillViewAdapter(getBaseContext(),
							pills));
					Toast.makeText(getApplicationContext(),
							pills.size() + " results found.", Toast.LENGTH_LONG)
							.show();

				} catch (XmlPullParserException e) {
					Toast.makeText(
							getApplicationContext(),
							"Connection Error. Please Try Again. (Parser Error)",
							Toast.LENGTH_LONG).show();
					e.printStackTrace();
				} catch (IOException e) {
					Toast.makeText(
							getApplicationContext(),
							"Connection Error. Please Try Again (IO Error)",
							Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getApplicationContext(), "No Records Found",
						Toast.LENGTH_LONG).show();
			}
			loading.setVisibility(View.GONE);
		}
	}

	private void showDrugInfo(Pill pill) {
		String message = "";
		String title = "";
		message += "Dosages: " + pill.getDosages() + "\n\n";
		if (!pill.getImprint().equals("")) {
			String imprintMessage = pill.getImprint().replace(";", ", ");
			message += "Imprint: " + imprintMessage + "\n\n";
		}
		if (!pill.getColorCode().equals("")) {
			String[] colors = pill.getColorCode().split(";");
			String colorMessage = "";
			if (colors.length > 1) {
				for (int i = 0; i < colors.length; i++) {
					colorMessage += Pill.translateColorCode(colors[i]) + ", ";
				}
			} else {
				colorMessage = Pill.translateColorCode(colors[0]);
			}
			message += "Color: " + colorMessage + "\n\n";
		}
		if (!pill.getDeaSchedule().equals("")) {
			message += "DEA Schedule : " + pill.getDeaSchedule() + "\n\n";
		}
		title = pill.getActiveIngredients();
		AlertDialog.Builder popupBuilder = new AlertDialog.Builder(this);
		popupBuilder.setMessage(message).setTitle(title);
		popupBuilder.setNeutralButton("Close",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		AlertDialog drugInfo = popupBuilder.create();
		drugInfo.show();
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	@Override
	public Object onRetainNonConfigurationInstance() {
	    return pills;
	}
}
