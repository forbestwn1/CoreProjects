package com.nosliw.uiresource;

import java.util.Map;

import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinitionSuite;

public class HAPUIResourceUnit {

	//every unit has its own context
	HAPContext m_context;
	
	//every unit has its own expression definition suite
	//it includes expression definition and constants from parent and its own
	private HAPExpressionDefinitionSuite m_expressionDefinitionSuite;

	//every unit has its own processed expression
	private Map<String, HAPExpression> m_expressions;
	
	//children resource unit by tag id
	private Map<String, HAPUIResourceUnit> m_children;
}
