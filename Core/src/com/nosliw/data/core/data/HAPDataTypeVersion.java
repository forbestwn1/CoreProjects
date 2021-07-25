package com.nosliw.data.core.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

/**
 * Every data type have a version with it
 * Version information have three components: major, minor, revision
 * There is no restriction for each components technically as long as they are unique
 * 		Major version is used for data struction
 * 		Minor version is used for data operation
 * 		Revision is used for customerization
 * When a data type change involves data struction change, then new major version should be introduced
 * When a data type change involved operation change, then new minor version should be introduced 
 */
@HAPEntityWithAttribute(baseName="DATATYPEVERSION")
public class HAPDataTypeVersion extends HAPSerializableImp{

	@HAPAttribute
	public static String MAJOR = "major";
	
	@HAPAttribute
	public static String MINOR = "minor";
	
	@HAPAttribute
	public static String REVISION = "revision";

	@HAPAttribute
	public static String NAME = "name";
	
	private String m_major;
	
	private String m_minor;
	
	private String m_revision;

	private String m_name;
	
	public HAPDataTypeVersion(){
		this.init();
	}
	
	public HAPDataTypeVersion(String literate){
		this.buildObjectByLiterate(literate);
	}
	
	private void init(){
		this.m_major = "1";
		this.m_minor = "0";
		this.m_revision = "0";
		this.m_name = null;
	}
	
	public String getName(){	
		if(HAPBasicUtility.isStringEmpty(this.m_name)){
			this.m_name = this.buildLiterate();
		}
		return this.m_name;
	}
	public void setName(String name){ this.buildObjectByLiterate(name); }
	
	public String getMajor(){  return this.m_major;	}
	private void setMajor(String major){  this.m_major = major;  }
	public String getMinor(){ return this.m_minor;	}
	private void setMinor(String minor){  this.m_minor = minor;  }
	public String getRevision(){  return this.m_revision;	}
	private void setRevision(String revision){  this.m_revision = revision;  }

	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		this.init();

		String[] segs = HAPUtilityNamingConversion.parsePaths(literateValue);
		if(segs.length>=3) this.setRevision(segs[2]);   
		if(segs.length>=2) this.setMinor(segs[1]); 
		if(segs.length>=1) this.setMajor(segs[0]); 
		return true;
	}

	@Override
	protected String buildLiterate(){
		String out = this.m_name;
		if(HAPBasicUtility.isStringEmpty(out)){
			out = HAPUtilityNamingConversion.cascadeComponentPath(new String[]{String.valueOf(this.getMajor()), String.valueOf(this.getMinor()), this.getRevision()});
			this.m_name = out;
		}
		return out; 
	}
	
	public HAPDataTypeVersion cloneVersion(){
		HAPDataTypeVersion out = new HAPDataTypeVersion();
		out.m_major = this.m_major;
		out.m_minor = this.m_minor;
		out.m_revision = this.m_revision;
		out.m_name = this.m_name;
		return out;
	}
	
	@Override
	public String toString(){
		return this.toStringValue(HAPSerializationFormat.LITERATE);
	}
}
