package com.ncmedic.ncmedic;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class FilterArrayAdapter extends BaseAdapter implements Filterable {

	private ArrayList<String> items;
	private ArrayList<String> original;
	private ContainsFilter filter;
	private int resourceID;
	private Context context;
    private static Pattern protocolPattern = Pattern.compile("^\\d+\\s+");

	public FilterArrayAdapter(Context c, int resource, List<String> objects) {
		this.items = new ArrayList<String>();
		this.original = new ArrayList<String>();
		this.items.addAll(objects);
		this.original.addAll(objects);
		this.resourceID = resource;
		this.context = c;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, resourceID);
	}

	private View createViewFromResource(int position, View convertView,
			ViewGroup parent, int resource) {
		View view;
		TextView text;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(resource, parent, false);
		} else {
			view = convertView;
		}

		text = (TextView) view;
		String item = getItem(position);
		Matcher matcher = protocolPattern.matcher(item);
		if (matcher.matches()) {
			item = matcher.replaceFirst("");
		}
		text.setText(item);
		

		return view;
	}

	@Override
	public Filter getFilter() {
		if (filter == null) {
			filter = new ContainsFilter();
		}
		return filter;
	}

	@Override
	public int getCount() {
		if (items != null) {
			return items.size();
		}
		return 0;
	}

	@Override
	public String getItem(int position) {
		return items.get(position);
	}

	public class ContainsFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			if (constraint != null && constraint.length() > 0) {
				ArrayList<String> filteredItems = new ArrayList<String>();
				for (int i = 0; i < original.size(); i++) {
					String item = original.get(i);
					if (item.toLowerCase(Locale.US).contains(constraint.toString().toLowerCase(
							Locale.US))) {
						filteredItems.add(item);
					}
				}
				results.count = filteredItems.size();
				results.values = filteredItems;
			} else {
				results.count = original.size();
				results.values = original;
			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			items = (ArrayList<String>) results.values;
			notifyDataSetChanged();
		}

	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
