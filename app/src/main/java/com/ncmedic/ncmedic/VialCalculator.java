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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class VialCalculator extends Fragment implements
		AdapterView.OnItemSelectedListener, TextWatcher {
	private EditText desiredDosage;
	private Spinner desiredDosageUnits;
	private EditText ptWeight;
	private Spinner ptWeightUnits;
	private EditText drugConc;
	private Spinner drugConcUnits;
	private EditText vialVolume;
	private Spinner vialVolumeUnits;
	private TextView output;
	private LinearLayout weightSelector;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_vial_calculator,
				container, false);
		desiredDosage = (EditText) view.findViewById(R.id.vialDesDose);
		desiredDosageUnits = (Spinner) view.findViewById(R.id.vialDesDoseUnits);
		ptWeight = (EditText) view.findViewById(R.id.vialPtWeight);
		ptWeightUnits = (Spinner) view.findViewById(R.id.vialPtWeightUnits);
		drugConc = (EditText) view.findViewById(R.id.vialDrugConc);
		drugConcUnits = (Spinner) view.findViewById(R.id.vialDrugConcUnits);
		vialVolume = (EditText) view.findViewById(R.id.vialVialVolume);
		vialVolumeUnits = (Spinner) view.findViewById(R.id.vialVialVolumeUnits);
		output = (TextView) view.findViewById(R.id.vialOutput);
		weightSelector = (LinearLayout) view
				.findViewById(R.id.vialWeightSelector);
		desiredDosage.addTextChangedListener(this);
		desiredDosageUnits.setOnItemSelectedListener(this);
		ptWeight.addTextChangedListener(this);
		ptWeightUnits.setOnItemSelectedListener(this);
		drugConc.addTextChangedListener(this);
		drugConcUnits.setOnItemSelectedListener(this);
		vialVolume.addTextChangedListener(this);
		vialVolumeUnits.setOnItemSelectedListener(this);
		return view;
	}

	public void calculateVial() {
		double doseMg = 0;
		double drugMg = 0;
		double vialML = 0;
		try {
			int ptWtUnitPosition = ptWeightUnits.getSelectedItemPosition();
			double desDose = parse(desiredDosage.getText().toString());
			double ptWt = parse(ptWeight.getText().toString());
			switch (desiredDosageUnits.getSelectedItemPosition()) {
			case 0:
				doseMg = desDose;
				break;
			case 1:
				switch (ptWtUnitPosition) {
				case 0:

					doseMg = desDose * (ptWt * 0.45);
					break;
				case 1:
					doseMg = (parse(desiredDosage.getText().toString()))
							* (ptWt);
					break;
				}
				break;
			case 2:
				switch (ptWtUnitPosition) {
				case 0:

					doseMg = (desDose / 1000) * (ptWt * 0.45);
					break;
				case 1:
					doseMg = (desDose / 1000) * (ptWt);
					break;
				}
				break;
			}
		} catch (NumberFormatException e) {
		}

		switch (drugConcUnits.getSelectedItemPosition()) {
		case 0:
			drugMg = parse(drugConc.getText().toString());
			break;
		case 1:
			drugMg = (parse(drugConc.getText().toString()) / 1000);
			break;
		}
		switch (vialVolumeUnits.getSelectedItemPosition()) {
		case 0:
			vialML = parse(vialVolume.getText().toString());
			break;
		case 1:
			vialML = parse(vialVolume.getText().toString());
			break;
		}
		if (drugMg != 0 && vialML != 0 && doseMg != 0) {
			double outputML = (doseMg * vialML) / drugMg;
			output.setText(round(outputML) + "mL");
		} else {
			output.setText(R.string.pendindML);
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent.getId() == desiredDosageUnits.getId()) {
			if (position == 0) {
				weightSelector.setVisibility(View.GONE);
			} else {
				weightSelector.setVisibility(View.VISIBLE);
			}
		}
		calculateVial();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
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
		calculateVial();

	}

	public String toString() {
		return "Dosage Calculator";
	}
}
