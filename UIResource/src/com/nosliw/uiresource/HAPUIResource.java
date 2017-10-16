package com.nosliw.uiresource;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.uiresource.expression.HAPUIResourceExpressionUnit;

/**
 * UIResource that is result of processing UI Definition
 */
@HAPEntityWithAttribute
public class HAPUIResource extends HAPSerializableImp{

	@HAPAttribute
	public static String RESOURCEDEFINITION = "resourceDefinition";

	@HAPAttribute
	public static String EXPRESSIONUNIT = "expressionUnit";
	
	//resource definition
	private HAPUIDefinitionUnitResource m_uiDefinitionResource;
	
	//resource unit
	private HAPUIResourceExpressionUnit m_expressionUnit;
	
	public HAPUIResource(HAPUIDefinitionUnitResource uiDefinitionUnitResource){
		this.m_uiDefinitionResource = uiDefinitionUnitResource; 
	}
	
	public void process(HAPExpressionManager expressionMan){
		HAPContext context = m_uiDefinitionResource.getContext();
		m_expressionUnit = this.processDefinitionUnit(m_uiDefinitionResource, context, null, expressionMan);
	}
	
	private HAPUIResourceExpressionUnit processDefinitionUnit(
			HAPUIDefinitionUnit defUnit, 
			HAPContext context,
			HAPUIResourceExpressionUnit parent,
			HAPExpressionManager expressionMan
			){
		HAPUIResourceExpressionUnit out = new HAPUIResourceExpressionUnit();
		out.addVariables(context.getCriterias());
		
		//build data constants
		if(parent!=null)	out.addConstants(parent.getConstants());
		Map<String, HAPConstantDef> constantDefs = this.m_uiDefinitionResource.getConstants();
		for(String name : constantDefs.keySet()){
			HAPData data = constantDefs.get(name).getDataValue();
			if(data!=null)		out.addConstant(name, data);
		}
		
		//get all expressions
		if(parent!=null)	out.addSupportExpressionDefinitions(parent.getSupportExpressionDefinitions());
		Set<HAPExpressionDefinition> expDefs = defUnit.getExpressionDefinitions();
		Set<HAPExpressionDefinition> otherExpDefs = defUnit.getOtherExpressionDefinitions();
		out.addExpressionDefinitions(expDefs);
		out.addSupportExpressionDefinitions(otherExpDefs);
		
		//prepress expressions
		out.processExpressions(expressionMan);
		
		//process child tags
//		Iterator<HAPUIDefinitionUnitTag> tagsIterator = defUnit.getUITags().iterator();
//		while(tagsIterator.hasNext()){
//			HAPUIDefinitionUnitTag tag = tagsIterator.next();
//			HAPContext tagContext = this.getContextForTag(context, tag);
//			HAPUIResourceExpressionUnit tagResource = this.processDefinitionUnit(tag, tagContext, out, expressionMan);
//			out.addChild(tag.getId(), tagResource);
//		}
		
		return out;
	}
	
	private HAPContext getContextForTag(HAPContext parent, HAPUIDefinitionUnitTag tag){
		
		return null;
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(RESOURCEDEFINITION, this.m_uiDefinitionResource.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(EXPRESSIONUNIT, this.m_expressionUnit.toStringValue(HAPSerializationFormat.JSON));
	}
}
