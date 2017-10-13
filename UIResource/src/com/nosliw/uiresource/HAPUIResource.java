package com.nosliw.uiresource;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpressionDefinition;

/**
 * UIResource that is result of processing UI Definition
 */
public class HAPUIResource {

	//resource definition
	private HAPUIDefinitionUnitResource m_uiDefinitionResource;
	
	//resource unit
	private HAPUIResourceUnit m_resourceUnit;
	
	public HAPUIResource(HAPUIDefinitionUnitResource uiDefinitionUnitResource){
		this.m_uiDefinitionResource = uiDefinitionUnitResource; 
	}
	
	public void process(){
		HAPContext context = m_uiDefinitionResource.getContext();
		m_resourceUnit = this.processDefinitionUnit(m_uiDefinitionResource, context, null);
	}
	
	private HAPUIResourceUnit processDefinitionUnit(
			HAPUIDefinitionUnit defUnit, 
			HAPContext context,
			HAPUIResourceUnit parent
			){
		HAPUIResourceUnit out = new HAPUIResourceUnit(context);
		
		//build data constants
		if(parent!=null)	out.addConstants(parent.getConstants());
		Map<String, HAPConstantDef> constantDefs = this.m_uiDefinitionResource.getConstants();
		for(String name : constantDefs.keySet()){
			HAPData data = constantDefs.get(name).getDataValue();
			if(data!=null)		out.addConstant(name, data);
		}
		
		//build expression
		if(parent!=null)	out.addOtherExpressionDefinitions(parent.getOtherExpressionDefinitions());
		Set<HAPExpressionDefinition> expDefs = defUnit.getExpressionDefinitions();
		Set<HAPExpressionDefinition> otherExpDefs = defUnit.getOtherExpressionDefinitions();
		out.addExpressionDefinitions(expDefs);
		out.addOtherExpressionDefinitions(otherExpDefs);
		
		//process child tags
		Iterator<HAPUIDefinitionUnitTag> tagsIterator = defUnit.getUITags().iterator();
		while(tagsIterator.hasNext()){
			HAPUIDefinitionUnitTag tag = tagsIterator.next();
			HAPContext tagContext = this.getContextForTag(context, tag);
			HAPUIResourceUnit tagResource = this.processDefinitionUnit(tag, tagContext, out);
			out.addChild(tag.getId(), tagResource);
		}
		
		return out;
	}
	
	private HAPContext getContextForTag(HAPContext parent, HAPUIDefinitionUnitTag tag){
		return null;
	}
}
