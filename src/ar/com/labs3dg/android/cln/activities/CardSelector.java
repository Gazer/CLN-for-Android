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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import ar.com.labs3dg.android.cln.R;

public class CardSelector extends Activity implements OnClickListener
{
	ImageButton classic;
	ImageButton premium;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initViews();
	}

	private void initViews()
	{
		classic = (ImageButton)findViewById(R.id.btn_classic);
		premium = (ImageButton)findViewById(R.id.btn_premium);

		classic.setOnClickListener(this);
		premium.setOnClickListener(this);
	}

	private void showDashboard()
	{
		Intent intent = new Intent(getApplicationContext(), SearchType.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View button)
	{
		String card = "";

		if (button.equals(classic)) {
			card = "7199";
		} else {
			card = "7200";
		}

		PreferenceManager.getDefaultSharedPreferences(this)
		.edit()
		.putString("card", card)
		.commit();

		showDashboard();
	}
}