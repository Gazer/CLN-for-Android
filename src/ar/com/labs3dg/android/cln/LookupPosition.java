package ar.com.labs3dg.android.cln;

import android.os.Bundle;

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

public class LookupPosition
{
	private final double _latitude;
	private final double _longitude;

	public LookupPosition( final double latitude, final double longitude )
	{
		super();
		_latitude = latitude;
		_longitude = longitude;
	}

	public LookupPosition(Bundle extras)
	{
		this(extras.getDouble("latitude"), extras.getDouble("longitude"));
	}

	public double getLatitude()
	{
		return _latitude;
	}

	public double getLongitude()
	{
		return _longitude;
	}
}
