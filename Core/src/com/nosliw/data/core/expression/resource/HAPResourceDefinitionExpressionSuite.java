package com.nosliw.data.core.expression.resource;

import java.util.Map;

import com.nosliw.data.core.component.HAPChildrenComponentIdContainer;
import com.nosliw.data.core.component.HAPResourceDefinitionContainer;
import com.nosliw.data.core.expression.HAPDefinitionExpressionSuite;
import com.nosliw.data.core.resource.HAPResourceDefinition;

public class HAPResourceDefinitionExpressionSuite extends HAPResourceDefinitionContainer implements HAPDefinitionExpressionSuite{

	public HAPResourceDefinitionExpressionSuite() {
	}

	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		return true;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	public HAPChildrenComponentIdContainer getChildrenComponentId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPResourceDefinitionContainer cloneResourceDefinitionContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPResourceDefinition getElementResourceDefinition(String eleName) {  return new HAPResourceDefinitionExpressionGroup(this, eleName);  }

}
