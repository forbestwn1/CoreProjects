package com.nosliw.data.core;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;

/**
 * Every data type have a version with it
 * Version information have three components: major, minor, revision
 * There is no restriction for each components technically as long as they are unique
 *  	major:  
 *  		number
 *  		sequence, one after another
 *  		change major version only when data structure change
 *  	minor:
 *  		number
 *  		sequence, one after another
 *  		change minor version when new functions are added
 *  	revision:
 *  		string
 *  		no sequence (cannot compare to each other under same major.minor)
 *  		used for customerization
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
