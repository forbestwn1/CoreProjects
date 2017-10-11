package com.nosliw.uiresource;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.HAPData;
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
	private Map<String, HAPExpressionDefinition> m_expressionDefinitions;

	//expressions for suport
	private Map<String, HAPExpressionDefinition> m_otherExpressionDefinitions;
	
	private Map<String, HAPData> m_constants;
	
	
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
	
	public void addExpressionDefinitions(Map<String, HAPExpressionDefinition> expDefs){		this.m_expressionDefinitions.putAll(expDefs);	}
	public void addOtherExpressionDefinitions(Map<String, HAPExpressionDefinition> expDefs){		this.m_otherExpressionDefinitions.putAll(expDefs);	}
	public Map<String, HAPExpressionDefinition> getExpressionDefinitions(){		return this.m_expressionDefinitions;	}
	public Map<String, HAPExpressionDefinition> getOtherExpressionDefinitions(){		return this.m_otherExpressionDefinitions;	}

	public Map<String, HAPData> getConstants(){  return this.m_constants; }
	public void addConstants(Map<String, HAPData> datas){  this.m_constants.putAll(datas);  }
	public void addConstant(String name, HAPData data){ this.m_constants.put(name, data);  }
}
