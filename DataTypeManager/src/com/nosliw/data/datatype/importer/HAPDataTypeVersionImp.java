package com.nosliw.data.datatype.importer;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.strvalue.HAPStringableValueObject;
import com.nosliw.data.HAPDataTypeVersion;

public class HAPDataTypeVersionImp extends HAPStringableValueObject implements HAPDataTypeVersion{
	protected String m_name;
	protected int m_major;
	protected int m_minor;
	protected String m_revision;
	
	private HAPDataTypeVersionImp(){super(null);}
	public HAPDataTypeVersionImp(String strValue) {		super(strValue);	}
	
	@Override
	public String getName(){  return this.m_name; }
	@Override
	public int getMajor(){  return this.m_major;	}
	@Override
	public int getMinor(){ return this.m_minor;	}
	@Override
	public String getRevision(){  return this.m_revision;	}

	protected void cloneFrom(HAPDataTypeVersionImp stringableValue){
		super.cloneFrom(stringableValue);
		stringableValue.m_major = this.m_major;
		stringableValue.m_minor = this.m_minor;
		stringableValue.m_revision = this.m_revision;
	}
	
	@Override
	public HAPDataTypeVersionImp clone(){
		HAPDataTypeVersionImp out = this.clone(this.getClass());
		return out;
	}

	@Override
	protected void parseStringValue(String strValue) {
		this.m_name = strValue;
		String[] segs = HAPNamingConversionUtility.parsePathSegs(strValue);
		if(segs.length>=3)   this.m_revision = segs[2];
		if(segs.length>=2)   this.m_minor = Integer.parseInt(segs[1]);
		if(segs.length>=1)   this.m_major = Integer.parseInt(segs[0]);
	}
}
