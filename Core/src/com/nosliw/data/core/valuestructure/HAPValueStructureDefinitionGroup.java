package com.nosliw.data.core.valuestructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
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
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPInfoAlias;
import com.nosliw.data.core.structure.HAPReferenceRoot;
import com.nosliw.data.core.structure.HAPRoot;

//a group of context
//normally contexts are grouped according to type : public, private, ...
@HAPEntityWithAttribute
public class HAPValueStructureDefinitionGroup extends HAPSerializableImp implements HAPValueStructureDefinition{

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
	
	private Map<String, HAPValueStructureDefinitionFlat> m_flatStructureByCategary;
	private Map<String, String> m_categaryById;
	
	private HAPInfoImpSimple m_info;
	
	private HAPValueStructureDefinitionGroup m_parent;
	
	public HAPValueStructureDefinitionGroup(){
		this.m_info = new HAPInfoImpSimple(); 
		this.m_flatStructureByCategary = new LinkedHashMap<String, HAPValueStructureDefinitionFlat>();
		this.m_categaryById = new LinkedHashMap<String, String>();
		for(String type : getAllCategaries()){
			this.m_flatStructureByCategary.put(type, new HAPValueStructureDefinitionFlat());
		}
	}

	public HAPValueStructureDefinitionGroup(HAPInfo info){
		this();
		for(String name : info.getNames()) {
			this.m_info.setValue(name, info.getValue(name));
		}
	}
	
	@Override
	public String getStructureType() {	return HAPConstantShared.STRUCTURE_TYPE_VALUEGROUP;	}

	@Override
	public boolean isFlat() {	return false;	}
	
	@Override
	public boolean isEmpty() {
		for(String key : this.m_flatStructureByCategary.keySet()) {
			if(!this.m_flatStructureByCategary.get(key).isEmpty())  return false;
		}
		return true;
	}

	@Override
	public HAPRoot getRoot(String id) {
		String categary = this.m_categaryById.get(id);
		if(categary==null)  return null;
		return this.getFlat(categary).getRoot(id);
	}

	@Override
	public List<HAPRoot> getAllRoots(){
		List<HAPRoot> out = new ArrayList<HAPRoot>();
		for(String categary : this.m_flatStructureByCategary.keySet()) {
			out.addAll(this.m_flatStructureByCategary.get(categary).getAllRoots());
		}
		return out;
	}

	@Override
	public List<HAPInfoAlias> discoverRootAliasById(String id) {
		String categary = this.m_categaryById.get(id);
		int priority = this.getPriorityByCategary(categary);
		HAPRoot root = this.m_flatStructureByCategary.get(categary).getRoot(id);
		
		//if simple name is override
		boolean isSimpleNameValid = true;
		for(int i=priority-1; i>=0; i--) {
			String ctg = this.getCetegaryByPriority(i);
			if(ctg!=null) {
				HAPValueStructureDefinitionFlat flat = this.m_flatStructureByCategary.get(ctg);
				if(flat!=null) {
					List<HAPRoot> sameNameRoots = flat.resolveRoot(new HAPReferenceRootInFlat(root.getName()), false);
					if(sameNameRoots.size()>0) {
						isSimpleNameValid = false;
					}
				}
			}
		}
		
		//build alias
		List<HAPInfoAlias> out = new ArrayList<HAPInfoAlias>();
		out.add(new HAPInfoAlias(new HAPReferenceRootInGroup(categary, root.getName()).getFullName(), priority));
		if(isSimpleNameValid) {
			out.add(new HAPInfoAlias(root.getName(), priority+0.5));
		}
		return out;
	}

	private int getPriorityByCategary(String categary) {
		for(int i=0; i<getAllCategaries().length; i++) {
			if(categary.equals(getAllCategaries()[i])) {
				return i;
			}
		}
		return 9999;
	}
	
	private String getCetegaryByPriority(int priority) {
		return getAllCategaries()[priority];
	}
	
	@Override
	public HAPRoot addRoot(HAPReferenceRoot rootReference, HAPRoot root) {
		HAPReferenceRootInGroup groupRootReference = (HAPReferenceRootInGroup)rootReference;
		root.setName(groupRootReference.getName());
		return this.addRoot(groupRootReference.getCategary(), root);
	}

	public HAPRoot addRoot(String categary, HAPRoot root){
		root = root.cloneRoot();
		if(HAPBasicUtility.isStringEmpty(categary))   categary = HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC;
		if(HAPBasicUtility.isStringEmpty(root.getLocalId()))  root.setLocalId(new HAPReferenceRootInGroup(categary, root.getName()).getFullName());
		this.m_categaryById.put(root.getLocalId(), categary);
		return this.getFlat(categary).addRoot(root);
	}
	
	public HAPRoot newRoot(String categary, String name) {
		HAPRoot out = new HAPRoot();
		out.setName(name);
		this.addRoot(categary, out);
		return out;
	}

	@Override
	public List<HAPRoot> resolveRoot(HAPReferenceRoot rootReference, boolean createIfNotExist) {
		HAPReferenceRootInGroup groupReference = (HAPReferenceRootInGroup)rootReference;
		
		//candidate categary
		List<String> categaryCandidates = new ArrayList<String>();
		if(HAPBasicUtility.isStringNotEmpty(groupReference.getCategary()))  categaryCandidates.add(groupReference.getCategary());  //check path first
		else categaryCandidates.addAll(Arrays.asList(HAPValueStructureDefinitionGroup.getVisibleCategaries()));               //otherwise, use visible context
		
		List<HAPRoot> out = new ArrayList<HAPRoot>();
		for(String categary : categaryCandidates) {
			HAPValueStructureDefinitionFlat flat = this.m_flatStructureByCategary.get(categary);
			if(flat!=null) {
				out.addAll(flat.resolveRoot(new HAPReferenceRootInFlat(groupReference.getName()), false));
			}
		}
		
		if(out.size()==0 && createIfNotExist) {
			String categary = null;
			if(categaryCandidates.size()==1)  categary = categaryCandidates.get(0);
			HAPRoot newRoot = new HAPRoot();
			newRoot.setName(groupReference.getName());
			newRoot = this.addRoot(categary, newRoot);
			out.add(newRoot);
		}
		
		return out;
	}

	@Override
	public void hardMergeWith(HAPValueStructureDefinition structure){
		if(structure!=null) {
			if(structure.getStructureType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
				HAPValueStructureDefinitionGroup groupStructure = (HAPValueStructureDefinitionGroup)structure;
				for(String categary : groupStructure.getCategaries()){
					HAPValueStructureDefinitionFlat flat = groupStructure.getFlat(categary);
					for(HAPRoot root : flat.getRoots()) {
						this.addRoot(categary, root);
					}
				}
			}
			else  throw new RuntimeException();
		}
	}


	public void empty() {
		this.m_categaryById = new LinkedHashMap<String, String>();
		for(String type : getAllCategaries()) {
			this.getFlat(type).empty();
		}
	}
	
	public HAPInfo getInfo() {  return this.m_info;  }
	
	//mark all the context root ele in context group as processed
	public void processed() {		for(HAPValueStructureDefinitionFlat context : this.m_flatStructureByCategary.values())   context.processed();	}
	
	public HAPValueStructureDefinitionGroup getParent() {   return this.m_parent;   }
	public void setParent(HAPValueStructureDefinitionGroup parent) {  this.m_parent = parent;   }
	
	public Set<HAPRoot> getRootsByCategary(String categary){  return this.getFlat(categary).getRoots();  }
	
	private HAPValueStructureDefinitionFlat getFlat(String categary){
		HAPValueStructureDefinitionFlat out = this.m_flatStructureByCategary.get(categary);
		if(out==null) {
			out = this.setFlat(categary, new HAPValueStructureDefinitionFlat());
		}
		return out;
	}

	private HAPValueStructureDefinitionFlat setFlat(String categary, HAPValueStructureDefinitionFlat flat) {  
		HAPValueStructureDefinitionFlat out = flat;
		if(flat!=null) this.m_flatStructureByCategary.put(categary, flat);
		return out;
	}
	
	public Set<String> getCategaries(){  return this.m_flatStructureByCategary.keySet();   }
	
	@Override
	public void updateRootName(HAPUpdateName nameUpdate) {
		for(HAPValueStructureDefinitionFlat context : this.m_flatStructureByCategary.values()) {
			context.updateRootName(nameUpdate);
		}
	}
	
	@Override
	public HAPValueStructureDefinition cloneStructure() {
		return this.cloneContextGroup();
	}
	
	public HAPValueStructureDefinitionGroup cloneContextGroup() {
		HAPValueStructureDefinitionGroup out = new HAPValueStructureDefinitionGroup();
		this.cloneTo(out);
		return out;
	}

	public HAPValueStructureDefinitionGroup cloneContextGroupBase() {
		HAPValueStructureDefinitionGroup out = new HAPValueStructureDefinitionGroup();
		this.cloneToBase(out);
		return out;
	}

	public void cloneToBase(HAPValueStructureDefinitionGroup out) {
		out.m_info = this.m_info.cloneInfo();
	}
	
	public void cloneTo(HAPValueStructureDefinitionGroup out) {
		this.cloneToBase(out);
		for(String categary : this.m_flatStructureByCategary.keySet()) {
			HAPValueStructureDefinitionFlat flat = this.m_flatStructureByCategary.get(categary);
			out.m_flatStructureByCategary.put(categary, (HAPValueStructureDefinitionFlat)flat.cloneStructure());
		}
		out.m_categaryById.clear();
		out.m_categaryById.putAll(this.m_categaryById);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getStructureType());
		jsonMap.put(GROUP, HAPJsonUtility.buildJson(this.m_flatStructureByCategary, HAPSerializationFormat.JSON));
		jsonMap.put(INFO, HAPJsonUtility.buildJson(this.m_info, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		HAPParserValueStructure.parseValueStructureDefinitionGroup((JSONObject)json, this);
		return true;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPValueStructureDefinitionGroup) {
			HAPValueStructureDefinitionGroup contextGroup = (HAPValueStructureDefinitionGroup)obj;
			if(contextGroup.getCategaries().equals(this.getCategaries())) {
				for(String categary : this.getCategaries()) {
					out = contextGroup.getFlat(categary).equals(this.getFlat(categary));
					if(!out)  break;
				}
			}
		}
		return out;
	}

	public HAPValueStructureDefinitionFlat getPublicContext(){  return this.getFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);  }
	public HAPValueStructureDefinitionFlat getProtectedContext(){  return this.getFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED);  }
	public HAPValueStructureDefinitionFlat getInternalContext(){  return this.getFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL);  }
	public HAPValueStructureDefinitionFlat getPrivateContext(){  return this.getFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE);  }

	public void addPublicElement(HAPRoot root){  this.addRoot(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, root);  }
	public void addProtectedElement(HAPRoot root){  this.addRoot(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED, root);  }
	public void addInternalElement(HAPRoot root){  this.addRoot(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL, root);  }
	public void addPrivateElement(HAPRoot root){  this.addRoot(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE, root);  }

	public static String[] getAllCategaries(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE,
		};
		return contextTypes;
	}

	public static String[] getAllCategariesWithPriority(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
		};
		return contextTypes;
	}

	//context type that can be inherited by child
	public static String[] getInheritableCategaries(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
		};
		return contextTypes;
	}

	//visible to child
	public static String[] getVisibleCategaries(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
		};
		return contextTypes;
	}

	@Override
	public Object solidateConstantScript(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) {
		// TODO Auto-generated method stub
		return null;
	}


}
