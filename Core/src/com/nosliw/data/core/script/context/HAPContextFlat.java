package com.nosliw.data.core.script.context;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstant;

//flat context represent result of flat a context group to context
//one root in context group named Abc will generate two element in context: local Abc and global Abc___categary
//A has local relative parent of A__categary
//Also, we can find global name by local name
//flat context is back compatible with context
@HAPEntityWithAttribute
public class HAPContextFlat extends HAPSerializableImp{

	@HAPAttribute
	public static final String CONTEXT = "context";

	//used to map from local (abc) to global name (public_abc)
	@HAPAttribute
	public static final String LOCAL2GLOBAL = "local2Global";
	
	private HAPContext m_context;
	
	private Map<String, String> m_local2Global;
	
	Set<String> m_excludedInfo;
	
	public HAPContextFlat() {
		this.m_context = new HAPContext();
		this.m_local2Global = new LinkedHashMap<String, String>();
	}

	public HAPContextFlat(Set<String> excludedInfo) {
		this.m_context = new HAPContext();
		this.m_local2Global = new LinkedHashMap<String, String>();
		this.m_excludedInfo = excludedInfo;
	}

	public HAPContextFlat(HAPContext context) {
		this.m_context = context;
		this.m_local2Global = new LinkedHashMap<String, String>();
	}

	public void addElementFromContextGroup(HAPContextGroup contextGroup, String categary, String localName) {
		//build global name element
		String globalName = new HAPContextDefinitionRootId(categary, localName).getFullName();
		HAPContextDefinitionRoot globalNameRoot = contextGroup.getElement(categary, localName);
		this.m_context.addElement(globalName, globalNameRoot);
		
		//build local name element
		HAPContextDefinitionRoot localNameRoot = HAPUtilityContext.createRelativeContextDefinitionRoot(globalNameRoot, null, globalName, this.m_excludedInfo);
		localNameRoot.setDefinition(localNameRoot.getDefinition());
		localNameRoot.setDefaultValue(globalNameRoot.getDefaultValue());
		m_context.addElement(localName, localNameRoot);
		
		//local to global mapping
		this.addLocal2GlobalMapping(localName, globalName);
	}
	
	public void addElement(String name, HAPContextDefinitionRoot rootEle) {
		//remove name mapping element whatever
		this.removeLocal2GlobalMapping(name);
		this.m_context.addElement(name, rootEle);	
	}
	
	//global variable name
	//if item does not exist, then return null
	public String getGlobalName(String name) {
		String out = this.m_local2Global.get(name);
		if(out==null) {
			if(this.m_context.getElement(name)!=null)	out = name;
		}
		return out;
	}
	
	public HAPContextDefinitionRoot getGlobalRoot(String name) {
		String solidName = this.getGlobalName(name);
		if(solidName==null)   return null;
		else return this.m_context.getElement(solidName);
	}

	public Map<String, HAPContextDefinitionRoot> getGlobalRoots(){
		Map<String, HAPContextDefinitionRoot> out = new LinkedHashMap<String, HAPContextDefinitionRoot>();
		for(String elementName : this.m_context.getElementNames()) {
			if(this.m_local2Global.get(elementName)==null) {
				out.put(elementName, this.m_context.getElement(elementName));
			}
		}
		return out;
	}
	
	public HAPContext getContext() {  return this.m_context;  }
	
	
	public void updateRootName(HAPUpdateName nameUpdate) {
		HAPContext newContext = new HAPContext();
		//update context
		for(String eleName : this.m_context.getElementNames()) {
			HAPContextDefinitionRoot root = this.m_context.getElement(eleName);
			if(root.getDefinition() instanceof HAPContextDefinitionLeafRelative) {
				HAPContextDefinitionLeafRelative relative = (HAPContextDefinitionLeafRelative)root.getDefinition();
				if(HAPConstant.DATAASSOCIATION_RELATEDENTITY_SELF.equals(relative.getParent())) {
					//update local relative path
					HAPContextPath path = relative.getPath();
					relative.setPath(new HAPContextPath(new HAPContextDefinitionRootId(path.getRootElementId().getCategary(), nameUpdate.getUpdatedName(path.getRootElementId().getName())), path.getSubPath()));
				}
			}
			//update root name
			newContext.addElement(nameUpdate.getUpdatedName(eleName), root);
		}
		this.m_context = newContext;
		
		//update name in local2Global mapping
		Map<String, String> newMapping = new LinkedHashMap<String, String>();
		for(String name : this.m_local2Global.keySet()) {
			newMapping.put(nameUpdate.getUpdatedName(name), nameUpdate.getUpdatedName(this.m_local2Global.get(name)));
		}
		this.m_local2Global = newMapping;
	}
	
	
	private void addLocal2GlobalMapping(String local, String global) {		this.m_local2Global.put(local, global);	}
	private void removeLocal2GlobalMapping(String localName) {    this.m_local2Global.remove(localName);   }
	
	public Map<String, Object> getConstantValue(){		return this.m_context.getConstantValue();	}
	
	public HAPContextFlat getVariableContext() {   
		HAPContextFlat out = new HAPContextFlat();
		out.m_context = this.m_context.getVariableContext();
		out.m_local2Global.putAll(this.m_local2Global);
		return out;
	}

	public HAPContextFlat cloneContextFlat() {
		HAPContextFlat out = new HAPContextFlat();
		out.m_local2Global.putAll(this.m_local2Global);
		if(this.m_excludedInfo!=null) {
			out.m_excludedInfo = new HashSet<String>();
			out.m_excludedInfo.addAll(this.m_excludedInfo);
		}
		out.m_context = this.m_context.cloneContext();
		return null;
	}
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(LOCAL2GLOBAL, HAPJsonUtility.buildMapJson(this.m_local2Global));
		jsonMap.put(CONTEXT, this.m_context.toStringValue(HAPSerializationFormat.JSON));
	}
}
