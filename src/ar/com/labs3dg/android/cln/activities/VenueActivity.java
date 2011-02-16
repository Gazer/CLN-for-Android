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

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import ar.com.labs3dg.android.cln.LookupPosition;
import ar.com.labs3dg.android.cln.R;
import ar.com.labs3dg.android.cln.model.Venue;

public class VenueActivity extends Activity implements OnClickListener
{
	private HashMap<Integer, Bitmap> icons;
	private HashMap<String, Bitmap> discounts;
	private Venue venue;
	private Button btnMap;
	private ImageButton btnPhone;
	private LookupPosition position;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.venue);

		discounts = new HashMap<String, Bitmap>();
		discounts.put("2x1", BitmapFactory.decodeResource(getResources(), R.drawable.dis_2x1));
		discounts.put("10%", BitmapFactory.decodeResource(getResources(), R.drawable.dis_10));
		discounts.put("15%", BitmapFactory.decodeResource(getResources(), R.drawable.dis_15));
		discounts.put("20%", BitmapFactory.decodeResource(getResources(), R.drawable.dis_20));
		discounts.put("25%", BitmapFactory.decodeResource(getResources(), R.drawable.dis_25));
		discounts.put("30%", BitmapFactory.decodeResource(getResources(), R.drawable.dis_30));
		discounts.put("35%", BitmapFactory.decodeResource(getResources(), R.drawable.dis_35));
		discounts.put("40%", BitmapFactory.decodeResource(getResources(), R.drawable.dis_40));
		discounts.put("50%", BitmapFactory.decodeResource(getResources(), R.drawable.dis_50));

		icons = new HashMap<Integer, Bitmap>();
		icons.put(6838, BitmapFactory.decodeResource(getResources(), R.drawable.ico_gastronomia));
		icons.put(6839, BitmapFactory.decodeResource(getResources(), R.drawable.ico_cuidadopersonal));
		icons.put(7279, BitmapFactory.decodeResource(getResources(), R.drawable.ico_entretenimiento));
		icons.put(7290, BitmapFactory.decodeResource(getResources(), R.drawable.ico_espectaculos));
		icons.put(7296, BitmapFactory.decodeResource(getResources(), R.drawable.ico_compras));

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			venue = extras.getParcelable("venue");
			position = new LookupPosition(extras.getDouble("latitude"), extras.getDouble("longitude"));
		} else {
			Log.d("VenuesActivity", "No extras");
			this.finish();
			return;
		}

		setText(R.id.description, venue.description);
		setText(R.id.name, venue.name);
		setText(R.id.address, venue.getFullAddress());
		setText(R.id.days, venue.days);
		setText(R.id.status, venue.status);
		if (venue.phone.length() < 4) {
			setText(R.id.phone, " ");
			findViewById(R.id.btnPhone).setVisibility(View.GONE);
		} else {
			setText(R.id.phone, venue.phone);
			findViewById(R.id.btnPhone).setVisibility(View.VISIBLE);
		}
		setText(R.id.email, "");
		setText(R.id.web, "");

		ImageView icon = (ImageView)findViewById(R.id.category);
		icon.setImageBitmap(icons.get(venue.category_id));

		icon = (ImageView)findViewById(R.id.discount);
		icon.setImageBitmap(discounts.get(venue.discount));

		btnMap = (Button)findViewById(R.id.mapView);
		btnMap.setOnClickListener(this);

		btnPhone = (ImageButton)findViewById(R.id.btnPhone);
		btnPhone.setOnClickListener(this);

		setupLinks();
	}

	private void setupLinks()
	{
		TextView v;

		v = (TextView)findViewById(R.id.phone);
		Linkify.addLinks(v, Linkify.ALL);

		v = (TextView)findViewById(R.id.web);
		Linkify.addLinks(v, Linkify.ALL);

		v = (TextView)findViewById(R.id.email);
		Linkify.addLinks(v, Linkify.ALL);
	}

	private void setText(int resId, String txt)
	{
		((TextView)findViewById(resId)).setText(txt);
	}

	private static final int MENU_SHOW_MAP = 1;
	private static final int MENU_HOME = 2;
	private static final int MENU_CALL = 3;

	@Override
	public boolean onCreateOptionsMenu( final Menu menu )
	{
		if (venue.phone.length() >= 4) {
			menu.add(0, MENU_CALL, 0, "Llamar" ).setIcon(android.R.drawable.ic_menu_call);
		}
		menu.add(0, MENU_SHOW_MAP, 0, "Ver Mapa" ).setIcon(android.R.drawable.ic_menu_mapmode);
		menu.add(0, MENU_HOME, 0, "Inicio" ).setIcon(R.drawable.home);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId()) {
		case MENU_SHOW_MAP:
			showMap();
			return true;
		case MENU_HOME:
			Intent homeIntent = new Intent(this, SearchType.class);
			homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(homeIntent);
			return true;
		case MENU_CALL:
			call();
			return true;
		}
		return false;
	}

	private void showMap()
	{
		Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
		intent.putExtra("venue", venue);
		intent.putExtra("latitude", position.getLatitude());
		intent.putExtra("longitude", position.getLongitude());
		startActivity(intent);
	}

	@Override
	public void onClick(View button)
	{
		if (button.equals(btnMap)) {
			showMap();
		} else if (button.equals(btnPhone)) {
			call();
		}
	}

	private void call()
	{
		String[] phones = (""+venue.phone).split("/");

		if (phones.length > 0) {
			Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phones[0]));
			startActivity(callIntent);
		}
	}
}
