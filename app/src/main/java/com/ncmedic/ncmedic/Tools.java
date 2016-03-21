package com.ncmedic.ncmedic;

import java.util.List;

import com.ncmedic.ncmedic.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class Tools extends Fragment implements OnClickListener {

	private SharedPreferences sharedPref;
	
	private boolean poiCon;
	private boolean chemtrec;
	private String hospital;
	private String dispatch;

	private TextView clock;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.activity_tools, container, false);
		clock = (TextView) view.findViewById(R.id.clock);
		updateClock();
		final Handler mHandler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							updateClock();
						}
					});
				}
			}
		}).start();
		view.findViewById(R.id.pillIdButton).setOnClickListener(this);
		view.findViewById(R.id.medevacButton).setOnClickListener(this);
		view.findViewById(R.id.abbrevButton).setOnClickListener(this);
		Context context = getActivity().getApplicationContext();
		sharedPref = context.getSharedPreferences(
				getString(R.string.shared_preferences_key),
				Context.MODE_PRIVATE);
		loadPreferences();
		return view;
	}
	
	private void loadPreferences() {
		poiCon = sharedPref.getBoolean(getActivity().getResources()
				.getString(R.string.poicon_preference_key), true);
		chemtrec =sharedPref.getBoolean(getActivity().getResources()
				.getString(R.string.chemtrec_preference_key), true);
		hospital =sharedPref.getString(getActivity().getResources()
				.getString(R.string.hospital_number_preference_key), "");
		dispatch =sharedPref.getString(getActivity().getResources()
				.getString(R.string.dispatch_number_preference_key), "");
	}


	public void pillId() {
		Intent intent = new Intent(getActivity(), PillId.class);
		startActivity(intent);
	}

	public void medevac() {
		Intent intent = new Intent(getActivity(), MedevacTool.class);
		startActivity(intent);
	}

	public void updateClock() {
		Time time = new Time();
		time.setToNow();
		clock.setText(String.format("%02d", time.hour) + ":"
				+ String.format("%02d", time.minute) + ":"
				+ String.format("%02d", time.second));
	}

	public void abbrev() {
		Intent intent = new Intent(getActivity(), Abbreviations.class);
		startActivity(intent);
	}

	@Override
	public String toString() {
		return "Tools";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pillIdButton:
			pillId();
			break;
		case R.id.medevacButton:
			medevac();
			break;
		case R.id.abbrevButton:
			abbrev();
			break;
		}
	}
	private void showPopup() {

		AlertDialog.Builder popupBuilder = new AlertDialog.Builder(
				getActivity());
		popupBuilder
				.setMessage("This feature is only available in the pro version of NCmedic.");
		popupBuilder.setPositiveButton("Upgrade Now",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Uri addr = Uri
								.parse("http://play.google.com/store/apps/details?id=com.ncmedic.ncmedic");
						Intent upgradeIntent = new Intent(Intent.ACTION_VIEW,
								addr);
						PackageManager packageManager = getActivity()
								.getPackageManager();
						List<ResolveInfo> activities = packageManager
								.queryIntentActivities(upgradeIntent, 0);
						boolean isIntentSafe = activities.size() > 0;

						// Start an activity if it's safe
						if (isIntentSafe) {
							startActivity(upgradeIntent);
						}
					}
				});
		popupBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// Do nothing but close the dialog
					}
				});

		// Remember, create doesn't show the dialog
		AlertDialog helpDialog = popupBuilder.create();
		helpDialog.show();
	}
}
