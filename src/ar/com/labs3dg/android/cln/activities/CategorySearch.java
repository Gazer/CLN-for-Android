package ar.com.labs3dg.android.cln.activities;

/*
 * Club La Nacion for Android by 3DGLabs
 * Copyright (C) 2011  3DGLabs <info@3dglabs.com.ar>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import ar.com.labs3dg.android.cln.LookupPosition;
import ar.com.labs3dg.android.cln.R;
import ar.com.labs3dg.android.cln.model.Category;
import ar.com.labs3dg.android.cln.model.Client;

public class CategorySearch extends Activity implements OnClickListener
{
	Button search;
	EditText keywords;
	Spinner category;
	Spinner subcategory;
	List<Category> categories;

	ArrayAdapter<String> adapter;
	ArrayAdapter<String> sub_adapter;
	ArrayList<String> categories_str;
	ArrayList<String> sub_categories_str;

	String card;
	LookupPosition position = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.category_search);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			position = new LookupPosition(extras);
		} else {
			Log.d("CategorySearch", "Missing location");
			this.finish();
		}

		categories = Client.Categories;
		categories_str = new ArrayList<String>();
		categories_str.add("Todas");
		for(Category c: categories) {
			categories_str.add(c.name);
		}
		sub_categories_str = new ArrayList<String>();

		search = (Button)findViewById(R.id.search);
		search.setOnClickListener(this);

		keywords = (EditText)findViewById(R.id.query);

		category = (Spinner)findViewById(R.id.category);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories_str);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		category.setAdapter(adapter);
		category.setOnItemSelectedListener(onCategoryClicked);

		subcategory = (Spinner)findViewById(R.id.subcategory);
		sub_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sub_categories_str);
		sub_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		subcategory.setAdapter(sub_adapter);

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		card = pref.getString("card", "");
	}

	@Override
	public void onClick(View arg0)
	{
		String q = keywords.getText().toString();
		if (q.length() == 0) {
			q = "-";
		}
		ArrayList<String> params = new ArrayList<String>();
		params.add("http://serviciosclub.lnol.com.ar/codamation/getComercios");
		params.add(getCategory());
		params.add(getSubCategory());
		params.add(q);
		params.add("" + position.getLatitude());
		params.add("" + position.getLongitude());
		params.add(Client.PER_PAGE);
		params.add("{PAGE}");
		params.add(card);
		params.add("/");

		String url = Client.join(params.iterator(), "/");

		Intent intent = new Intent(getApplicationContext(), VenuesMapActivity.class);
		intent.putExtra("title", "Cat");
		intent.putExtra("subtitle", "SubCat");
		intent.putExtra("data_url", url);
		intent.putExtra("latitude", position.getLatitude());
		intent.putExtra("longitude", position.getLongitude());
		startActivity(intent);
	}

	private final OnItemSelectedListener onCategoryClicked = new OnItemSelectedListener()
	{
		@Override
		public void onItemSelected(AdapterView<?> adapter, View arg1, int position, long id)
		{
			sub_categories_str.clear();
			sub_categories_str.add("Todas");
			if (position == 0) {
				return;
			}

			for(Category c: categories.get(position-1).subcategories) {
				sub_categories_str.add(c.name);
			}
			sub_adapter.notifyDataSetChanged();
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapter) {}
	};

	private String getCategory()
	{
		int position = category.getSelectedItemPosition();

		if (position == 0) {
			return "-";
		}

		Category cat = categories.get(position-1);
		return "" + cat.id;
	}

	private String getSubCategory()
	{
		int position = category.getSelectedItemPosition();
		if (position == 0) {
			return "-";
		}
		Category cat = categories.get(position-1);

		position = subcategory.getSelectedItemPosition();
		if (position == 0) {
			return "-";
		}

		return "" + cat.subcategories.get(position-1).id;
	}
}
