package com.ncmedic.ncmedic;

import com.ncmedic.ncmedic.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class ProtocolPage extends Fragment {
	public final static String EXTRA_PROTOCOL_INDEX = "com.ncmedic.ncmedic.PROTOCOL_PAGE_INDEX";
	public final static String EXTRA_PROTOCOL_PAGE = "com.ncmedic.ncmedic.PROTOCOL_PAGE";

	private int protocolIndex;
	private int page;
	private WebView protocolView;

	@SuppressLint("NewApi")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.protocol_page, container, false);
		protocolView = (WebView) view.findViewById(R.id.protocolPageWebView);
		page = this.getArguments().getInt(EXTRA_PROTOCOL_PAGE, 1);
		protocolIndex = this.getArguments().getInt(EXTRA_PROTOCOL_INDEX, 1);
		protocolView.getSettings().setLoadWithOverviewMode(true);
		protocolView.getSettings().setUseWideViewPort(true);
		protocolView.getSettings().setBuiltInZoomControls(true);
		try {
			protocolView.getSettings().setDisplayZoomControls(false);
		} catch (NoSuchMethodError e) {
			e.printStackTrace();
		}
		protocolView.loadUrl("file:///android_asset/protocols/"
				+ protocolIndex + "/page" + page + ".png");
		return view;
	}

	@Override
	public String toString() {
		page = this.getArguments().getInt(EXTRA_PROTOCOL_PAGE, 1);
		return "Page " + (page+1);
	}

}
