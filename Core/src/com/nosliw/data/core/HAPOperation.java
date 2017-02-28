package com.nosliw.data.core;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

/**
 * This entity describe all the information related with a operation
 *  	name:   name of the operation
 *  	type:	type of the operation, normal, constructor, convertor, ....
 *  	parms: 	parameters of operation
 *  	output: output information of operation
 *  When defining parameters and output data type, we use criteria for it.  
 *  This means that in design time both parameter and output may not be a single data type
 */
@HAPEntityWithAttribute(baseName="DATAOPERATIONINFO")
public interface HAPOperation {

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String TYPE = "type";
	
	@HAPAttribute
	public static String PAMRS = "parms";
	
	@HAPAttribute
	public static String OUTPUT = "output";
	
	@HAPAttribute
	public static String INFO = "info";

	String getName();

	String getType();
	
	HAPInfo getInfo();
	
	Map<String, HAPOperationParmInfo> getParmsInfo();
	
	HAPOperationOutInfo getOutputInfo();
	
}
