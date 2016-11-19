package com.nosliw.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;

@HAPEntityWithAttribute(baseName="DATATYPEVERSION")
public interface HAPDataTypeVersion extends HAPSerializable{

	@HAPAttribute
	public static String NAME = "name";
	
	@HAPAttribute
	public static String MAJOR = "major";
	
	@HAPAttribute
	public static String MINOR = "minor";
	
	@HAPAttribute
	public static String REVISION = "revision";

	String getName();
	
	Integer getMajor();
	
	Integer getMinor();
	
	String getRevision();
	
	HAPDataTypeVersion cloneVersion();
}
