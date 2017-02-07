package com.nosliw.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;

/**
 * Every data type have a version with it
 * Version information have three components: major, minor, revision
 * There is no restriction for each components technically as long as they are unique
 *  
 *
 */
@HAPEntityWithAttribute(baseName="DATATYPEVERSION")
public interface HAPDataTypeVersion extends HAPSerializable{

	@HAPAttribute
	public static String MAJOR = "major";
	
	@HAPAttribute
	public static String MINOR = "minor";
	
	@HAPAttribute
	public static String REVISION = "revision";

	int getMajor();
	
	int getMinor();
	
	String getRevision();
	
	HAPDataTypeVersion cloneVersion();
}
