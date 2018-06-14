package com.nosliw.uiresource.context;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;

//a group of context
//normally contexts are grouped according to type : public, private, ...
@HAPEntityWithAttribute
public class HAPContextGroup extends HAPSerializableImp{
	
	private Map<String, HAPContext> m_contexts;
	
	public HAPContextGroup(){
		this.m_contexts = new LinkedHashMap<String, HAPContext>();
		for(String type : getContextTypes()){
			this.m_contexts.put(type, new HAPContext());
		}
	}

	public static String[] getContextTypes(){
		String[] contextTypes = {
				HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC,
				HAPConstant.UIRESOURCE_CONTEXTTYPE_INTERNAL,
				HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE,
				HAPConstant.UIRESOURCE_CONTEXTTYPE_EXCLUDED
				};
		return contextTypes;
	}
	
	public HAPContext getPublicContext(){  return this.getContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC);  }
	public HAPContext getInternalContext(){  return this.getContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_INTERNAL);  }
	public HAPContext getPrivateContext(){  return this.getContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE);  }
	public HAPContext getExcludedContext(){  return this.getContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_EXCLUDED);  }

	public void addElement(String name, HAPContextNodeRoot rootEle, String type){
		this.getContext(type).addElement(name, rootEle);
	}
	
	public HAPContext getContext(String type){
		return this.m_contexts.get(type);
	}
	
	public void hardMergeWith(HAPContextGroup contextGroup){
		for(String type : this.m_contexts.keySet()){
			this.getContext(type).hardMergeWith(contextGroup.getContext(type));
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		for(String type : this.m_contexts.keySet()){
			jsonMap.put(type, HAPJsonUtility.buildJson(this.m_contexts.get(type), HAPSerializationFormat.JSON));
		}
	}
}
