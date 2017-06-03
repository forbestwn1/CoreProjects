package com.nosliw.data.core;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;

/**
 * Data type id to specify the data type
 * 		name : the name of data type
 * 		version: the same data type may have different version
 * Therefore, name and version together are related with a unique data type
 */
@HAPEntityWithAttribute(baseName="DATATYPEID")
public class HAPDataTypeId extends HAPSerializableImp{

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String VERSION = "version";

	@HAPAttribute
	public static String FULLNAME = "fullName";
	
	private String m_name;
	
	private HAPDataTypeVersion m_version;
	
	private String m_fullName;
	
	public HAPDataTypeId(){	}

	public HAPDataTypeId(String name, String version){
		this(name, new HAPDataTypeVersion(version));
	}
		
	public HAPDataTypeId(String name, HAPDataTypeVersion version){
		this.m_name = name;
		this.m_version = version;
		this.processVersion();
	}

	public HAPDataTypeId(String fullName){
		this.setFullName(fullName);
	}
	
	public String getFullName(){
		if(HAPBasicUtility.isStringEmpty(this.m_fullName)){
			String versionLiterate = null;
			if(this.m_version!=null){
				versionLiterate = this.m_version.toStringValue(HAPSerializationFormat.LITERATE);
			}
			this.m_fullName = HAPNamingConversionUtility.cascadeLevel1(this.getName(), versionLiterate);
		}
		return this.m_fullName;
	}
	public void setFullName(String fullName){
		this.m_fullName = null;

		String[] segs = HAPNamingConversionUtility.parseLevel1(fullName);
		this.m_name = segs[0];
		if(segs.length>=2){
			this.m_version = new HAPDataTypeVersion(segs[1]);
		}
		this.processVersion();
	}
	
	public String getVersionMajor(){  return this.getVersion().getMajor(); }
	public String getVersionMinor(){  return this.getVersion().getMinor(); }
	public String getVersionRevision(){  return this.getVersion().getRevision();  }
	
	public String getName() {		return this.m_name;	}
	protected void setName(String name){ this.m_name = name; }
	public HAPDataTypeVersion getVersion() {  return this.m_version;  }
	protected void setVersion(HAPDataTypeVersion version){  this.m_version = version;  }
	protected void setVersion(String version){  this.m_version = new HAPDataTypeVersion(version); }

	public String getVersionFullName(){   return this.getVersion().getName();  }
	
	private void processVersion(){
		if(this.m_version==null)  this.m_version = new HAPDataTypeVersion();
	}
	
	@Override
	protected String buildFullJson(){ return this.buildLiterate(); }
	@Override
	protected String buildJson(){ return this.buildLiterate(); }
	
	@Override
	protected String buildLiterate(){
		return this.getFullName();
	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		this.setFullName(literateValue);
		return true;
	}
	
	public static String buildStringValue(String name, String version){
		return HAPNamingConversionUtility.cascadeLevel1(name, version);
	}
	
	public HAPDataTypeId clone(){
		HAPDataTypeId out = new HAPDataTypeId();
		out.m_name = this.m_name;
		out.m_version = this.m_version.cloneVersion();
		out.m_fullName = this.m_fullName;
		return out;
	}
	
	@Override
	public int hashCode(){		return this.getFullName().hashCode();	}
	
	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPDataTypeId){
			HAPDataTypeId id = (HAPDataTypeId)obj;
			out = HAPBasicUtility.isEquals(id.getFullName(), this.getFullName());
		}
		return out;
	}
	
	@Override
	public String toString(){
		return this.toStringValue(HAPSerializationFormat.LITERATE);
	}
	
}
