package com.nosliw.data.core.valuestructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression.HAPUtilityScriptExpression;
import com.nosliw.data.core.structure.HAPElementStructure;
import com.nosliw.data.core.structure.HAPInfoAlias;
import com.nosliw.data.core.structure.HAPReferenceRoot;
import com.nosliw.data.core.structure.HAPRootStructure;

//a group of context
//normally contexts are grouped according to type : public, private, ...
@HAPEntityWithAttribute
public class HAPValueStructureDefinitionGroup extends HAPValueStructureDefinitionImp{

	@HAPAttribute
	public static final String GROUP = "group";

	@HAPAttribute
	public static final String INFO_INHERIT = "inherit";

	@HAPAttribute
	public static final String INFO_POPUP = "popup";

	@HAPAttribute
	public static final String INFO_ESCALATE = "escalate";
	
	private Map<String, HAPValueStructureDefinitionFlat> m_flatStructureByCategary;
	private Map<String, String> m_categaryById;
	
	public HAPValueStructureDefinitionGroup(){
		this.m_flatStructureByCategary = new LinkedHashMap<String, HAPValueStructureDefinitionFlat>();
		this.m_categaryById = new LinkedHashMap<String, String>();
		for(String type : getAllCategaries()){
			this.m_flatStructureByCategary.put(type, new HAPValueStructureDefinitionFlat());
		}
	}

	public HAPValueStructureDefinitionGroup(HAPInfo info){
		this();
		this.setInfo(info.cloneInfo());
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
	public HAPRootStructure getRoot(String id) {
		String categary = this.m_categaryById.get(id);
		if(categary==null)  return null;
		return this.getFlat(categary).getRoot(id);
	}

	@Override
	public List<HAPRootStructure> getAllRoots(){
		List<HAPRootStructure> out = new ArrayList<HAPRootStructure>();
		for(String categary : this.m_flatStructureByCategary.keySet()) {
			out.addAll(this.m_flatStructureByCategary.get(categary).getAllRoots());
		}
		return out;
	}

	@Override
	public List<HAPInfoAlias> discoverRootAliasById(String id) {
		String categary = this.m_categaryById.get(id);
		int priority = this.getPriorityByCategary(categary);
		HAPRootStructure root = this.m_flatStructureByCategary.get(categary).getRoot(id);
		
		//if simple name is override
		boolean isSimpleNameValid = true;
		for(int i=priority-1; i>=0; i--) {
			String ctg = this.getCetegaryByPriority(i);
			if(ctg!=null) {
				HAPValueStructureDefinitionFlat flat = this.m_flatStructureByCategary.get(ctg);
				if(flat!=null) {
					List<HAPRootStructure> sameNameRoots = flat.resolveRoot(new HAPReferenceRootInFlat(root.getName()), false);
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

	@Override
	public HAPReferenceRoot getRootReferenceById(String id) {
		String categary = this.m_categaryById.get(id);
		String name = this.getRoot(id).getName();
		return new HAPReferenceRootInGroup(categary, name);
	}

	@Override
	public Object solidateConstantScript(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) {
		HAPValueStructureDefinitionGroup out = new HAPValueStructureDefinitionGroup();
		this.cloneBaseToValueStructureDefinition(out);
		
		for(String categary : this.getCategaries()) {
			for(HAPRootStructure root : this.getRootsByCategary(categary)) {
				String solidName = HAPUtilityScriptExpression.solidateLiterate(root.getName(), constants, runtimeEnv);
				HAPRootStructure newRoot = root.cloneRoot();
				newRoot.setDefinition(((HAPElementStructure)newRoot.getDefinition().solidateConstantScript(constants, runtimeEnv)).cloneStructureElement());
				newRoot.setName(solidName);
				newRoot.setLocalId(null);
				out.addRoot(categary, newRoot);
			}
		}
		return out;
	}

	@Override
	public boolean isExternalVisible(String rootId) {
		HAPReferenceRootInGroup rootRef = (HAPReferenceRootInGroup)this.getRootReferenceById(rootId);
		return HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC.equals(rootRef.getCategary());
	}

	//whether root is inherited by child
	@Override
	public boolean isInheriable(String rootId) {
		HAPReferenceRootInGroup rootRef = (HAPReferenceRootInGroup)this.getRootReferenceById(rootId);
		return new HashSet<>(Arrays.asList(this.getInheritableCategaries())).contains(rootRef.getCategary());
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
	public HAPRootStructure addRoot(HAPReferenceRoot rootReference, HAPRootStructure root) {
		HAPReferenceRootInGroup groupRootReference = (HAPReferenceRootInGroup)rootReference;
		root.setName(groupRootReference.getName());
		return this.addRoot(groupRootReference.getCategary(), root);
	}

	public HAPRootStructure addRoot(String categary, HAPRootStructure root){
		root = root.cloneRoot();
		if(HAPBasicUtility.isStringEmpty(categary))   categary = HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC;
		if(HAPBasicUtility.isStringEmpty(root.getLocalId()))  root.setLocalId(new HAPReferenceRootInGroup(categary, root.getName()).getFullName());
		this.m_categaryById.put(root.getLocalId(), categary);
		return this.getFlat(categary).addRoot(root);
	}
	
	public HAPRootStructure newRoot(String categary, String name) {
		HAPRootStructure out = new HAPRootStructure();
		out.setName(name);
		this.addRoot(categary, out);
		return out;
	}

	@Override
	public List<HAPRootStructure> resolveRoot(HAPReferenceRoot rootReference, boolean createIfNotExist) {
		HAPReferenceRootInGroup groupReference = (HAPReferenceRootInGroup)rootReference;
		
		//candidate categary
		List<String> categaryCandidates = new ArrayList<String>();
		if(HAPBasicUtility.isStringNotEmpty(groupReference.getCategary()))  categaryCandidates.add(groupReference.getCategary());  //check path first
		else categaryCandidates.addAll(Arrays.asList(HAPValueStructureDefinitionGroup.getVisibleCategaries()));               //otherwise, use visible context
		
		List<HAPRootStructure> out = new ArrayList<HAPRootStructure>();
		for(String categary : categaryCandidates) {
			HAPValueStructureDefinitionFlat flat = this.m_flatStructureByCategary.get(categary);
			if(flat!=null) {
				out.addAll(flat.resolveRoot(new HAPReferenceRootInFlat(groupReference.getName()), false));
			}
		}
		
		if(out.size()==0 && createIfNotExist) {
			String categary = null;
			if(categaryCandidates.size()==1)  categary = categaryCandidates.get(0);
			HAPRootStructure newRoot = new HAPRootStructure();
			newRoot.setName(groupReference.getName());
			newRoot = this.addRoot(categary, newRoot);
			out.add(newRoot);
		}
		
		return out;
	}

	@Override
	public void hardMergeWith(HAPValueStructure structure){
		if(structure!=null) {
			if(structure.getStructureType().equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
				HAPValueStructureDefinitionGroup groupStructure = (HAPValueStructureDefinitionGroup)structure;
				for(String categary : groupStructure.getCategaries()){
					HAPValueStructureDefinitionFlat flat = groupStructure.getFlat(categary);
					for(HAPRootStructure root : flat.getRoots()) {
						this.addRoot(categary, root);
					}
				}
			}
			else  throw new RuntimeException();
		}
	}

	//mark all the context root ele in context group as processed
	@Override
	public void processed() {		for(HAPValueStructureDefinitionFlat context : this.m_flatStructureByCategary.values())   context.processed();	}
	
	public void empty() {
		this.m_categaryById = new LinkedHashMap<String, String>();
		for(String type : getAllCategaries()) {
			this.getFlat(type).empty();
		}
	}
	
	public Set<HAPRootStructure> getRootsByCategary(String categary){  return this.getFlat(categary).getRoots();  }
	
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
	public HAPValueStructure cloneStructure() {
		return this.cloneValueStructureGroup();
	}
	
	public HAPValueStructureDefinitionGroup cloneValueStructureGroup() {
		HAPValueStructureDefinitionGroup out = new HAPValueStructureDefinitionGroup();
		this.cloneToGroupValueStructure(out);
		return out;
	}

	public HAPValueStructureDefinitionGroup cloneValueStructureGroupBase() {
		HAPValueStructureDefinitionGroup out = new HAPValueStructureDefinitionGroup();
		this.cloneBaseToValueStructureDefinition(out);
		return out;
	}

	public void cloneToGroupValueStructure(HAPValueStructureDefinitionGroup out) {
		this.cloneBaseToValueStructureDefinition(out);
		for(String categary : this.m_flatStructureByCategary.keySet()) {
			HAPValueStructureDefinitionFlat flat = this.m_flatStructureByCategary.get(categary);
			out.m_flatStructureByCategary.put(categary, (HAPValueStructureDefinitionFlat)flat.cloneStructure());
		}
		out.m_categaryById.clear();
		out.m_categaryById.putAll(this.m_categaryById);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(GROUP, HAPJsonUtility.buildJson(this.m_flatStructureByCategary, HAPSerializationFormat.JSON));
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

	public void addPublicElement(HAPRootStructure root){  this.addRoot(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, root);  }
	public void addProtectedElement(HAPRootStructure root){  this.addRoot(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED, root);  }
	public void addInternalElement(HAPRootStructure root){  this.addRoot(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL, root);  }
	public void addPrivateElement(HAPRootStructure root){  this.addRoot(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE, root);  }

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
