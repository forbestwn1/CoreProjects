package com.nosliw.core.application.common.dataexpression;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public class HAPGroupDataExpression implements HAPUnitDataExpression{

	@HAPAttribute
	public static String ITEM = "item";

	private List<HAPElementInGroupDataExpression> m_items;
	
	private HAPContainerVariableCriteriaInfo m_variableInfo;
	
	public HAPGroupDataExpression() {
		this.m_variableInfo = new HAPContainerVariableCriteriaInfo();
		this.m_items = new ArrayList<HAPElementInGroupDataExpression>();
	}

	
	@Override
	public HAPContainerVariableCriteriaInfo getVariablesInfo() {   return this.m_variableInfo;   }

	
	
}
