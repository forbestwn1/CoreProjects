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
public class HAPStructureValueDefinitionGroup extends HAPSerializableImp implements HAPStructureValueDefinition{

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
	
	private Map<String, HAPStructureValueDefinitionFlat> m_contexts;
	private HAPInfoImpSimple m_info;
	
	private HAPStructureValueDefinitionGroup m_parent;
	
	public HAPStructureValueDefinitionGroup(){
		this.m_info = new HAPInfoImpSimple(); 
		this.m_contexts = new LinkedHashMap<String, HAPStructureValueDefinitionFlat>();
		for(String type : getAllContextTypes()){
			this.m_contexts.put(type, new HAPStructureValueDefinitionFlat());
		}
	}

	public HAPStructureValueDefinitionGroup(HAPInfo info){
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
	public HAPRoot getRoot(String name, boolean createIfNotExist) {
		HAPIdContextDefinitionRoot rootId = new HAPIdContextDefinitionRoot(name);
		HAPRoot out = this.getRoot(rootId);   
		if(out==null && createIfNotExist) {
			out = this.addRoot(rootId.getName(), rootId.getCategary());
		}
		return out;
	}

	@Override
	public void hardMergeWith(HAPStructureValueDefinition context){
		if(context!=null) {
			if(context.getType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
				HAPStructureValueDefinitionGroup contextGroup = (HAPStructureValueDefinitionGroup)context;
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
	public void processed() {		for(HAPStructureValueDefinitionFlat context : this.m_contexts.values())   context.processed();	}
	
	public HAPStructureValueDefinitionGroup getParent() {   return this.m_parent;   }
	public void setParent(HAPStructureValueDefinitionGroup parent) {  this.m_parent = parent;   }
	
	public HAPStructureValueDefinitionFlat getPublicContext(){  return this.getContext(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);  }
	public HAPStructureValueDefinitionFlat getProtectedContext(){  return this.getContext(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED);  }
	public HAPStructureValueDefinitionFlat getInternalContext(){  return this.getContext(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL);  }
	public HAPStructureValueDefinitionFlat getPrivateContext(){  return this.getContext(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE);  }

	public void addPublicElement(String name, HAPRoot ele){  this.addElement(name, ele, HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);  }
	public void addProtectedElement(String name, HAPRoot ele){  this.addElement(name, ele, HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED);  }
	public void addInternalElement(String name, HAPRoot ele){  this.addElement(name, ele, HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL);  }
	public void addPrivateElement(String name, HAPRoot ele){  this.addElement(name, ele, HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE);  }

	public Map<String, HAPRoot> getElements(String contextType){  return this.getContext(contextType).getRoots();  }
	
	public void addElement(String name, HAPRoot rootEle, String type){
		if(HAPBasicUtility.isStringEmpty(type))   type = HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC;
		this.getContext(type).addRoot(name, rootEle);	
	}
	public HAPRoot addElement(String name, String type) {
		HAPRoot out = new HAPRoot();
		this.addElement(name, out, type);
		return out;
	}
	
	public HAPStructureValueDefinitionFlat getContext(String type){		return this.m_contexts.get(type);	}
	public HAPStructureValueDefinitionFlat getChildContext(String type){		
		HAPStructureValueDefinitionFlat out = this.m_contexts.get(type);
		if(out==null) {
			out = new HAPStructureValueDefinitionFlat();
			this.setContext(type, out);
		}
		return out;
	}
	public void setContext(String type, HAPStructureValueDefinitionFlat context) {   if(context!=null) this.m_contexts.put(type, context);   }
	public HAPStructureValueDefinitionFlat removeContext(String type) { 
		HAPStructureValueDefinitionFlat out = this.m_contexts.get(type);
		this.m_contexts.put(type, new HAPStructureValueDefinitionFlat());
		return out;
	}

	public HAPRoot getElement(HAPIdContextDefinitionRoot nodeId) {  return this.getRoot(nodeId.getCategary(), nodeId.getName());   }
	public HAPRoot getElement(String type, String name) {
		HAPStructureValueDefinitionFlat context = this.getContext(type);
		return context==null?null:context.getRoot(name);
	}
	
	public Set<String> getContextTypes(){  return this.m_contexts.keySet();   }
	
	@Override
	public void updateRootName(HAPUpdateName nameUpdate) {
		for(HAPStructureValueDefinitionFlat context : this.m_contexts.values()) {
			context.updateRootName(nameUpdate);
		}
	}
	
	@Override
	public HAPStructureValueDefinition cloneStructure() {
		return this.cloneContextGroup();
	}
	
	public HAPStructureValueDefinitionGroup cloneContextGroup() {
		HAPStructureValueDefinitionGroup out = new HAPStructureValueDefinitionGroup();
		this.cloneTo(out);
		return out;
	}

	public HAPStructureValueDefinitionGroup cloneContextGroupBase() {
		HAPStructureValueDefinitionGroup out = new HAPStructureValueDefinitionGroup();
		this.cloneToBase(out);
		return out;
	}

	public void cloneToBase(HAPStructureValueDefinitionGroup out) {
		out.m_info = this.m_info.cloneInfo();
	}
	
	public void cloneTo(HAPStructureValueDefinitionGroup out) {
		this.cloneToBase(out);
		for(String categary : this.m_contexts.keySet()) {
			HAPStructureValueDefinitionFlat context = this.m_contexts.get(categary);
			for(String name : context.getRoots().keySet()) {
				out.addElement(name, this.getElement(categary, name).cloneRoot(), categary);
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
		if(obj instanceof HAPStructureValueDefinitionGroup) {
			HAPStructureValueDefinitionGroup contextGroup = (HAPStructureValueDefinitionGroup)obj;
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
