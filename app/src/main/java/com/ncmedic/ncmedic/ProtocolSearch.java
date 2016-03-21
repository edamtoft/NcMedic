package com.ncmedic.ncmedic;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

public class ProtocolSearch extends Fragment implements TextWatcher,
		AdapterView.OnItemClickListener {
	EditText searchBox;
	ListView protocolList;
	ProtocolListAdapter adapter;
	String[] protocols;
	private static Pattern protocolPattern = Pattern.compile("^(\\d+)\\s+(.*)");


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.activity_protocol_search,
				container, false);
		searchBox = (EditText) view.findViewById(R.id.protocolNameSearch);
		protocolList = (ListView) view.findViewById(R.id.protocols_list_view);
		protocols = getResources().getStringArray(R.array.all_protocols);
		adapter = new ProtocolListAdapter(getActivity().getBaseContext(),
				Arrays.asList(protocols));
		protocolList.setAdapter(adapter);
		searchBox.addTextChangedListener(this);
		protocolList.setOnItemClickListener(this);
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
		String protocolString = adapter.getItem(pos);
		Matcher matcher = protocolPattern.matcher(protocolString);
		if (matcher.find()) {
			openProtocol(Integer.parseInt(matcher.group(1)));
		}
	}

	private void openProtocol(int index) {
		Intent intent = new Intent(getActivity(), ProtocolViewer.class);
		intent.putExtra(ProtocolViewer.EXTRA_PROTOCOL_INDEX, index);
		startActivity(intent);
	}
	@Override
	public String toString() {
		return "Protocols";
	}
}
