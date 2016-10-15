package com.nosliw.data.library.geolocationi.v100;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.imp.HAPDataImp;

public class HAPGeoLocationData extends HAPDataImp{

	private double m_lat;
	private double m_lon;
	
	
	public HAPGeoLocationData(HAPDataType dataType, double lat, double lon) {
		super(dataType);
		this.m_lat = lat;
		this.m_lon = lon;
	}

	public double getLatitude(){
		return this.m_lat;
	}
	
	public double getLontitude(){
		return this.m_lon;
	}
	
	
	
	@Override
	public HAPData cloneData() {
		return null;
	}

	@Override
	public String toDataStringValue(String format) {
		return null;
	}

}
