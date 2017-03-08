package com.nosliw.data.core;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;

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
public interface HAPDataTypeVersion extends HAPSerializable{

	@HAPAttribute
	public static String MAJOR = "major";
	
	@HAPAttribute
	public static String MINOR = "minor";
	
	@HAPAttribute
	public static String REVISION = "revision";

	String getMajor();
	
	String getMinor();
	
	String getRevision();
	
	HAPDataTypeVersion cloneVersion();
}
