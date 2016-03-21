package com.ncmedic.ncmedic;

import com.ncmedic.ncmedic.R;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

public class KingCalculator extends Fragment implements AdapterView.OnItemSelectedListener{

	private Spinner heightSelector;
	private TextView kingInflateTo;
	private TextView kingOutput;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_king_calculator, container, false);
		heightSelector = (Spinner) view.findViewById(R.id.kingHeightSelector);
		kingInflateTo = (TextView) view.findViewById(R.id.kingInflateTo);
		kingOutput = (TextView) view.findViewById(R.id.kingOutput);
		heightSelector.setOnItemSelectedListener(this);
		updateKingSize();
		return view;
	}

	

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		updateKingSize();
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	public void updateKingSize() {
		String size = new String();
		int color = Color.BLACK;
		String inflateTo = new String();
		switch (heightSelector.getSelectedItemPosition()) {
		case 0:
			size = "5";
			inflateTo = "60-80";
			color = Color.parseColor("#5f2874");
			break;
		case 1:
			size = "4";
			inflateTo = "50-70";
			color = Color.parseColor("#b80000");
			break;
		case 2:
			size = "3";
			inflateTo = "40-55";
			color = Color.parseColor("#d29400");
			break;
		case 3:
			size = "2.5";
			inflateTo = "30-40";
			color = Color.parseColor("#ff4e00");
			break;
		case 4:
			size = "2";
			inflateTo = "25-35";
			color = Color.parseColor("#147b00");
			break;
		}
		kingOutput.setText("Size "+size);
		kingOutput.setBackgroundColor(color);
		kingInflateTo.setText("Inflate with "+inflateTo+"mL");
	}
	
	public String toString() {
		return "King Airway Sizes";
	}
}
