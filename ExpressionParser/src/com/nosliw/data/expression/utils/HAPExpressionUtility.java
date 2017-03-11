package com.nosliw.data.expression.utils;

import java.util.Map;

import com.nosliw.common.exception.HAPServiceDataException;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.basic.bool.HAPBooleanData;
import com.nosliw.data.core.basic.floa.HAPFloatData;
import com.nosliw.data.core.basic.number.HAPIntegerData;
import com.nosliw.data.core.basic.string.HAPStringData;
import com.nosliw.data.core.utils.HAPDataErrorUtility;
import com.nosliw.data.core.utils.HAPDataUtility;
import com.nosliw.data.core1.HAPDataTypeManager;
import com.nosliw.data.core1.HAPOperand;
import com.nosliw.data.core1.HAPOperationContext;
import com.nosliw.data.core1.HAPWraper;
import com.nosliw.data.expression1.HAPExpression;
import com.nosliw.data.expression1.HAPOperandDataOperaion;
import com.nosliw.data.expression1.HAPOperandPath;

public class HAPExpressionUtility {

	/*
	 * execute some expression with expected return data of boolean type
	 * if return is not boolean type, then throws exception
	 */
	public static boolean executeValidationExpression(HAPExpression expression, Map<String, HAPData> parms, Map<String, HAPWraper> wraperVars, HAPOperationContext opContext) throws HAPServiceDataException
	{
		HAPData out = expression.execute(parms, wraperVars, opContext);
		if(HAPDataUtility.isBooleanType(out)){
			return ((HAPBooleanData)out).getValue();
		}
		else{
			throw new HAPServiceDataException(HAPDataErrorUtility.createDataTypeError(HAPDataUtility.getDataTypeInfo(out), HAPDataTypeManager.BOOLEAN.process(), null));
		}
	}

	/*
	 * execute some expression with expected return data of integer type
	 * if return is not integer type, then throws exception
	 */
	public static int executeIntegerExpression(HAPExpression expression, Map<String, HAPData> parms, Map<String, HAPWraper> wraperVars, HAPOperationContext opContext) throws HAPServiceDataException
	{
		HAPData out = expression.execute(parms, wraperVars, opContext);
		if(HAPDataUtility.isIntegerType(out)){
			return ((HAPIntegerData)out).getValue();
		}
		else{
			throw new HAPServiceDataException(HAPDataErrorUtility.createDataTypeError(HAPDataUtility.getDataTypeInfo(out), HAPDataTypeManager.INTEGER.process(), null));
		}
	}
	
	/*
	 * execute some expression with expected return data of string type
	 * if return is not string type, then throws exception
	 */
	public static String executeStringExpression(HAPExpression expression, Map<String, HAPData> parms, Map<String, HAPWraper> wraperVars, HAPOperationContext opContext) throws HAPServiceDataException
	{
		HAPData out = expression.execute(parms, wraperVars, opContext);
		if(HAPDataUtility.isStringType(out)){
			return ((HAPStringData)out).getValue();
		}
		else{
			throw new HAPServiceDataException(HAPDataErrorUtility.createDataTypeError(HAPDataUtility.getDataTypeInfo(out), HAPDataTypeManager.STRING.process(), null));
		}
	}

	/*
	 * execute some expression with expected return data of string type
	 * if return is not string type, then throws exception
	 */
	public static float executeFloatExpression(HAPExpression expression, Map<String, HAPData> parms, Map<String, HAPWraper> wraperVars, HAPOperationContext opContext) throws HAPServiceDataException
	{
		HAPData out = expression.execute(parms, wraperVars, opContext);
		if(HAPDataUtility.isFloatType(out)){
			return ((HAPFloatData)out).getValue();
		}
		else{
			throw new HAPServiceDataException(HAPDataErrorUtility.createDataTypeError(HAPDataUtility.getDataTypeInfo(out), HAPDataTypeManager.FLOAT.process(), null));
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
		case HAPConstant.EXPRESSION_OPERAND_VARIABLE:
		case HAPConstant.EXPRESSION_OPERAND_CONSTANT:
		{
			break;
		}
		case HAPConstant.EXPRESSION_OPERAND_DATAOPERATION:
		case HAPConstant.EXPRESSION_OPERAND_DATATYPEOPERATION:
		case HAPConstant.EXPRESSION_OPERAND_ATTRIBUTEOPERATION:
		{
			if(out.toChild){
				//continue to child operand
				HAPOperandDataOperaion operationOperand = (HAPOperandDataOperaion)operand;
				if(operand.getOperandType()!=HAPConstant.EXPRESSION_OPERAND_DATATYPEOPERATION){
					//for data and attribute operation, process base data
					HAPOperandDataOperaion dataOperationOperand = (HAPOperandDataOperaion)operand;
					HAPIterateOperandTaskOut baseOut = iterateOperand(dataOperationOperand.getBaseData(), task, out.childData, false);
					if(baseOut.outOperand!=null)   dataOperationOperand.setBaseData(baseOut.outOperand);
				}
				
				if(operand.getOperandType()!=HAPConstant.EXPRESSION_OPERAND_ATTRIBUTEOPERATION){
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
		case HAPConstant.EXPRESSION_OPERAND_PATHOPERATION:
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
