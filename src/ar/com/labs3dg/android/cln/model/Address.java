package ar.com.labs3dg.android.cln.model;

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
