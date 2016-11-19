package com.nosliw.data.datatype.importer;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.strvalue.HAPStringableValueObject;
import com.nosliw.data.HAPDataTypeInfo;
import com.nosliw.data.HAPDataTypeVersion;

public class HAPDataTypeInfoImp1 extends HAPStringableValueObject implements HAPDataTypeInfo{

	private String m_name;
	private HAPDataTypeVersion m_version;
	
	public HAPDataTypeInfoImp1(){super(null);}
	public HAPDataTypeInfoImp1(String strValue) {super(strValue);	}

	@Override
	public String getName() {		return this.m_name;	}
	@Override
	public HAPDataTypeVersion getVersion() {	return this.m_version;	}

	@Override
	protected void parseStringValue(String strValue) {
		String[] segs = HAPNamingConversionUtility.parseSegments(strValue);
		this.m_name = segs[0];
		if(segs.length>=2)    this.m_version = new HAPDataTypeVersionImp(segs[1]);
	}

	public static String buildStringValue(String name, String version){
		return HAPNamingConversionUtility.cascadeSegments(name, version);
	}
	
	public HAPDataTypeInfoImp1 clone(){
		HAPDataTypeInfoImp1 out = this.clone(this.getClass());
		return out;
	}

	protected void cloneFrom(HAPDataTypeInfoImp1 dataTypeInfo){
		super.cloneFrom(dataTypeInfo);
		this.m_name = dataTypeInfo.m_name;
		this.m_version = dataTypeInfo.m_version.clone();
	}
}
