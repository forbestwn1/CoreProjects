package com.nosliw.data;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute(baseName="DATAOPERATIONINFO")
public interface HAPOperationInfo {

	@HAPAttribute
	public static String NAME = "name";
	
	@HAPAttribute
	public static String PAMRS = "parms";
	
	@HAPAttribute
	public static String OUTPUT = "output";
	
	@HAPAttribute
	public static String DESCRIPTION = "description";

	String getName();

	String getDescription();
	
	Map<String, HAPOperationParmInfo> getParmsInfo();
	
	HAPOperationOutInfo getOutputInfo();
	
}
