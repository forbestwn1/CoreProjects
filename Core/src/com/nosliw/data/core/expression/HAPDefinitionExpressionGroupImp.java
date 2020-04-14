package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.common.HAPWithDataContext;
import com.nosliw.data.core.component.HAPWithAttachmentImp;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextStructure;

public class HAPDefinitionExpressionGroupImp extends HAPWithAttachmentImp implements HAPDefinitionExpressionGroup, HAPWithDataContext{

	private Map<String, HAPDefinitionExpression> m_elements;
	
	private HAPContext m_context;
	
	public HAPDefinitionExpressionGroupImp() {
		this.m_elements = new LinkedHashMap<String, HAPDefinitionExpression>();
	}
	
	public void addExpression(HAPDefinitionExpression element) {   this.m_elements.put(element.getName(), element);   }

	@Override
	public HAPContextStructure getContextStructure() {   return this.m_context;  }

	@Override
	public void setContextStructure(HAPContextStructure context) {  this.m_context = (HAPContext)context;  }

	@Override
	public void cloneToDataContext(HAPWithDataContext dataContext) {   dataContext.setContextStructure(this.m_context.cloneContext());  }

	@Override
	public Map<String, HAPDefinitionExpression> getEntityElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPDefinitionExpression getEntityElement(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addEntityElement(HAPDefinitionExpression entityElement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, HAPDefinitionConstant> getConstantDefinitions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPDefinitionConstant getConstantDefinition(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addConstantDefinition(HAPDefinitionConstant constantDef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HAPDefinitionExpressionGroup cloneExpressionGroupDefinition() {
		// TODO Auto-generated method stub
		return null;
	}
}
