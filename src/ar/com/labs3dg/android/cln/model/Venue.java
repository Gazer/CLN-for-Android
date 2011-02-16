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

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Venue implements Parcelable
{
	@SuppressWarnings("unchecked")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
	{
		public Venue createFromParcel(Parcel in)
		{
			return new Venue(in);
		}

		public Venue[] newArray(int size)
		{
			return new Venue[size];
		}
	};

	public int id;
	public String name;
	public double lat;
	public double lon;
	public int category_id;
	public String discount;
	public String address;
	public String city;
	public String state;
	public String phone;
	public String description;
	public String days;
	public String from;
	public String status;

	public Venue()
	{
	}

	public Venue(Parcel in)
	{
		id = in.readInt();
		name = in.readString();
		lat = in.readDouble();
		lon = in.readDouble();
		category_id = in.readInt();
		discount = in.readString();
		address = in.readString();
		city = in.readString();
		state = in.readString();
		phone = in.readString();
		description = in.readString();
		days = in.readString();
		from = in.readString();
		status = in.readString();
	}

	public String getFullAddress()
	{
		ArrayList<String> params = new ArrayList<String>();
		if ((address != null) && (address.length() > 0)) {
			params.add(address);
		}
		if ((city != null) && (city.length() > 0)) {
			params.add(city);
		}
		if ((state != null) && (state.length() > 0)) {
			params.add(state);
		}

		return Client.join(params.iterator(), ", ");
	}

	public static Venue FromString(String s) throws Exception
	{
		String[] data = s.split("\\{#\\}");
		Venue v = new Venue();
		v.id = Integer.parseInt(data[0]);
		v.name = data[1];
		v.address = data[2];
		v.city = data[3];
		v.state = data[4];
		v.phone = data[5];
		v.category_id = Integer.parseInt(data[6]);
		v.discount = data[8];
		v.description = data[9];
		v.days = data[10];
		v.status = data[11];
		v.lat = Double.parseDouble(data[12]);
		v.lon = Double.parseDouble(data[13]);

		return v;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeDouble(lat);
		dest.writeDouble(lon);
		dest.writeInt(category_id);
		dest.writeString(discount);
		dest.writeString(address);
		dest.writeString(city);
		dest.writeString(state);
		dest.writeString(phone);
		dest.writeString(description);
		dest.writeString(days);
		dest.writeString(from);
		dest.writeString(status);
	}
}
