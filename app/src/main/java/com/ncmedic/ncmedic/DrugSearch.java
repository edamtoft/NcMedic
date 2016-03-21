package com.ncmedic.ncmedic;

import java.util.Arrays;
import java.util.List;

import com.ncmedic.ncmedic.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class DrugSearch extends Fragment implements TextWatcher,
		AdapterView.OnItemClickListener {
	EditText searchBox;
	ListView drugList;
	ProtocolListAdapter adapter;
	String[] drugs;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.activity_drug_search, container,
				false);
		searchBox = (EditText) view.findViewById(R.id.drugNameSearch);
		drugList = (ListView) view.findViewById(R.id.drugs_list_view);
		drugs = getResources().getStringArray(R.array.drugs);
		adapter = new ProtocolListAdapter(getActivity().getBaseContext(),
				Arrays.asList(drugs));
		drugList.setAdapter(adapter);
		searchBox.addTextChangedListener(this);
		drugList.setOnItemClickListener(this);
		searchBox.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView view, int actionID,
					KeyEvent event) {
				if (actionID == EditorInfo.IME_ACTION_DONE) {
					InputMethodManager imm = (InputMethodManager) getActivity()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}
				return false;
			}

		});

		return view;

	}

	@Override
	public void afterTextChanged(Editable arg0) {
		//

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence c, int arg1, int arg2, int arg3) {
		adapter.getFilter().filter(c);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		String drugName = adapter.getItem(pos);
		List<String> drugList = Arrays.asList(getResources().getStringArray(
				R.array.drugs));
		int position = drugList.indexOf(drugName);
		openDrug(position);
	}

	private void openDrug(int index) {

		Intent intent = new Intent(getActivity(), DrugPicker.class);
		intent.putExtra(DrugPicker.EXTRA_DRUG_INDEX, index);
		startActivity(intent);
	}

	@Override
	public String toString() {
		return "EMS Drugs";
	}
}
