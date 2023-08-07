package com.nosliw.data.core.domain.entity.expression.script;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPExecutableContainerExpression extends HAPExecutableImp{

	@HAPAttribute
	public static String ELEMENT = "element";

	private List<HAPExecutableExpression> m_elements = new ArrayList<HAPExecutableExpression>();
	
	public List<HAPExecutableExpression> getAllExpressionItems(){   return this.m_elements;  }
	public void addExpressionItem(HAPExecutableExpression expressionItem) {    this.m_elements.add(expressionItem);       }

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		List<String> eleArrayStr = new ArrayList<String>();
		for(HAPExecutableExpression element : this.m_elements) {
			eleArrayStr.add(element.toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(ELEMENT, HAPUtilityJson.buildArrayJson(eleArrayStr.toArray(new String[0])));
	}
}
