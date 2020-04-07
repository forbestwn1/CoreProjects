package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.component.HAPWithAttachmentImp;
import com.nosliw.data.core.component.HAPWithDataContext;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextStructure;

public class HAPDefinitionExpressionGroupImp extends HAPWithAttachmentImp implements HAPDefinitionExpressionGroup, HAPWithDataContext{

	private Map<String, HAPDefinitionExpression> m_elements;
	
	private HAPContext m_context;
	
	public HAPDefinitionExpressionGroupImp() {
		this.m_elements = new LinkedHashMap<String, HAPDefinitionExpression>();
	}
	
	@Override
	public Map<String, HAPDefinitionExpression> getExpressions() {  return this.m_elements;  }  

	public void addExpression(HAPDefinitionExpression element) {   this.m_elements.put(element.getName(), element);   }

	@Override
	public HAPContextStructure getContextStructure() {   return this.m_context;  }

	@Override
	public void setContextStructure(HAPContextStructure context) {  this.m_context = (HAPContext)context;  }

	@Override
	public void cloneToDataContext(HAPWithDataContext dataContext) {   dataContext.setContextStructure(this.m_context.cloneContext());  }
}
