package com.ncmedic.ncmedic;

import com.ncmedic.ncmedic.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

public class GcsCalculator extends Fragment implements AdapterView.OnItemSelectedListener{
	
	private Spinner eyeSpinner;
	private Spinner verbalSpinner;
	private Spinner motorSpinner;
	private TextView gcsOutput;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_gcs_calculator, container, false);
		eyeSpinner = (Spinner) view.findViewById(R.id.gcsEyeSpinner);
		verbalSpinner = (Spinner) view.findViewById(R.id.gcsVerbalSpinner);
		motorSpinner = (Spinner) view.findViewById(R.id.gcsMotorSpinner);
		gcsOutput = (TextView) view.findViewById(R.id.gcsOutput);
		eyeSpinner.setOnItemSelectedListener(this);
		verbalSpinner.setOnItemSelectedListener(this);
		motorSpinner.setOnItemSelectedListener(this);
		printGCS();
		return view;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		printGCS();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	public void printGCS() {
		int eye = 4-eyeSpinner.getSelectedItemPosition();
		int verbal = 5-verbalSpinner.getSelectedItemPosition();
		int motor = 6-motorSpinner.getSelectedItemPosition();
		int gcs = eye+verbal+motor;
		gcsOutput.setText("GCS-"+gcs);

	}
	
	public String toString() {
		return "Glasgow Coma Scale";
	}

}
