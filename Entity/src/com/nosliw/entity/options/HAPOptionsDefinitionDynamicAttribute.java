package com.nosliw.entity.options;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.data1.HAPWraper;
import com.nosliw.entity.data.HAPDataWraper;
import com.nosliw.entity.definition.HAPAttributeDefinition;
import com.nosliw.entity.expression.HAPAttributeExpressionUtility;
import com.nosliw.expression.HAPExpressionInfo;

/*
 * this options is based on Dynamic Expression Options
 * in addition, it provide:
 * 		two parms for expression : this -- attribute     entity --- entity own this attribute
 * 		expression to do more filter
 */
public class HAPOptionsDefinitionDynamicAttribute extends HAPOptionsDefinitionComplexExpression{

	private HAPAttributeDefinition m_attributeDef;
	
	public HAPOptionsDefinitionDynamicAttribute(String name, HAPOptionsDefinition baseOptions, Map<String, HAPExpressionInfo> parmsExpressionInfo, HAPAttributeDefinition attrDef, String desc, HAPDataTypeManager dataTypeMan) {
		super(name, baseOptions, createAttrVarsInfo(parmsExpressionInfo, attrDef, dataTypeMan), desc, dataTypeMan);
		this.m_attributeDef = attrDef;
	}
	
	/*
	 * enhance parmsExpresionInfo with attribute infor
	 */
	private static Map<String, HAPExpressionInfo> createAttrVarsInfo(Map<String, HAPExpressionInfo> parmsExpressionInfo, HAPAttributeDefinition attrDef, HAPDataTypeManager dataTypeMan){
		Map<String, HAPExpressionInfo> out = new LinkedHashMap<String, HAPExpressionInfo>();
		for(String parm : parmsExpressionInfo.keySet()){
			out.put(parm, HAPAttributeExpressionUtility.appendAttributeVarInfoToExpressionInfo(parmsExpressionInfo.get(parm), attrDef)); 
		}
		return out;
	}

	protected HAPAttributeDefinition getAttributeDefinition(){
		return this.m_attributeDef;
	}

	@Override
	public String getType() {	return HAPConstant.OPTIONS_TYPE_DYNAMIC_EXPRESSION_ATTRIBUTE;	}
	
	@Override
	public String toStringValue(String format) {
		return null;
	}

	@Override
	public void init() {
	}

//	public List<HAPWraper> getOptions(HAPDataWraper attrDataWraper) {
//		Map<String, HAPWraper> parms = HAPAttributeExpressionUtility.prepareAttributeExpressionWraperParms(attrDataWraper, null);
//		return super.getOptions(null, parms);
//	}
	
}
