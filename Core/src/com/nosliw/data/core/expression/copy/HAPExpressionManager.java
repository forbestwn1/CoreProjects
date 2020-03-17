package com.nosliw.data.core.expression.copy;

import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPCriteriaParser;

public class HAPExpressionManager {

	public static HAPExpressionParser expressionParser;

	public static HAPDataTypeHelper dataTypeHelper;
	
	public static HAPCriteriaParser criteriaParser = HAPCriteriaParser.getInstance();
}
