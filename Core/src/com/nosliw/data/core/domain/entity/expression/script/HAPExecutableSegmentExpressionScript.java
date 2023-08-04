package com.nosliw.data.core.domain.entity.expression.script;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.script.expression.imp.expression.HAPConstantInScript;
import com.nosliw.data.core.script.expression.imp.expression.HAPVariableInScript;

public class HAPExecutableSegmentExpressionScript extends HAPExecutableSegmentExpression{

	private List<Object> m_segments;

	public HAPExecutableSegmentExpressionScript() {
		this.m_segments = new ArrayList<Object>();
	}
	
	@Override
	public String getType() {  return HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPT;  }

	public List<Object> getSegments(){   return this.m_segments;    }
	
	public void addSegment(Object segment) {    this.m_segments.add(segment);    }
	
	public void updateConstantValue(Map<String, Object> constantsValue) {
		List<Object> newEles = new ArrayList<Object>();
		for(Object ele : this.m_segments) {
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
		this.m_segments = newEles;
	}
	
}
