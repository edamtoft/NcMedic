package com.ncmedic.ncmedic;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ncmedic.ncmedic.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class ProtocolListAdapter extends BaseAdapter implements Filterable {
	
	private Context context;
	private ArrayList<String> items;
	private ArrayList<String> original;
	private ProtocolsFilter filter;
	private static Pattern protocolPattern = Pattern.compile("^(\\d+)\\s+(.*)");


	public ProtocolListAdapter(Context context, List<String> protocolList) {
		this.context = context;
		this.items = new ArrayList<String>();
		this.items.addAll(protocolList);
		this.original = new ArrayList<String>();
		this.original.addAll(protocolList);
	}

	@Override
	public int getCount() {
		return items.size();

	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View protocolItem = inflater.inflate(R.layout.protocol_item, null);

		TextView number = (TextView) protocolItem.findViewById(R.id.protocol_number_text_view);
		TextView name = (TextView) protocolItem.findViewById(R.id.protocol_name_text_view);
		String current = items.get(position);
		Matcher matcher = protocolPattern.matcher(current);
		if (matcher.matches()) {
			number.setText(matcher.group(1));
			name.setText(matcher.group(2));
		} else {
			number.setVisibility(View.GONE);
			name.setText(current);
		}
		return protocolItem;
	}

	@Override
	public Filter getFilter() {
		if (filter == null) {
			filter = new ProtocolsFilter();
		}
		return filter;
	}
	
	public class ProtocolsFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			if (constraint != null && constraint.length() > 0) {
				ArrayList<String> filteredItems = new ArrayList<String>();
				for (int i = 0; i < original.size(); i++) {
					String item = original.get(i);
					if (item.toLowerCase(Locale.US).contains(constraint.toString().toLowerCase(Locale.US))) {
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
		protected void publishResults(CharSequence constraint, FilterResults results) {
			items = (ArrayList<String>)results.values;
			notifyDataSetChanged();
		}

	}

	@Override
	public String getItem(int position) {
		return items.get(position);
	}

}
