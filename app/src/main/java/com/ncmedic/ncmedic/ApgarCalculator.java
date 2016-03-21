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

public class ApgarCalculator extends Fragment implements AdapterView.OnItemSelectedListener {

	private Spinner appearance;
	private Spinner pulse;
	private Spinner grimmace;
	private Spinner activity;
	private Spinner resp;
	private TextView output;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_apgar_calculator, container, false);
		appearance = (Spinner) view.findViewById(R.id.apgarAppearanceSpinner);
		pulse = (Spinner) view.findViewById(R.id.apgarPulseSpinner);
		grimmace = (Spinner) view.findViewById(R.id.apgarGrimmaceSpinner);
		activity = (Spinner) view.findViewById(R.id.apgarActivitySpinner);
		resp = (Spinner) view.findViewById(R.id.apgarRespSpinner);
		output = (TextView) view.findViewById(R.id.apgarOutput);
		appearance.setOnItemSelectedListener(this);
		pulse.setOnItemSelectedListener(this);
		grimmace.setOnItemSelectedListener(this);
		activity.setOnItemSelectedListener(this);
		resp.setOnItemSelectedListener(this);
		printApgar();
		return view;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		printApgar();
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	public void printApgar() {

		int apgar = appearance.getSelectedItemPosition()+
				pulse.getSelectedItemPosition()+
				grimmace.getSelectedItemPosition()+
				activity.getSelectedItemPosition()+
				resp.getSelectedItemPosition();
		output.setText(apgar+"/10");
	}
	
	public String toString() {
		return "APGAR Score";
	}
}
