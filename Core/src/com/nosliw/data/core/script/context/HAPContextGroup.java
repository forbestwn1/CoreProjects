package com.nosliw.data.core.script.context;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;

//a group of context
//normally contexts are grouped according to type : public, private, ...
@HAPEntityWithAttribute
public class HAPContextGroup extends HAPSerializableImp{

	@HAPAttribute
	public static final String INFO = "info";
	
	@HAPAttribute
	public static final String INFO_INHERIT = "inherit";

	@HAPAttribute
	public static final String INFO_POPUP = "popup";

	@HAPAttribute
	public static final String INFO_ESCALATE = "escalate";
	
	private Map<String, HAPContext> m_contexts;
	private HAPInfoImpSimple m_info;
	
	private HAPContextGroup m_parent;;
	
	public HAPContextGroup(){
		this.m_info = new HAPInfoImpSimple(); 
		this.m_contexts = new LinkedHashMap<String, HAPContext>();
		for(String type : getAllContextTypes()){
			this.m_contexts.put(type, new HAPContext());
		}
	}

	public HAPContextGroup(HAPInfo info){
		this();
		for(String name : info.getNames()) {
			this.m_info.setValue(name, info.getValue(name));
		}
	}
	
	public static String[] getAllContextTypes(){
		String[] contextTypes = {
			HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC,
			HAPConstant.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstant.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE,
		};
		return contextTypes;
	}

	public static String[] getContextTypesWithPriority(){
		String[] contextTypes = {
			HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE,
			HAPConstant.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstant.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC,
		};
		return contextTypes;
	}

	//context type that can be inherited by child
	public static String[] getInheritableContextTypes(){
		String[] contextTypes = {
			HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC,
			HAPConstant.UIRESOURCE_CONTEXTTYPE_PROTECTED,
		};
		return contextTypes;
	}

	//visible to child
	public static String[] getVisibleContextTypes(){
		String[] contextTypes = {
			HAPConstant.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstant.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC,
		};
		return contextTypes;
	}

	public void empty() {
		for(String type : getAllContextTypes()) {
			this.getContext(type).empty();
		}
	}
	
	public void setParent(HAPContextGroup parent) {  this.m_parent = parent;  }
	public HAPContextGroup getParent() {   return this.m_parent;    }
	
	public HAPInfo getInfo() {  return this.m_info;  }
	
	public HAPContext getPublicContext(){  return this.getContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC);  }
	public HAPContext getProtectedContext(){  return this.getContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PROTECTED);  }
	public HAPContext getInternalContext(){  return this.getContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_INTERNAL);  }
	public HAPContext getPrivateContext(){  return this.getContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE);  }

	public void addPublicElement(String name, HAPContextNodeRoot ele){  this.addElement(name, ele, HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC);  }
	public void addProtectedElement(String name, HAPContextNodeRoot ele){  this.addElement(name, ele, HAPConstant.UIRESOURCE_CONTEXTTYPE_PROTECTED);  }
	public void addInternalElement(String name, HAPContextNodeRoot ele){  this.addElement(name, ele, HAPConstant.UIRESOURCE_CONTEXTTYPE_INTERNAL);  }
	public void addPrivateElement(String name, HAPContextNodeRoot ele){  this.addElement(name, ele, HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE);  }

	public Map<String, HAPContextNodeRoot> getElements(String contextType){  return this.getContext(contextType).getElements();  }
	
	public void addElement(String name, HAPContextNodeRoot rootEle, String type){	this.getContext(type).addElement(name, rootEle);	}
	
	public HAPContext getContext(String type){		return this.m_contexts.get(type);	}
	
	public HAPContextNodeRoot getElement(HAPContextRootNodeId nodeId) {  return this.getElement(nodeId.getCategary(), nodeId.getName());   }
	public HAPContextNodeRoot getElement(String type, String name) {	return this.m_contexts.get(type).getElement(name);	}
	
	public HAPContextGroup clone() {
		HAPContextGroup out = new HAPContextGroup();
		this.cloneTo(out);
		return out;
	}
	
	public void cloneTo(HAPContextGroup out) {
		out.m_info = this.m_info.cloneInfo();
		for(String categary : this.m_contexts.keySet()) {
			HAPContext context = this.m_contexts.get(categary);
			for(String name : context.getElements().keySet()) {
				out.addElement(name, this.getElement(categary, name).cloneContextNodeRoot(), categary);
			}
		}
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
		jsonMap.put(INFO, HAPJsonUtility.buildJson(this.m_info, HAPSerializationFormat.JSON));
	}
}
