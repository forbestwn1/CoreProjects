package com.nosliw.data.core.valuestructure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.google.common.collect.Lists;
import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression.HAPUtilityScriptExpression;
import com.nosliw.data.core.structure.HAPElement;
import com.nosliw.data.core.structure.HAPInfoAlias;
import com.nosliw.data.core.structure.HAPReferenceRoot;
import com.nosliw.data.core.structure.HAPRoot;

@HAPEntityWithAttribute
public class HAPValueStructureDefinitionFlat extends HAPValueStructureDefinitionImp{

	@HAPAttribute
	public static final String FLAT = "flat";

	private Map<String, HAPRoot> m_rootById;
	private Map<String, String> m_idByName;
	
	public HAPValueStructureDefinitionFlat(){
		this.empty();
	}

	@Override
	public String getStructureType() {	return HAPConstantShared.STRUCTURE_TYPE_VALUEFLAT;	}

	@Override
	public boolean isFlat() {	return true;	}

	@Override
	public boolean isEmpty() {  return this.m_rootById.isEmpty(); }

	@Override
	public HAPRoot getRoot(String id) {  return this.m_rootById.get(id);  }

	@Override
	public List<HAPRoot> getAllRoots(){   return new ArrayList<HAPRoot>(this.m_rootById.values());      }

	@Override
	public HAPRoot addRoot(HAPReferenceRoot rootReference, HAPRoot root) {
		HAPReferenceRootInFlat flatRootReference = (HAPReferenceRootInFlat)rootReference;
		root.setName(flatRootReference.getName());
		return this.addRoot(root);
	}

	public HAPRoot addRoot(HAPRoot root) {
		root = root.cloneRoot();
		String name = root.getName();
		if(HAPBasicUtility.isStringEmpty(root.getLocalId()))  root.setLocalId(name);
		this.m_rootById.put(root.getLocalId(), root);
		this.m_idByName.put(name, root.getLocalId());
		return root;
	}
	
	public HAPRoot addRoot(String name, HAPElement structureEle) {
		HAPRoot root = new HAPRoot(structureEle);
		root.setName(name);
		return this.addRoot(root);  
	}
	
	public HAPRoot newRoot(String name) {  
		HAPRoot root = new HAPRoot();
		root.setName(name);
		return this.addRoot(root);  
	}

	@Override
	public List<HAPInfoAlias> discoverRootAliasById(String id) {
		List<HAPInfoAlias> out = new ArrayList<HAPInfoAlias>();
		String name = getNameById(id);
		if(name!=null) {
			HAPInfoAlias aliasInfo = new HAPInfoAlias(name, 0);
			out.add(aliasInfo);
		}
		return out;
	}

	@Override
	public HAPReferenceRoot getRootReferenceById(String id) {
		return new HAPReferenceRootInFlat(this.getNameById(id));
	}

	//whether root is inherited by child
	@Override
	public boolean isInheriable(String rootId) {   return true;	}

	@Override
	public boolean isExternalVisible(String rootId) {  return true; }

	@Override
	public List<HAPRoot> resolveRoot(HAPReferenceRoot rootReference, boolean createIfNotExist) {
		HAPReferenceRootInFlat flatReference = (HAPReferenceRootInFlat)rootReference;
		String rootName = flatReference.getName();
		HAPRoot root = this.getRootByName(rootName);
		if(createIfNotExist && root==null) 	root = this.newRoot(rootName);
		if(root!=null) return Lists.newArrayList(root);
		else return Lists.newArrayList();
	}

	@Override
	public Object solidateConstantScript(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) {
		HAPValueStructureDefinitionFlat out = new HAPValueStructureDefinitionFlat();
		this.cloneBaseToValueStructureDefinition(out);
		
		for(HAPRoot root : this.getAllRoots()) {
			String solidName = HAPUtilityScriptExpression.solidateLiterate(root.getName(), constants, runtimeEnv);
			HAPRoot newRoot = root.cloneRoot();
			newRoot.setDefinition(((HAPElement)newRoot.getDefinition().solidateConstantScript(constants, runtimeEnv)).cloneStructureElement());
			newRoot.setName(solidName);
			newRoot.setLocalId(null);
			out.addRoot(newRoot);
		}
		return out;
	}

	@Override
	public void hardMergeWith(HAPValueStructureDefinition parent){
		if(parent!=null) {
			if(parent.getStructureType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT)) {
				HAPValueStructureDefinitionFlat context  = (HAPValueStructureDefinitionFlat)parent;
				Set<HAPRoot> eles = context.getRoots();
				for(HAPRoot root : eles){
					this.addRoot(root.cloneRoot());
				}
			}
			else  throw new RuntimeException();
		}
	}

	public void empty() {
		this.m_rootById = new LinkedHashMap<String, HAPRoot>();
		this.m_idByName = new LinkedHashMap<String, String>();
	}

	//mark all the element in context as processed
	@Override
	public void processed() {   
		for(HAPRoot ele : this.m_rootById.values()) 	ele.getDefinition().processed();
	}

	public Set<String> getRootNames(){  return this.m_idByName.keySet();   }
	public Set<HAPRoot> getRoots(){  return new HashSet<HAPRoot>(this.m_rootById.values());  }
	
	
	public HAPValueStructureDefinitionFlat toSolidContext() {
		HAPValueStructureDefinitionFlat out = new HAPValueStructureDefinitionFlat();
		for(String name :this.getRootNames()) {
			out.addRoot(name, this.getRoot(name).getDefinition().getSolidStructureElement().cloneStructureElement());
		}
		return out;
	}
	
	@Override
	public HAPValueStructureDefinition cloneStructure() {	
		HAPValueStructureDefinitionFlat out = new HAPValueStructureDefinitionFlat();
		out.empty();
		for(String id : this.m_rootById.keySet()) {
			HAPRoot root = this.m_rootById.get(id);
			out.addRoot(root.cloneRoot());
		}
		return out;	
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;
			HAPParserValueStructure.parseValueStructureDefinitionFlat(jsonObj, this);
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		Map<String, String> rootsMap = new LinkedHashMap<String, String>();
		for(String name : this.m_idByName.keySet()) {
			rootsMap.put(name, this.getRootByName(name).toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(FLAT, HAPJsonUtility.buildMapJson(rootsMap));
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPValueStructureDefinitionFlat) {
			HAPValueStructureDefinitionFlat context = (HAPValueStructureDefinitionFlat)obj;
			if(context.getRootNames().equals(this.getRootNames())) {
				for(String eleName : this.getRootNames()) {
					out = this.getRootByName(eleName).equals(context.getRootByName(eleName));
					if(!out)  
						break;
				}
				return true;
			}
		}
		return out;
	}

	@Override
	public void updateRootName(HAPUpdateName nameUpdate) {
		// TODO Auto-generated method stub
		
	}

	private String getNameById(String id) {
		String out = null;
		for(String name : this.m_idByName.keySet()) {
			if(id.equals(this.m_idByName.get(name))) {
				out = name;
				break;
			}
		}
		return out;
	}
	
	private HAPRoot getRootByName(String name) {
		String id = this.m_idByName.get(name);
		if(id==null)  return null;
		return this.m_rootById.get(id);
	}

}
