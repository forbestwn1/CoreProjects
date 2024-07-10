package com.nosliw.core.application.division.manual.brick.dataexpression.group;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.application.brick.dataexpression.group.HAPBlockDataExpressionGroup;
import com.nosliw.core.application.brick.dataexpression.group.HAPGroupDataExpression;
import com.nosliw.core.application.common.valueport.HAPVariableInfo;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickImp;

public class HAPManualBlockDataExpressionGroup extends HAPManualBrickImp implements HAPBlockDataExpressionGroup{

	@Override
	public void init() {
		super.init();
		this.setAttributeValueWithValue(VALUE, new HAPGroupDataExpression());
		this.setAttributeValueWithValue(VARIABLEINFOS, new LinkedHashMap<String, HAPVariableInfo>());
	}
	
	@Override
	public HAPGroupDataExpression getValue(){	return (HAPGroupDataExpression)this.getAttributeValueOfValue(VALUE);	}

	@Override
	public Map<String, HAPVariableInfo> getVariablesInfo() {     return (Map<String, HAPVariableInfo>)this.getAttributeValueOfValue(VARIABLEINFOS);  }

}
