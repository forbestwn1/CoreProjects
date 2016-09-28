package com.nosliw.data.library.distance.v100;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataImp;
import com.nosliw.data.HAPDataType;

public class HAPDistanceData extends HAPDataImp{

	private double m_distance;
	private HAPData m_lengthUnit;
	
	public HAPDistanceData(double distance, HAPData lengthUnit, HAPDataType dataType) {
		super(dataType);
		this.m_distance = distance;
		this.m_lengthUnit = lengthUnit;
	}

	public HAPData getLengthUnit(){
		return this.m_lengthUnit;
	}
	
	public double getDistance(){
		return this.m_distance;
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
