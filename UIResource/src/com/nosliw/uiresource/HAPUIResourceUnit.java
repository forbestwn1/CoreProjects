package com.nosliw.uiresource;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionDefinitionSuite;
import com.nosliw.data.core.imp.expression.HAPExpressionDefinitionSuiteImp;

/**
 *  
 */
public class HAPUIResourceUnit {

	//every unit has its own context
	HAPContext m_context;
	
	//expressions used to do discovery (content, attribute)
	private Set<HAPExpressionDefinition> m_expressionDefinitions;

	//expressions for suport
	private Set<HAPExpressionDefinition> m_otherExpressionDefinitions;
	
	
	//every unit has its own expression definition suite
	//it includes expression definition and constants from parent and its own
	private HAPExpressionDefinitionSuite m_expressionDefinitionSuite;

	//every unit has its own processed expression
	private Map<String, HAPExpression> m_expressions;
	
	//children resource unit by tag id
	private Map<String, HAPUIResourceUnit> m_children;

	public HAPUIResourceUnit(HAPContext context){
		this.m_expressionDefinitionSuite = new HAPExpressionDefinitionSuiteImp();
		this.m_expressions = new LinkedHashMap<String, HAPExpression>();
		this.m_children = new LinkedHashMap<String, HAPUIResourceUnit>();
		
		this.m_context = context;
	}

	public void addChild(String id, HAPUIResourceUnit child){  this.m_children.put(id, child);  }
	
	public void addExpressionDefinitions(Set<HAPExpressionDefinition> expDefs){
		this.m_expressionDefinitions.addAll(expDefs);
	}
	
	public void addOtherExpressionDefinitions(Set<HAPExpressionDefinition> expDefs){
		this.m_otherExpressionDefinitions.addAll(expDefs);
	}

	public Set<HAPExpressionDefinition> getExpressionDefinitions(){
		return this.m_expressionDefinitions;
	}

	public Set<HAPExpressionDefinition> getOtherExpressionDefinitions(){
		return this.m_otherExpressionDefinitions;
	}

}
