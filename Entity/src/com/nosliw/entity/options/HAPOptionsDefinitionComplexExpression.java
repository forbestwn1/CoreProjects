package com.nosliw.entity.options;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.expression1.HAPExpression;
import com.nosliw.data.expression1.HAPExpressionInfo;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.data1.HAPWraper;

/*
 * this options is based on one dynamic options
 * however, the parms value for base options are expressions 
 */
public abstract class HAPOptionsDefinitionComplexExpression extends HAPOptionsDefinition{

	//base options
	HAPOptionsDefinition m_baseOptions;
	//expression parms for dynamic options
	Map<String, HAPExpression> m_expressionParms; 
	
	public HAPOptionsDefinitionComplexExpression(String name, HAPOptionsDefinition baseOptions, Map<String, HAPExpressionInfo> expressionParms, String desc, HAPDataTypeManager dataTypeMan) {
		super(name, baseOptions.getDataTypeInfo(), desc, dataTypeMan);
		this.m_baseOptions = baseOptions;
		
		this.m_expressionParms = new LinkedHashMap<String, HAPExpression>();
		for(String parm : expressionParms.keySet()){
			this.m_expressionParms.put(parm, new HAPExpression(expressionParms.get(name), dataTypeMan));
		}
	}

	@Override
	public String getType() {	return HAPConstant.OPTIONS_TYPE_DYNAMIC_EXPRESSION;	}

	
	
//	public List<HAPWraper> getOptions(Map<String, HAPData> parms, Map<String, HAPWraper> wraperParms) {
//		Map<String, HAPData> baseParms = new LinkedHashMap<String, HAPData>();
//		for(String parm : this.m_expressionParms.keySet()){
//			HAPExpression parmExpression = this.m_expressionParms.get(parm);
//			HAPData parmData = parmExpression.execute(parms, wraperParms);
//			baseParms.put(parm, parmData);
//		}
//		return this.m_baseOptions.getOptions(baseParms);
//	}
	
}
