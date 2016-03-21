package com.ncmedic.ncmedic;

import com.ncmedic.ncmedic.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context context;
	private final String[] values;

	public ImageAdapter(Context context, String[] vals) {
		this.context = context;
		this.values = vals;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View shapeView;

		if (convertView == null) {

			shapeView = new View(context);

			shapeView = inflater.inflate(R.layout.pill_shape, null);

			ImageView imageView = (ImageView) shapeView
					.findViewById(R.id.pill_item_image);

			String value = values[position];
			
			imageView.setContentDescription(value);

			if (value.equals("C48335")) {
				imageView.setImageResource(R.drawable.bullet);
			} else if (value.equals("C48336")) {
				imageView.setImageResource(R.drawable.capsule);
			} else if (value.equals("C48337")) {
				imageView.setImageResource(R.drawable.clover);
			} else if (value.equals("C48338")) {
				imageView.setImageResource(R.drawable.diamond);
			} else if (value.equals("C48339")) {
				imageView.setImageResource(R.drawable.doublecircle);
			} else if (value.equals("C48340")) {
				imageView.setImageResource(R.drawable.irregular);
			} else if (value.equals("C48341")) {
				imageView.setImageResource(R.drawable.gear);
			} else if (value.equals("C48343")) {
				imageView.setImageResource(R.drawable.hexagon);
			} else if (value.equals("C48344")) {
				imageView.setImageResource(R.drawable.octagon);
			} else if (value.equals("C48345")) {
				imageView.setImageResource(R.drawable.oval);
			} else if (value.equals("C48346")) {
				imageView.setImageResource(R.drawable.pentagon);
			} else if (value.equals("C48347")) {
				imageView.setImageResource(R.drawable.rect);
			} else if (value.equals("C48348")) {
				imageView.setImageResource(R.drawable.round);
			} else if (value.equals("C48349")) {
				imageView.setImageResource(R.drawable.semicircle);
			} else if (value.equals("C48350")) {
				imageView.setImageResource(R.drawable.square);
			} else if (value.equals("C48351")) {
				imageView.setImageResource(R.drawable.tear);
			} else if (value.equals("C48352")) {
				imageView.setImageResource(R.drawable.trapezoid);
			} else if (value.equals("C48353")) {
				imageView.setImageResource(R.drawable.triangle);
			} 

		} else {
			shapeView = (View) convertView;
		}

		return shapeView;
	}

	@Override
	public int getCount() {
		return values.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}