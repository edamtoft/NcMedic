package com.ncmedic.ncmedic;

import java.util.ArrayList;
import java.util.Locale;

import com.ncmedic.ncmedic.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class AbbreviationAdapter extends BaseAdapter implements Filterable {
	private Context context;
	private ArrayList<String[]> abbreviations;
	private ArrayList<String[]> original;
	private AbbreviationFilter filter;

	public AbbreviationAdapter(Context context, ArrayList<String[]> abbrevs) {
		this.context = context;
		this.abbreviations = new ArrayList<String[]>();
		this.abbreviations.addAll(abbrevs);
		this.original = new ArrayList<String[]>();
		this.original.addAll(abbrevs);
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View abbreviation;

		if (convertView == null) {

			abbreviation = new View(context);

		} else {
			abbreviation = (View) convertView;
		}
		abbreviation = inflater.inflate(R.layout.abbreviation, null);

		TextView def = (TextView) abbreviation
				.findViewById(R.id.abbrevDefTextView);
		TextView abb = (TextView) abbreviation
				.findViewById(R.id.abbrevAbbTextView);
		String[] current = abbreviations.get(position);
		abb.setText(current[0]);
		def.setText(current[1]);
		return abbreviation;
	}

	@Override
	public int getCount() {
		return abbreviations.size();
	}

	@Override
	public Object getItem(int position) {
		return abbreviations.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public Filter getFilter() {
		if (filter == null) {
			filter = new AbbreviationFilter();
		}
		return filter;
	}
	
	public class AbbreviationFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			String constraintString = constraint.toString().toLowerCase(Locale.US);
			FilterResults results = new FilterResults();
			if (constraint != null && constraintString.length() > 0) {
				ArrayList<String[]> filteredItems = new ArrayList<String[]>();
				for (int i = 0; i < original.size(); i++) {
					String[] item = original.get(i);
					if (item[0].toLowerCase(Locale.US).contains(constraintString) || item[1].toLowerCase(Locale.US).contains(constraintString)) {
						filteredItems.add(item);
					}
				}
				results.count = filteredItems.size();
				results.values = filteredItems;
			} else {
				synchronized(this) {
					results.count = original.size();
					results.values = original;
				}
			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			abbreviations = (ArrayList<String[]>)results.values;
			notifyDataSetChanged();
		}

	}

}