package com.nosliw.core.application.common.dataexpression;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;

@HAPEntityWithAttribute
public class HAPElementInGroupDataExpression extends HAPEntityInfoImp{

	@HAPAttribute
	public static String EXPRESSION = "expression";

	private HAPDataExpression m_dataExpression;
	
	
	
	
}
