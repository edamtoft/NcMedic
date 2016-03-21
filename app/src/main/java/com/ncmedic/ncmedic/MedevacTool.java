package com.ncmedic.ncmedic;

import java.text.DecimalFormat;

import com.ncmedic.ncmedic.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MedevacTool extends Activity implements LocationListener {
	private TextView latitudeField;
	private TextView longitudeField;
	private TextView accuracyField;
	private TextView altitudeField;
	private RelativeLayout gpsSearch;
	private Button smsButton;
	private LocationManager locationManager;
	private String provider;
	private Location location;
	private double latitude;
	private double longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medevac_tool);
		setPreferences();
		latitudeField = (TextView) findViewById(R.id.latitudeField);
		longitudeField = (TextView) findViewById(R.id.longitudeField);
		accuracyField = (TextView) findViewById(R.id.accuracyField);
		altitudeField = (TextView) findViewById(R.id.altitudeField);
		gpsSearch = (RelativeLayout) findViewById(R.id.gpsSearchRelativeLayout);
		smsButton = (Button) findViewById(R.id.smsButton);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
	protected void onStart() {
		super.onStart();
		boolean enabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!enabled) {
			AlertDialog.Builder popupBuilder = new AlertDialog.Builder(this);
			popupBuilder.setMessage("GPS is disabled in your settings.")
					.setTitle("GPS Disabled");
			popupBuilder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			popupBuilder.setPositiveButton("Settings",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(
									Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(intent);
						}
					});
			AlertDialog messageBox = popupBuilder.create();
			messageBox.show();
		} else {
			Criteria criteria = new Criteria();
			provider = locationManager.getBestProvider(criteria, false);
			location = locationManager.getLastKnownLocation(provider);
			System.out.println("Provider " + provider + " has been selected.");
			locationManager.requestLocationUpdates(provider, 1000, 0, this);
			onLocationChanged(location);
			clearLocation("Waiting for Fix", true);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		locationManager.removeUpdates(this);
		clearLocation("--", false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.medevac_tool, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		updateLocation();
	}

	@Override
	public void onProviderDisabled(String arg0) {
		clearLocation("GPS Disabled", false);
	}

	@Override
	public void onProviderEnabled(String arg0) {
		clearLocation("Waiting for Fix", true);

	}

	@Override
	public void onStatusChanged(String arg0, int status, Bundle arg2) {

	}

	public void sendSms(View view) {
		DecimalFormat df = new DecimalFormat("0.00000");
		String message = "LZ at: " + df.format(latitude) + ", "
				+ df.format(longitude);
		Intent it = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"));
		it.putExtra("sms_body", message);
		startActivity(it);
	}

	private void updateLocation() {
		DecimalFormat df = new DecimalFormat("0.00000");
		location = locationManager.getLastKnownLocation(provider);
		Time time = new Time();
		time.setToNow();
		long elapsed = 9999;
		try {
			elapsed = (time.toMillis(true) - location.getTime());
		} catch (NullPointerException e) {
			clearLocation("GPS Error", false);
			e.printStackTrace();
		};
		if (elapsed > 1000) {
			clearLocation("Waiting for Fix", true);
		} else {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			double acc = location.getAccuracy() * 3.28;
			double alt = location.getAltitude() * 3.28;
			latitudeField.setText("lat: " + df.format(latitude));
			longitudeField.setText("long: " + df.format(longitude));
			accuracyField.setText("Accuracy: " + Math.round(acc) + "ft");
			altitudeField.setText("Altitude: " + Math.round(alt) + "ft");
			smsButton.setEnabled(true);
			gpsSearch.setVisibility(View.INVISIBLE);
			latitudeField.setEnabled(true);
			longitudeField.setEnabled(true);
			latitudeField.setVisibility(View.VISIBLE);
			longitudeField.setVisibility(View.VISIBLE);
			
		}
	}

	private void clearLocation(String s, boolean searching) {
		latitudeField.setText("Lat: " + s);
		longitudeField.setText("Long: " + s);
		accuracyField.setText("Accuracy: N/A");
		altitudeField.setText("Altitude: N/A");
		smsButton.setEnabled(false);
		latitudeField.setEnabled(false);
		longitudeField.setEnabled(false);
		if (searching) {
			latitudeField.setVisibility(View.INVISIBLE);
			longitudeField.setVisibility(View.INVISIBLE);
			gpsSearch.setVisibility(View.VISIBLE);
		} else {
			latitudeField.setVisibility(View.VISIBLE);
			longitudeField.setVisibility(View.VISIBLE);
			gpsSearch.setVisibility(View.INVISIBLE);
		}
	}
}
