package com.nosliw.uiresource;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.imp.expression.HAPExpressionDefinitionSuiteImp;
import com.nosliw.uiresource.expression.HAPExpressionUtility;

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
	
	private Map<String, HAPData> m_constants;
	
	//every unit has its own expression definition suite
	//it includes expression definition and constants from parent and its own
	private HAPExpressionDefinitionSuiteImp m_expressionDefinitionSuite;

	//calcuated. every unit has its own processed expression
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
	
	public void addExpressionDefinitions(Collection<HAPExpressionDefinition> expDefs){
		this.m_expressionDefinitions.addAll(expDefs);
		for(HAPExpressionDefinition expDef : expDefs)		this.m_expressionDefinitionSuite.addExpressionDefinition(expDef);
	}
	public void addOtherExpressionDefinitions(Collection<HAPExpressionDefinition> expDefs){		
		this.m_otherExpressionDefinitions.addAll(expDefs);	
		for(HAPExpressionDefinition expDef : expDefs)		this.m_expressionDefinitionSuite.addExpressionDefinition(expDef);
	}
	public Set<HAPExpressionDefinition> getExpressionDefinitions(){		return this.m_expressionDefinitions;	}
	public Set<HAPExpressionDefinition> getOtherExpressionDefinitions(){		return this.m_otherExpressionDefinitions;	}

	public Map<String, HAPData> getConstants(){  return this.m_constants; }
	public void addConstants(Map<String, HAPData> datas){  this.m_constants.putAll(datas);  }
	public void addConstant(String name, HAPData data){ this.m_constants.put(name, data);  }

	public void processExpressions(HAPExpressionManager expressionMan){
		//preprocess attributes operand in expressions
		HAPExpressionUtility.processAttributeOperandInExpression(m_expressionDefinitionSuite, this.m_context.getCriterias());
		
		//only expression in content(html and attributes) need to process 
		for(HAPExpressionDefinition expDef : this.m_expressionDefinitions){
			String name = expDef.getName();
			HAPExpression expression = expressionMan.processExpression(null, m_expressionDefinitionSuite, name, this.m_context.getCriterias());
			this.m_expressions.put(name, expression);
		}
	}
	
}

