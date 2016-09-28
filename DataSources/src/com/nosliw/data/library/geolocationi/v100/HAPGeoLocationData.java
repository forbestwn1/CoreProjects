package com.nosliw.data.library.geolocationi.v100;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataImp;
import com.nosliw.data.HAPDataType;

public class HAPGeoLocationData extends HAPDataImp{

	private float m_lat;
	private float m_lon;
	
	
	public HAPGeoLocationData(HAPDataType dataType) {
		super(dataType);
	}

	public float getLatitude(){
		return this.m_lat;
	}
	
	public float getLontitude(){
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
