package com.nosliw.data.core.structure.value;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.google.common.collect.Lists;
import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.structure.HAPConfigureReferenceResolve;
import com.nosliw.data.core.structure.HAPElement;
import com.nosliw.data.core.structure.HAPReferenceRoot;
import com.nosliw.data.core.structure.HAPRoot;

@HAPEntityWithAttribute
public class HAPStructureValueDefinitionFlat extends HAPSerializableImp implements HAPStructureValueDefinition{

	@HAPAttribute
	public static final String FLAT = "flat";

	private Map<String, HAPRoot> m_rootById;
	private Map<String, String> m_idByName;
	
	public HAPStructureValueDefinitionFlat(){
		this.empty();
	}

	@Override
	public String getType() {	return HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT;	}

	@Override
	public boolean isFlat() {	return true;	}

	@Override
	public boolean isEmpty() {  return this.m_rootById.isEmpty(); }

	@Override
	public HAPRoot getRoot(String id) {  return this.m_rootById.get(id);  }

	@Override
	public List<HAPRoot> resolveRoot(HAPReferenceRoot rootReference, HAPConfigureReferenceResolve configure, boolean createIfNotExist) {
		HAPReferenceRootInFlat flatReference = (HAPReferenceRootInFlat)rootReference;
		String rootName = flatReference.getName();
		HAPRoot root = this.getRootByName(rootName);
		if(createIfNotExist && root==null) 	root = this.newRoot(rootName);
		if(root!=null) return Lists.newArrayList(root);
		else return Lists.newArrayList();
	}

	@Override
	public void hardMergeWith(HAPStructureValueDefinition parent){
		if(parent!=null) {
			if(parent.getType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT)) {
				HAPStructureValueDefinitionFlat context  = (HAPStructureValueDefinitionFlat)parent;
				Map<String, HAPRoot> eles = context.getRoots();
				for(String rootName : eles.keySet()){
					this.addRoot(eles.get(rootName).cloneRoot());
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
	public void processed() {   
		for(HAPRoot ele : this.m_rootById.values()) 	ele.getDefinition().processed();
	}

	public Set<String> getRootNames(){  return this.m_idByName.keySet();   }
	public Map<String, HAPRoot> getRoots(){  return this.m_rootById;  }
	
	
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

	public HAPStructureValueDefinitionFlat toSolidContext() {
		HAPStructureValueDefinitionFlat out = new HAPStructureValueDefinitionFlat();
		for(String name :this.getRootNames()) {
			out.addRoot(name, this.getRoot(name).getDefinition().getSolidStructureElement().cloneStructureElement());
		}
		return out;
	}
	
	@Override
	public HAPStructureValueDefinition cloneStructure() {	
		HAPStructureValueDefinitionFlat out = new HAPStructureValueDefinitionFlat();
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
			HAPParserValueStructure.parseparseValueStructureDefinitionFlat(jsonObj, this);
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getType());
		
		Map<String, String> rootsMap = new LinkedHashMap<String, String>();
		for(String name : this.m_idByName.keySet()) {
			rootsMap.put(name, this.getRootByName(name).toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(FLAT, HAPJsonUtility.buildMapJson(rootsMap));
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPStructureValueDefinitionFlat) {
			HAPStructureValueDefinitionFlat context = (HAPStructureValueDefinitionFlat)obj;
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

	private HAPRoot getRootByName(String name) {
		String id = this.m_idByName.get(name);
		if(id==null)  return null;
		return this.m_rootById.get(id);
	}
	
}
