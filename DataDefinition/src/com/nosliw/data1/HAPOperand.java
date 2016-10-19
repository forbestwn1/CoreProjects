package com.nosliw.data1;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.data.HAPData;
import com.nosliw.data.datatype.HAPDataTypeInfo;

/*
 * this interface is for all the entity which can be part of expression (operand)
 * there are three type of operationable : constant, variable, expression
 */
public interface HAPOperand extends HAPSerializable{

	//get type of operand : constant, variable, expression
	public int getOperandType();
	
	/*
	 * execute the operand and return the result
	 * inputs: 
	 * 		vars    	available variable values
	 * 		wraperVars  available wrapered variable values  XXXXX  why this needed ????
	 */
	public HAPData execute(Map<String, HAPData> vars, Map<String, HAPWraper> wraperVars, HAPOperationContext opContext);
	
	/*
	 * try best to get output data type information, 
	 * output data type information can always be get during run time
	 * however, sometimes, some judge can be made during parser time using output data type information
	 * for instance, we can know whether a express can be run on client side or not 
	 */
	public HAPDataTypeInfo getOutDataTypeInfo();

	/*
	 * check if this operand can be run under particular script
	 */
	public boolean isScriptRunnable(String script);

	/*
	 * preprocess operand, do data type analysis, get as much information as possible, include:
	 * 		find all variables and their data type and set them into varsInfo map
	 * 		find all out data type info of operand
	 */
	public void preProcess(Map<String, HAPDataTypeInfo> varsInfo, Set<HAPDataTypeInfo> dataTypeInfos);
}
