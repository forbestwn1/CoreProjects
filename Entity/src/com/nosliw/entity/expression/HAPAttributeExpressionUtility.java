package com.nosliw.entity.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceDataException;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data1.HAPDataTypeInfo;
import com.nosliw.data1.HAPWraper;
import com.nosliw.entity.data.HAPDataWraper;
import com.nosliw.entity.definition.HAPAttributeDefinition;
import com.nosliw.expression.HAPExpression;
import com.nosliw.expression.HAPExpressionInfo;
import com.nosliw.expression.utils.HAPExpressionUtility;

public class HAPAttributeExpressionUtility {

	/*
	 * prepare variable information for expression based on attribute
	 */
	public static Map<String, HAPDataTypeInfo> prepareAttributeExpressionVarInfos1(HAPAttributeDefinition attrDef, Map<String, HAPDataTypeInfo> varInfos){
		Map<String, HAPDataTypeInfo> out = varInfos;
		if(varInfos==null)		out = new LinkedHashMap<String, HAPDataTypeInfo>();
		out.put(HAPConstant.EXPRESSION_VARIABLE_THIS, attrDef.getDataTypeDefinitionInfo());
		String entityName = attrDef.getEntityDefinition().getEntityName();
		out.put(HAPConstant.EXPRESSION_VARIABLE_PARENT, new HAPDataTypeInfo(HAPConstant.DATATYPE_CATEGARY_ENTITY, entityName));
		return out;
	}

	/*
	 * add entity attribute as variables to expressioninfo 
	 */
	public static HAPExpressionInfo appendAttributeVarInfoToExpressionInfo(HAPExpressionInfo expressionInfo, HAPAttributeDefinition attrDef){
		Map<String, HAPDataTypeInfo> varInfos = new LinkedHashMap<String, HAPDataTypeInfo>();
		varInfos.put(HAPConstant.EXPRESSION_VARIABLE_THIS, attrDef.getDataTypeDefinitionInfo());
		String entityName = attrDef.getEntityDefinition().getEntityName();
		varInfos.put(HAPConstant.EXPRESSION_VARIABLE_PARENT, new HAPDataTypeInfo(HAPConstant.DATATYPE_CATEGARY_ENTITY, entityName));
		return expressionInfo.addVariable(varInfos);
	}
	
	/*
	 * prepare variable parms for expression based on attribute
	 */
	public static Map<String, HAPWraper> prepareAttributeExpressionWraperParms(HAPDataWraper attrWraper, Map<String, HAPWraper> wraperParms){
		if(wraperParms==null)  wraperParms = new LinkedHashMap<String, HAPWraper>();
		wraperParms.put(HAPConstant.EXPRESSION_VARIABLE_THIS, attrWraper);
		wraperParms.put(HAPConstant.EXPRESSION_VARIABLE_PARENT, attrWraper.getParentEntity().getWraper());
		return wraperParms;
	}
	
	
	/*
	 * execute some expression based on attribute with expected return data of boolean type
	 * if return is not boolean type, then throws exception
	 */
	public static boolean executeAttributeValidationExpression(HAPExpression expression, HAPDataWraper attrWraper) throws HAPServiceDataException{
		return HAPExpressionUtility.executeValidationExpression(expression,  null, HAPAttributeExpressionUtility.prepareAttributeExpressionWraperParms(attrWraper, null));
	}
}
