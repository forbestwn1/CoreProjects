package com.nosliw.data.core.structure.value;

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
import com.nosliw.data.core.structure.HAPParserContext;
import com.nosliw.data.core.structure.HAPReferenceRoot;
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
	
	private Map<String, HAPStructureValueDefinitionFlat> m_flatStructureByCategary;
	private Map<String, String> m_categaryById;
	
	private HAPInfoImpSimple m_info;
	
	private HAPStructureValueDefinitionGroup m_parent;
	
	public HAPStructureValueDefinitionGroup(){
		this.m_info = new HAPInfoImpSimple(); 
		this.m_flatStructureByCategary = new LinkedHashMap<String, HAPStructureValueDefinitionFlat>();
		for(String type : getAllCategaries()){
			this.m_flatStructureByCategary.put(type, new HAPStructureValueDefinitionFlat());
		}
	}

	public HAPStructureValueDefinitionGroup(HAPInfo info){
		this();
		for(String name : info.getNames()) {
			this.m_info.setValue(name, info.getValue(name));
		}
	}
	
	@Override
	public String getStructureType() {	return HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT;	}

	@Override
	public boolean isFlat() {	return false;	}
	
	@Override
	public boolean isEmpty() {
		for(String key : this.m_flatStructureByCategary.keySet()) {
			if(!this.m_flatStructureByCategary.get(key).isEmpty())  return false;
		}
		return true;
	}

	public HAPRoot addRoot(String categary, HAPRoot root){
		root = root.cloneRoot();
		if(HAPBasicUtility.isStringEmpty(categary))   categary = HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC;
		if(HAPBasicUtility.isStringEmpty(root.getLocalId()))  root.setLocalId(new HAPReferenceRootInGroup(categary, root.getName()).getFullName());
		this.m_categaryById.put(root.getId(), categary);
		this.getFlat(categary).addRoot(root);
		return root;
	}
	public HAPRoot newRoot(String categary, String name) {
		HAPRoot out = new HAPRoot();
		out.setName(name);
		this.addRoot(categary, out);
		return out;
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
	public List<HAPRoot> resolveRoot(HAPReferenceRoot rootReference, boolean createIfNotExist) {
		HAPReferenceRootInGroup groupReference = (HAPReferenceRootInGroup)rootReference;
		
		//candidate categary
		List<String> categaryCandidates = new ArrayList<String>();
		if(HAPBasicUtility.isStringNotEmpty(groupReference.getCategary()))  categaryCandidates.add(groupReference.getCategary());  //check path first
		else categaryCandidates.addAll(Arrays.asList(HAPStructureValueDefinitionGroup.getVisibleCategaries()));               //otherwise, use visible context
		
		List<HAPRoot> out = new ArrayList<HAPRoot>();
		for(String categary : categaryCandidates) {
			HAPStructureValueDefinitionFlat flat = this.m_flatStructureByCategary.get(categary);
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
	public void hardMergeWith(HAPStructureValueDefinition structure){
		if(structure!=null) {
			if(structure.getStructureType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
				HAPStructureValueDefinitionGroup groupStructure = (HAPStructureValueDefinitionGroup)structure;
				for(String categary : groupStructure.getCategaries()){
					HAPStructureValueDefinitionFlat flat = groupStructure.getFlat(categary);
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
	public void processed() {		for(HAPStructureValueDefinitionFlat context : this.m_flatStructureByCategary.values())   context.processed();	}
	
	public HAPStructureValueDefinitionGroup getParent() {   return this.m_parent;   }
	public void setParent(HAPStructureValueDefinitionGroup parent) {  this.m_parent = parent;   }
	
	public Set<HAPRoot> getRootsByCategary(String categary){  return this.getFlat(categary).getRoots();  }
	
	private HAPStructureValueDefinitionFlat getFlat(String categary){
		HAPStructureValueDefinitionFlat out = this.m_flatStructureByCategary.get(categary);
		if(out==null) {
			out = this.setFlat(categary, new HAPStructureValueDefinitionFlat());
		}
		return out;
	}

	private HAPStructureValueDefinitionFlat setFlat(String categary, HAPStructureValueDefinitionFlat flat) {  
		HAPStructureValueDefinitionFlat out = flat;
		if(flat!=null) this.m_flatStructureByCategary.put(categary, flat);
		return out;
	}
	
	public Set<String> getCategaries(){  return this.m_flatStructureByCategary.keySet();   }
	
	@Override
	public void updateRootName(HAPUpdateName nameUpdate) {
		for(HAPStructureValueDefinitionFlat context : this.m_flatStructureByCategary.values()) {
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
		for(String categary : this.m_flatStructureByCategary.keySet()) {
			HAPStructureValueDefinitionFlat context = this.m_flatStructureByCategary.get(categary);
			for(String name : context.getRoots().keySet()) {
				out.addRoot(name, this.getElement(categary, name).cloneRoot(), categary);
			}
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(GROUP, HAPJsonUtility.buildJson(this.m_flatStructureByCategary, HAPSerializationFormat.JSON));
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
			if(contextGroup.getCategaries().equals(this.getCategaries())) {
				for(String categary : this.getCategaries()) {
					out = contextGroup.getFlat(categary).equals(this.getFlat(categary));
					if(!out)  break;
				}
			}
		}
		return out;
	}

	public HAPStructureValueDefinitionFlat getPublicContext(){  return this.getFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);  }
	public HAPStructureValueDefinitionFlat getProtectedContext(){  return this.getFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED);  }
	public HAPStructureValueDefinitionFlat getInternalContext(){  return this.getFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL);  }
	public HAPStructureValueDefinitionFlat getPrivateContext(){  return this.getFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE);  }

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


}
