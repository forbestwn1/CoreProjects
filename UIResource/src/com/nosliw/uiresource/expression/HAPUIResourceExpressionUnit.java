package com.nosliw.uiresource.expression;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.imp.expression.HAPExpressionDefinitionSuiteImp;
import com.nosliw.uiresource.definition.HAPConstantDef;
import com.nosliw.uiresource.definition.HAPUIDefinitionUnit;

/**
 *  This class store all the expression info related with ui resources defintion 
 */
@HAPEntityWithAttribute
public class HAPUIResourceExpressionUnit extends HAPSerializableImp{

	@HAPAttribute
	public static String VARIABLES = "variables";
	
	@HAPAttribute
	public static String EXPRESSIONDEFINITIONS = "expressionDefinitions";

	@HAPAttribute
	public static String EXPRESSIONDEFINITIONS_SUPPORT = "expressionDefinitions_support";
	
	@HAPAttribute
	public static String CONSTANTS = "constants";

	@HAPAttribute
	public static String EXPRESSIONS = "expressions";

	@HAPAttribute
	public static String CHILDREN = "children";

	//variables defined in context
	private Map<String, HAPDataTypeCriteria> m_variables; 	
	
	//expressions used to do discovery (content, attribute)
	private Set<HAPExpressionDefinition> m_expressionDefinitions;

	//expressions for suport
	private Set<HAPExpressionDefinition> m_supportExpressionDefinitions;
	
	//every unit has its own expression definition suite
	//it includes expression definition and constants from parent and its own
	private HAPExpressionDefinitionSuiteImp m_expressionDefinitionSuite;

	//calcuated. every unit has its own processed expression
	private Map<String, HAPExpression> m_expressions;
	
	//children resource unit by tag id
	private Map<String, HAPUIResourceExpressionUnit> m_children;

	public HAPUIResourceExpressionUnit(
			HAPUIDefinitionUnit currentResourceUnit, 
			HAPUIDefinitionUnit parentResourceUnit,
			HAPExpressionManager expressionMan
			){
		this.m_expressionDefinitions = new HashSet<HAPExpressionDefinition>();
		this.m_supportExpressionDefinitions = new HashSet<HAPExpressionDefinition>();
		this.m_expressionDefinitionSuite = new HAPExpressionDefinitionSuiteImp();
		this.m_expressions = new LinkedHashMap<String, HAPExpression>();
		this.m_children = new LinkedHashMap<String, HAPUIResourceExpressionUnit>();
		this.m_variables = new LinkedHashMap<String, HAPDataTypeCriteria>();
		this.init(currentResourceUnit, parentResourceUnit, expressionMan);
	}

	private void init(
			HAPUIDefinitionUnit currentResourceUnit, 
			HAPUIDefinitionUnit parentResourceUnit,
			HAPExpressionManager expressionMan
			){
		
		this.addVariables(currentResourceUnit.getContext().getCriterias());

		HAPUIResourceExpressionUnit parent = parentResourceUnit==null?null:parentResourceUnit.getExpressionUnit();
		
		//build data constants
		if(parent!=null)	this.addConstants(parent.getConstants());
		Map<String, HAPConstantDef> constantDefs = currentResourceUnit.getConstants();
		for(String name : constantDefs.keySet()){
			HAPData data = constantDefs.get(name).getDataValue();
			if(data!=null)		this.addConstant(name, data);
		}
		
		//get all expressions
		if(parent!=null)	this.addSupportExpressionDefinitions(parent.getSupportExpressionDefinitions());
		Set<HAPExpressionDefinition> expDefs = currentResourceUnit.getExpressionDefinitions();
		Set<HAPExpressionDefinition> otherExpDefs = currentResourceUnit.getOtherExpressionDefinitions();
		this.addExpressionDefinitions(expDefs);
		this.addSupportExpressionDefinitions(otherExpDefs);
		
		//prepress expressions
		//preprocess attributes operand in expressions
		HAPUIResourceExpressionUtility.processAttributeOperandInExpression(m_expressionDefinitionSuite, m_variables);
		
		//only expression in content(html and attributes) need to process 
		for(HAPExpressionDefinition expDef : this.m_expressionDefinitions){
			String name = expDef.getName();
			HAPExpression expression = expressionMan.processExpression(null, m_expressionDefinitionSuite, name, m_variables);
			this.m_expressions.put(name, expression);
		}
		
		//process child tags
//		Iterator<HAPUIDefinitionUnitTag> tagsIterator = defUnit.getUITags().iterator();
//		while(tagsIterator.hasNext()){
//			HAPUIDefinitionUnitTag tag = tagsIterator.next();
//			HAPContext tagContext = this.getContextForTag(context, tag);
//			HAPUIResourceExpressionUnit tagResource = this.processDefinitionUnit(tag, tagContext, out, expressionMan);
//			out.addChild(tag.getId(), tagResource);
//		}
	}
	
	public void addVariables(Map<String, HAPDataTypeCriteria> variables){	this.m_variables.putAll(variables);	}
	
	public void addChild(String id, HAPUIResourceExpressionUnit child){  this.m_children.put(id, child);  }
	
	public void addExpressionDefinitions(Collection<HAPExpressionDefinition> expDefs){
		this.m_expressionDefinitions.addAll(expDefs);
		for(HAPExpressionDefinition expDef : expDefs)		this.m_expressionDefinitionSuite.addExpressionDefinition(expDef);
	}
	public void addSupportExpressionDefinitions(Collection<HAPExpressionDefinition> expDefs){		
		this.m_supportExpressionDefinitions.addAll(expDefs);	
		for(HAPExpressionDefinition expDef : expDefs)		this.m_expressionDefinitionSuite.addExpressionDefinition(expDef);
	}
	public Set<HAPExpressionDefinition> getExpressionDefinitions(){		return this.m_expressionDefinitions;	}
	public Set<HAPExpressionDefinition> getSupportExpressionDefinitions(){		return this.m_supportExpressionDefinitions;	}

	public Map<String, HAPData> getConstants(){  return this.m_expressionDefinitionSuite.getConstants(); }
	public void addConstants(Map<String, HAPData> datas){	this.m_expressionDefinitionSuite.getConstants().putAll(datas);	}
	public void addConstant(String name, HAPData data){		this.m_expressionDefinitionSuite.addConstant(name, data);	}

	public Map<String, HAPExpression> getExpressions(){  return this.m_expressions;  }
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(VARIABLES, HAPJsonUtility.buildJson(this.m_variables, HAPSerializationFormat.JSON));
		jsonMap.put(EXPRESSIONDEFINITIONS, HAPJsonUtility.buildJson(this.m_expressionDefinitions, HAPSerializationFormat.JSON));
		jsonMap.put(EXPRESSIONDEFINITIONS_SUPPORT, HAPJsonUtility.buildJson(this.m_supportExpressionDefinitions, HAPSerializationFormat.JSON));
		jsonMap.put(EXPRESSIONS, HAPJsonUtility.buildJson(this.m_expressions, HAPSerializationFormat.JSON));
		jsonMap.put(CHILDREN, HAPJsonUtility.buildJson(this.m_children, HAPSerializationFormat.JSON));
	}
}
