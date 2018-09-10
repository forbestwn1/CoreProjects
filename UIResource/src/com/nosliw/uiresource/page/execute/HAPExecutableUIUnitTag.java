package com.nosliw.uiresource.page.execute;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitTag;

public class HAPExecutableUIUnitTag extends HAPExecutableUIUnit{

	@HAPAttribute
	public static final String TAGNAME = "tagName";

	@HAPAttribute
	public static final String TAGCONTEXT = "tagContext";
	
	//context for tag
	private HAPContextGroup m_tagContext;
	private HAPContext m_flatTagContext;

	public HAPExecutableUIUnitTag(HAPDefinitionUIUnitTag uiTagDefinition) {
		super(uiTagDefinition);
	}

	public HAPContextGroup getTagContext(){  return this.m_tagContext;   }
	public void setTagContext(HAPContextGroup context) {  this.m_tagContext = context;   }
	public HAPContext getFlatTagContext() { return this.m_flatTagContext;  }
	public void setFlatTagContext(HAPContext context) {  this.m_flatTagContext = context;   }
	public HAPContext getTagVariableContext() {  return this.m_flatTagContext.getVariableContext();  }
	
	public HAPDefinitionUIUnitTag getUIUnitTagDefinition() {   return (HAPDefinitionUIUnitTag)this.getUIUnitDefinition();  }
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TAGNAME, this.getUIUnitTagDefinition().getTagName());
		jsonMap.put(TAGCONTEXT, this.getTagVariableContext().toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TAGNAME, this.getUIUnitTagDefinition().getTagName());
		jsonMap.put(TAGCONTEXT, this.getTagVariableContext().toStringValue(HAPSerializationFormat.JSON_FULL));
	}
}
