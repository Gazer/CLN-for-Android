package ar.com.labs3dg.android.cln.model;

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

import ar.com.labs3dg.android.cln.LookupPosition;

public class Address
{
	LookupPosition location;
	String address;

	private Address(LookupPosition l, String a)
	{
		location = l;
		address = a;
	}

	public LookupPosition getPosition()
	{
		return location;
	}

	@Override
	public String toString()
	{
		return address;
	}

	public static Address Parse(String s)
	{
		String[] a = s.split("\\{#\\}");

		double lat = Double.parseDouble(a[1].replace(",", "."));
		double lon = Double.parseDouble(a[0].replace(",", "."));

		return new Address(new LookupPosition(lat, lon), a[2]);
	}
}
