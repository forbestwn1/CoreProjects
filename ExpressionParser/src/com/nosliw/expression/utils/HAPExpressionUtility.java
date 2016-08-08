package com.nosliw.expression.utils;

import java.util.Map;

import com.nosliw.common.exception.HAPServiceDataException;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPOperand;
import com.nosliw.data.HAPWraper;
import com.nosliw.data.basic.bool.HAPBooleanData;
import com.nosliw.data.basic.floa.HAPFloatData;
import com.nosliw.data.basic.number.HAPIntegerData;
import com.nosliw.data.basic.string.HAPStringData;
import com.nosliw.data.utils.HAPDataErrorUtility;
import com.nosliw.data.utils.HAPDataUtility;
import com.nosliw.expression.HAPExpression;
import com.nosliw.expression.HAPOperandDataOperaion;
import com.nosliw.expression.HAPOperandPath;

public class HAPExpressionUtility {

	/*
	 * execute some expression with expected return data of boolean type
	 * if return is not boolean type, then throws exception
	 */
	public static boolean executeValidationExpression(HAPExpression expression, Map<String, HAPData> parms, Map<String, HAPWraper> wraperVars) throws HAPServiceDataException
	{
		HAPData out = expression.execute(parms, wraperVars);
		if(HAPDataUtility.isBooleanType(out)){
			return ((HAPBooleanData)out).getValue();
		}
		else{
			throw new HAPServiceDataException(HAPDataErrorUtility.createDataTypeError(HAPDataUtility.getDataTypeInfo(out), HAPDataTypeManager.BOOLEAN.getDataTypeInfo(), null));
		}
	}

	/*
	 * execute some expression with expected return data of integer type
	 * if return is not integer type, then throws exception
	 */
	public static int executeIntegerExpression(HAPExpression expression, Map<String, HAPData> parms, Map<String, HAPWraper> wraperVars) throws HAPServiceDataException
	{
		HAPData out = expression.execute(parms, wraperVars);
		if(HAPDataUtility.isIntegerType(out)){
			return ((HAPIntegerData)out).getValue();
		}
		else{
			throw new HAPServiceDataException(HAPDataErrorUtility.createDataTypeError(HAPDataUtility.getDataTypeInfo(out), HAPDataTypeManager.INTEGER.getDataTypeInfo(), null));
		}
	}
	
	/*
	 * execute some expression with expected return data of string type
	 * if return is not string type, then throws exception
	 */
	public static String executeStringExpression(HAPExpression expression, Map<String, HAPData> parms, Map<String, HAPWraper> wraperVars) throws HAPServiceDataException
	{
		HAPData out = expression.execute(parms, wraperVars);
		if(HAPDataUtility.isStringType(out)){
			return ((HAPStringData)out).getValue();
		}
		else{
			throw new HAPServiceDataException(HAPDataErrorUtility.createDataTypeError(HAPDataUtility.getDataTypeInfo(out), HAPDataTypeManager.STRING.getDataTypeInfo(), null));
		}
	}

	/*
	 * execute some expression with expected return data of string type
	 * if return is not string type, then throws exception
	 */
	public static float executeFloatExpression(HAPExpression expression, Map<String, HAPData> parms, Map<String, HAPWraper> wraperVars) throws HAPServiceDataException
	{
		HAPData out = expression.execute(parms, wraperVars);
		if(HAPDataUtility.isFloatType(out)){
			return ((HAPFloatData)out).getValue();
		}
		else{
			throw new HAPServiceDataException(HAPDataErrorUtility.createDataTypeError(HAPDataUtility.getDataTypeInfo(out), HAPDataTypeManager.FLOAT.getDataTypeInfo(), null));
		}
	}
	
	/*
	 * iterate all operand 
	 */
	public static HAPIterateOperandTaskOut iterateOperand(HAPOperand operand, HAPIterateOperandTask task, Object data){
		return iterateOperand(operand, task, data, true);
	}

	
	private static HAPIterateOperandTaskOut iterateOperand(HAPOperand operand, HAPIterateOperandTask task, Object data, boolean isRoot){
		HAPIterateOperandTaskOut out = task.process(operand, data, isRoot);
		switch(operand.getOperandType()){
		case HAPConstant.CONS_EXPRESSION_OPERAND_VARIABLE:
		case HAPConstant.CONS_EXPRESSION_OPERAND_CONSTANT:
		{
			break;
		}
		case HAPConstant.CONS_EXPRESSION_OPERAND_DATAOPERATION:
		case HAPConstant.CONS_EXPRESSION_OPERAND_DATATYPEOPERATION:
		case HAPConstant.CONS_EXPRESSION_OPERAND_ATTRIBUTEOPERATION:
		{
			if(out.toChild){
				//continue to child operand
				HAPOperandDataOperaion operationOperand = (HAPOperandDataOperaion)operand;
				if(operand.getOperandType()!=HAPConstant.CONS_EXPRESSION_OPERAND_DATATYPEOPERATION){
					//for data and attribute operation, process base data
					HAPOperandDataOperaion dataOperationOperand = (HAPOperandDataOperaion)operand;
					HAPIterateOperandTaskOut baseOut = iterateOperand(dataOperationOperand.getBaseData(), task, out.childData, false);
					if(baseOut.outOperand!=null)   dataOperationOperand.setBaseData(baseOut.outOperand);
				}
				
				if(operand.getOperandType()!=HAPConstant.CONS_EXPRESSION_OPERAND_ATTRIBUTEOPERATION){
					//parms
					HAPOperand[] parms = operationOperand.getParameters();
					for(int i=0; i<parms.length; i++){
						HAPIterateOperandTaskOut parmOut = iterateOperand(parms[i], task, out.childData, false);
						if(parmOut.outOperand!=null)  parms[i] = parmOut.outOperand;
					}
				}
			}
			break;
		}
		case HAPConstant.CONS_EXPRESSION_OPERAND_PATHOPERATION:
		{
			if(out.toChild){
				HAPOperandPath pathOperand = (HAPOperandPath)operand;
				HAPIterateOperandTaskOut baseOut = iterateOperand(pathOperand.getBaseData(), task, out.childData, false);
				if(baseOut.outOperand!=null)   pathOperand.setBaseData(pathOperand);
			}			
		}
		}	
		return out;
	}
}
