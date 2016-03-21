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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class Calculators extends Fragment implements OnClickListener {
	private SharedPreferences sharedPref;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.activity_calculators, container,
				false);
		view.findViewById(R.id.gcsCalcButton).setOnClickListener(this);
		view.findViewById(R.id.dosageCalcButton).setOnClickListener(this);
		view.findViewById(R.id.dripCalcButton).setOnClickListener(this);
		view.findViewById(R.id.apgarCalcButton).setOnClickListener(this);
		view.findViewById(R.id.kingCalcButton).setOnClickListener(this);
		Context context = getActivity().getApplicationContext();
		sharedPref = context.getSharedPreferences(
				getString(R.string.shared_preferences_key),
				Context.MODE_PRIVATE);
		return view;
	}


	public void openGcsCalc() {
		Intent intent = new Intent(getActivity(), CalculatorPage.class);
		intent.putExtra(CalculatorPage.EXTRA_CALC_INDEX, 0);
		startActivity(intent);
	}

	public void openVialCalc() {
		Intent intent = new Intent(getActivity(), CalculatorPage.class);
		intent.putExtra(CalculatorPage.EXTRA_CALC_INDEX, 1);
		startActivity(intent);
	}

	public void openDripCalc() {
		Intent intent = new Intent(getActivity(), CalculatorPage.class);
		intent.putExtra(CalculatorPage.EXTRA_CALC_INDEX, 2);
		startActivity(intent);
	}

	public void openApgarCalc() {
		Intent intent = new Intent(getActivity(), CalculatorPage.class);
		intent.putExtra(CalculatorPage.EXTRA_CALC_INDEX, 3);
		startActivity(intent);
	}

	public void openKingCalc() {
		Intent intent = new Intent(getActivity(), CalculatorPage.class);
		intent.putExtra(CalculatorPage.EXTRA_CALC_INDEX, 4);
		startActivity(intent);
	}

	@Override
	public String toString() {
		return "Calculators";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gcsCalcButton:
			openGcsCalc();
			break;
		case R.id.dosageCalcButton:
			openVialCalc();
			break;
		case R.id.dripCalcButton:
			openDripCalc();
			break;
		case R.id.apgarCalcButton:
			openApgarCalc();
			break;
		case R.id.kingCalcButton:
			openKingCalc();
			break;
		}
	}
}
