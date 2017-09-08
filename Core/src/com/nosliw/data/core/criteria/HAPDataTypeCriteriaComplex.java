package com.nosliw.data.core.criteria;

import java.util.List;
import java.util.Map;

public abstract class HAPDataTypeCriteriaComplex extends HAPDataTypeCriteriaImp{

	public HAPDataTypeCriteriaComplex(List<HAPDataTypeCriteria> eles) {
		this.addChildren(eles);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}

}
