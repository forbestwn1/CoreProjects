package com.nosliw.data.core.structure.value;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.structure.HAPIdContextDefinitionRoot;
import com.nosliw.data.core.structure.HAPParserContext;
import com.nosliw.data.core.structure.HAPRoot;

//a group of context
//normally contexts are grouped according to type : public, private, ...
@HAPEntityWithAttribute
public class HAPContextStructureValueDefinitionGroup extends HAPSerializableImp implements HAPContextStructureValueDefinition{

	@HAPAttribute
	public static final String GROUP = "group";

	@HAPAttribute
	public static final String INFO = "info";
	
	@HAPAttribute
	public static final String INFO_INHERIT = "inherit";

	@HAPAttribute
	public static final String INFO_POPUP = "popup";

	@HAPAttribute
	public static final String INFO_ESCALATE = "escalate";
	
	private Map<String, HAPContextStructureValueDefinitionFlat> m_contexts;
	private HAPInfoImpSimple m_info;
	
	private HAPContextStructureValueDefinitionGroup m_parent;
	
	public HAPContextStructureValueDefinitionGroup(){
		this.m_info = new HAPInfoImpSimple(); 
		this.m_contexts = new LinkedHashMap<String, HAPContextStructureValueDefinitionFlat>();
		for(String type : getAllContextTypes()){
			this.m_contexts.put(type, new HAPContextStructureValueDefinitionFlat());
		}
	}

	public HAPContextStructureValueDefinitionGroup(HAPInfo info){
		this();
		for(String name : info.getNames()) {
			this.m_info.setValue(name, info.getValue(name));
		}
	}
	
	@Override
	public String getType() {	return HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT;	}

	@Override
	public boolean isFlat() {	return false;	}
	
	@Override
	public boolean isEmpty() {
		for(String key : this.m_contexts.keySet()) {
			if(!this.m_contexts.get(key).isEmpty())  return false;
		}
		return true;
	}

	@Override
	public HAPRoot getElement(String name, boolean createIfNotExist) {
		HAPIdContextDefinitionRoot rootId = new HAPIdContextDefinitionRoot(name);
		HAPRoot out = this.getElement(rootId);   
		if(out==null && createIfNotExist) {
			out = this.addElement(rootId.getName(), rootId.getCategary());
		}
		return out;
	}

	@Override
	public void hardMergeWith(HAPContextStructureValueDefinition context){
		if(context!=null) {
			if(context.getType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
				HAPContextStructureValueDefinitionGroup contextGroup = (HAPContextStructureValueDefinitionGroup)context;
				for(String type : contextGroup.getContextTypes()){
					this.getChildContext(type).hardMergeWith(contextGroup.getContext(type));
				}
			}
			else  throw new RuntimeException();
		}
	}

	public static String[] getAllContextTypes(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE,
		};
		return contextTypes;
	}

	public static String[] getContextTypesWithPriority(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
		};
		return contextTypes;
	}

	//context type that can be inherited by child
	public static String[] getInheritableContextTypes(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
		};
		return contextTypes;
	}

	//visible to child
	public static String[] getVisibleContextTypes(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
		};
		return contextTypes;
	}

	public void empty() {
		for(String type : getAllContextTypes()) {
			this.getContext(type).empty();
		}
	}
	
	public HAPInfo getInfo() {  return this.m_info;  }
	
	//mark all the context root ele in context group as processed
	public void processed() {		for(HAPContextStructureValueDefinitionFlat context : this.m_contexts.values())   context.processed();	}
	
	public HAPContextStructureValueDefinitionGroup getParent() {   return this.m_parent;   }
	public void setParent(HAPContextStructureValueDefinitionGroup parent) {  this.m_parent = parent;   }
	
	public HAPContextStructureValueDefinitionFlat getPublicContext(){  return this.getContext(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);  }
	public HAPContextStructureValueDefinitionFlat getProtectedContext(){  return this.getContext(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED);  }
	public HAPContextStructureValueDefinitionFlat getInternalContext(){  return this.getContext(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL);  }
	public HAPContextStructureValueDefinitionFlat getPrivateContext(){  return this.getContext(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE);  }

	public void addPublicElement(String name, HAPRoot ele){  this.addElement(name, ele, HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);  }
	public void addProtectedElement(String name, HAPRoot ele){  this.addElement(name, ele, HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED);  }
	public void addInternalElement(String name, HAPRoot ele){  this.addElement(name, ele, HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL);  }
	public void addPrivateElement(String name, HAPRoot ele){  this.addElement(name, ele, HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE);  }

	public Map<String, HAPRoot> getElements(String contextType){  return this.getContext(contextType).getElements();  }
	
	public void addElement(String name, HAPRoot rootEle, String type){
		if(HAPBasicUtility.isStringEmpty(type))   type = HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC;
		this.getContext(type).addElement(name, rootEle);	
	}
	public HAPRoot addElement(String name, String type) {
		HAPRoot out = new HAPRoot();
		this.addElement(name, out, type);
		return out;
	}
	
	public HAPContextStructureValueDefinitionFlat getContext(String type){		return this.m_contexts.get(type);	}
	public HAPContextStructureValueDefinitionFlat getChildContext(String type){		
		HAPContextStructureValueDefinitionFlat out = this.m_contexts.get(type);
		if(out==null) {
			out = new HAPContextStructureValueDefinitionFlat();
			this.setContext(type, out);
		}
		return out;
	}
	public void setContext(String type, HAPContextStructureValueDefinitionFlat context) {   if(context!=null) this.m_contexts.put(type, context);   }
	public HAPContextStructureValueDefinitionFlat removeContext(String type) { 
		HAPContextStructureValueDefinitionFlat out = this.m_contexts.get(type);
		this.m_contexts.put(type, new HAPContextStructureValueDefinitionFlat());
		return out;
	}

	public HAPRoot getElement(HAPIdContextDefinitionRoot nodeId) {  return this.getElement(nodeId.getCategary(), nodeId.getName());   }
	public HAPRoot getElement(String type, String name) {
		HAPContextStructureValueDefinitionFlat context = this.getContext(type);
		return context==null?null:context.getElement(name);
	}
	
	public Set<String> getContextTypes(){  return this.m_contexts.keySet();   }
	
	@Override
	public void updateRootName(HAPUpdateName nameUpdate) {
		for(HAPContextStructureValueDefinitionFlat context : this.m_contexts.values()) {
			context.updateRootName(nameUpdate);
		}
	}
	
	@Override
	public HAPContextStructureValueDefinition cloneContextStructure() {
		return this.cloneContextGroup();
	}
	
	public HAPContextStructureValueDefinitionGroup cloneContextGroup() {
		HAPContextStructureValueDefinitionGroup out = new HAPContextStructureValueDefinitionGroup();
		this.cloneTo(out);
		return out;
	}

	public HAPContextStructureValueDefinitionGroup cloneContextGroupBase() {
		HAPContextStructureValueDefinitionGroup out = new HAPContextStructureValueDefinitionGroup();
		this.cloneToBase(out);
		return out;
	}

	public void cloneToBase(HAPContextStructureValueDefinitionGroup out) {
		out.m_info = this.m_info.cloneInfo();
	}
	
	public void cloneTo(HAPContextStructureValueDefinitionGroup out) {
		this.cloneToBase(out);
		for(String categary : this.m_contexts.keySet()) {
			HAPContextStructureValueDefinitionFlat context = this.m_contexts.get(categary);
			for(String name : context.getElements().keySet()) {
				out.addElement(name, this.getElement(categary, name).cloneContextDefinitionRoot(), categary);
			}
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(GROUP, HAPJsonUtility.buildJson(this.m_contexts, HAPSerializationFormat.JSON));
		jsonMap.put(INFO, HAPJsonUtility.buildJson(this.m_info, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		HAPParserContext.parseValueStructureDefinitionGroup((JSONObject)json, this);
		return true;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPContextStructureValueDefinitionGroup) {
			HAPContextStructureValueDefinitionGroup contextGroup = (HAPContextStructureValueDefinitionGroup)obj;
			if(contextGroup.getContextTypes().equals(this.getContextTypes())) {
				for(String categary : this.getContextTypes()) {
					out = contextGroup.getContext(categary).equals(this.getContext(categary));
					if(!out)  break;
				}
			}
		}
		return out;
	}

}
