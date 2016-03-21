package com.ncmedic.ncmedic;

import java.text.DecimalFormat;

import com.ncmedic.ncmedic.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class DripCalculator extends Fragment implements
AdapterView.OnItemSelectedListener, TextWatcher{
	private EditText dripVolume;
	private Spinner dripSet;
	private EditText dripTime;
	private Spinner dripTimeUnits;
	private TextView dripOutput;
	private TextView dripInverse;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_drip_calculator, container, false);
		dripVolume = (EditText) view.findViewById(R.id.dripVolume);
		dripSet = (Spinner) view.findViewById(R.id.dripSet);
		dripTime = (EditText) view.findViewById(R.id.dripTime);
		dripTimeUnits = (Spinner) view.findViewById(R.id.dripTimeUnits);
		dripOutput = (TextView) view.findViewById(R.id.dripOutput);
		dripInverse = (TextView) view.findViewById(R.id.dripInverse);
		dripVolume.addTextChangedListener(this);
		dripSet.setOnItemSelectedListener(this);
		dripTime.addTextChangedListener(this);
		dripTimeUnits.setOnItemSelectedListener(this);
		return view;
	}

	public void calculateDrip() {
		
		double volume = 0;
		double mins = 0;
		double gttsPerMin = 0;
		volume = parse(dripVolume.getText().toString());
		switch (dripSet.getSelectedItemPosition()) {
		case 0:
			gttsPerMin = 60;
			break;
		case 1:
			gttsPerMin = 20;
			break;
		case 2:
			gttsPerMin = 15;
			break;
		case 3:
			gttsPerMin = 10;
			break;
		}
		switch (dripTimeUnits.getSelectedItemPosition()) {
		case 0:
			mins = parse(dripTime.getText().toString());
			break;
		case 1:
			mins = (parse(dripTime.getText().toString())*60);
		}
		if (volume!=0 && mins!=0 && gttsPerMin!=0){
			double outputDrops = (volume * gttsPerMin) / mins;
			dripOutput.setText(round(outputDrops) + "gtts/min");
			double dripInv = 60/outputDrops;
			dripInverse.setText("1 drip every "+round(dripInv)+" seconds");
		} else {
			dripOutput.setText(R.string.pending_drip);
			dripInverse.setText(R.string.pending_drop_inverse);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		calculateDrip();
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent0) {
		// TODO Auto-generated method stub
		
	}
	public double parse(String text) {
		try {
			return Double.parseDouble(text);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	double round(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
    return Double.valueOf(twoDForm.format(d));
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
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		calculateDrip();
		
	}
	
	public String toString() {
		return "Fluid over Time";
	}
}
