package com.nosliw.data.core.domain.entity.expression.script1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.script.expression1.imp.expression.HAPConstantInScript;
import com.nosliw.data.core.script.expression1.imp.expression.HAPVariableInScript;

public class HAPExecutableSegmentExpressionScript extends HAPExecutableSegmentExpression{

	@HAPAttribute
	public static String PART = "part";

	private List<Object> m_parts;

	public HAPExecutableSegmentExpressionScript(String id) {
		super(id);
		this.m_parts = new ArrayList<Object>();
	}
	
	@Override
	public String getType() {  return HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPTSIMPLE;  }

	public List<Object> getParts(){   return this.m_parts;    }
	
	public void addPart(Object part) {    this.m_parts.add(part);    }
	
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
