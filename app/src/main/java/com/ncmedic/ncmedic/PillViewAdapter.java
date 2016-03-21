package com.ncmedic.ncmedic;

import java.util.List;

import com.ncmedic.ncmedic.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PillViewAdapter extends BaseAdapter {
	private Context context;
	private final List<Pill> pillValues;

	public PillViewAdapter(Context context, List<Pill> pills) {
		this.context = context;
		this.pillValues = pills;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View pillView;

		if (convertView == null) {

			pillView = new View(context);

		} else {
			pillView = (View) convertView;
		}
		pillView = inflater.inflate(R.layout.pill_view, null);

		TextView ingredients = (TextView) pillView
				.findViewById(R.id.pillIngredientText);
		TextView dosages = (TextView) pillView
				.findViewById(R.id.pillDosageText);
		ImageView img = (ImageView) pillView.findViewById(R.id.pillImage);
		Pill currentPill = pillValues.get(position);
		String shapeCode = currentPill.getShapeCode();
		if (shapeCode.equals("C48335")) {
			img.setImageResource(R.drawable.bullet);
		} else if (shapeCode.equals("C48336")) {
			img.setImageResource(R.drawable.capsule);
		} else if (shapeCode.equals("C48337")) {
			img.setImageResource(R.drawable.clover);
		} else if (shapeCode.equals("C48338")) {
			img.setImageResource(R.drawable.diamond);
		} else if (shapeCode.equals("C48339")) {
			img.setImageResource(R.drawable.doublecircle);
		} else if (shapeCode.equals("C48340")) {
			img.setImageResource(R.drawable.irregular);
		} else if (shapeCode.equals("C48341")) {
			img.setImageResource(R.drawable.gear);
		} else if (shapeCode.equals("C48343")) {
			img.setImageResource(R.drawable.hexagon);
		} else if (shapeCode.equals("C48344")) {
			img.setImageResource(R.drawable.octagon);
		} else if (shapeCode.equals("C48345")) {
			img.setImageResource(R.drawable.oval);
		} else if (shapeCode.equals("C48346")) {
			img.setImageResource(R.drawable.pentagon);
		} else if (shapeCode.equals("C48347")) {
			img.setImageResource(R.drawable.rect);
		} else if (shapeCode.equals("C48348")) {
			img.setImageResource(R.drawable.round);
		} else if (shapeCode.equals("C48349")) {
			img.setImageResource(R.drawable.semicircle);
		} else if (shapeCode.equals("C48350")) {
			img.setImageResource(R.drawable.square);
		} else if (shapeCode.equals("C48351")) {
			img.setImageResource(R.drawable.tear);
		} else if (shapeCode.equals("C48352")) {
			img.setImageResource(R.drawable.trapezoid);
		} else if (shapeCode.equals("C48353")) {
			img.setImageResource(R.drawable.triangle);
		}
		ingredients.setText(currentPill.getActiveIngredients());
		dosages.setText(currentPill.getDosages());
		return pillView;
	}

	@Override
	public int getCount() {
		return pillValues.size();
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