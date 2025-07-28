package com.nosliw.core.application.common.scriptexpression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.common.scriptexpression.HAPManualVariableInScript;
import com.nosliw.data.core.script.expression1.imp.expression.HAPConstantInScript;
import com.nosliw.data.core.script.expression1.imp.expression.HAPVariableInScript;

public class HAPManualSegmentScriptExpressionScriptSimple extends HAPManualSegmentScriptExpressionScript{

	@HAPAttribute
	public static String PART = "part";

	private List<Object> m_parts;

	public HAPManualSegmentScriptExpressionScriptSimple(String id) {
		super(id);
		this.m_parts = new ArrayList<Object>();
	}
	
	@Override
	public String getType() {  return HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPTSIMPLE;  }

	public List<Object> getParts(){   return this.m_parts;    }
	
	public void addPart(Object part) {    this.m_parts.add(part);    }
	
	public Set<String> getVariableKeys(){
		Set<String> out = new HashSet<String>();
		for(Object part : this.m_parts) {
			if(part instanceof HAPManualVariableInScript) {
				out.add(((HAPManualVariableInScript)part).getVariableKey());
			}
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		List<String> partJsonArray = new ArrayList<String>();
		for(Object part : this.m_parts) {
			partJsonArray.add(HAPManagerSerialize.getInstance().toStringValue(part, HAPSerializationFormat.JSON));
		}
		jsonMap.put(PART, HAPUtilityJson.buildArrayJson(partJsonArray.toArray(new String[0])));
	}

	public void updateConstantValue(Map<String, Object> constantsValue) {
		List<Object> newEles = new ArrayList<Object>();
		for(Object ele : this.m_parts) {
			if(ele instanceof HAPVariableInScript) {
				HAPVariableInScript varEle = (HAPVariableInScript)ele;
				Object constantValue = constantsValue.get(varEle.getVariableName());
				if(constantValue!=null) {
					HAPConstantInScript constantEle = new HAPConstantInScript(varEle.getVariableName());
					constantEle.setValue(constantValue);
				}
				else {
					newEles.add(ele);
				}
			}
			else if(ele instanceof HAPConstantInScript) {
				HAPConstantInScript constantEle = (HAPConstantInScript)ele;
				constantEle.setValue(constantsValue.get(constantEle.getConstantName()));
				newEles.add(constantEle);
			}
			else {
				newEles.add(ele);
			}
		}
		this.m_parts = newEles;
	}
	
}
