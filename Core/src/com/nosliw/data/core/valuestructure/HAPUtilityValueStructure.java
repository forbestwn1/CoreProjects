package com.nosliw.data.core.valuestructure;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.operand.HAPContainerVariableCriteriaInfo;
import com.nosliw.data.core.structure.HAPElement;
import com.nosliw.data.core.structure.HAPElementLeafData;
import com.nosliw.data.core.structure.HAPElementLeafRelative;
import com.nosliw.data.core.structure.HAPElementNode;
import com.nosliw.data.core.structure.HAPInfoAlias;
import com.nosliw.data.core.structure.HAPReferenceRoot;
import com.nosliw.data.core.structure.HAPRoot;

public class HAPUtilityValueStructure {

	public static HAPExecutableValueStructure buildFlatContextFromContextStructure(HAPValueStructureDefinition valueStructure) {
		HAPExecutableValueStructure out = new HAPExecutableValueStructure();
		for(HAPRoot root : valueStructure.getAllRoots()) {
			out.addRoot(root);
		}
		return out;
	}
	
	public static Map<String, Object> replaceValueNameWithId(HAPValueStructure valueStructure, Map<String, Object> values){
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		for(String rootName : values.keySet()) {
			HAPReferenceRoot rootReference = null;
			String structureType = valueStructure.getStructureType();
			if(structureType.equals(HAPConstantShared.STRUCTURE_TYPE_VALUEGROUP)) {
				rootReference = new HAPReferenceRootInGroup(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, rootName);
			}
			else if(structureType.equals(HAPConstantShared.STRUCTURE_TYPE_VALUEFLAT)) {
				rootReference = new HAPReferenceRootInFlat(rootName);
			}
			List<HAPRoot> roots = valueStructure.resolveRoot(rootReference, false);
			if(roots!=null && roots.size()>0) {
				out.put(roots.get(0).getLocalId(), values.get(rootName));
			}
		}
		return out;
	}
	
	public static HAPValueStructureDefinition hardMerge(HAPValueStructureDefinition child, HAPValueStructureDefinition parent) {
		if(child==null) return (HAPValueStructureDefinition)parent.cloneStructure();
		if(parent==null)  return (HAPValueStructureDefinition)child.cloneStructure();
		
		String type1 = child.getStructureType();
		String type2 = parent.getStructureType();
		if(!type1.equals(type2))  throw new RuntimeException();
		
		HAPValueStructureDefinition out = null;
		out = (HAPValueStructureDefinition)child.cloneStructure();
		out.hardMergeWith(parent);
		return out;
	}

	public static HAPContainerVariableCriteriaInfo discoverDataVariablesInStructure(HAPValueStructureDefinition structure) {
		HAPContainerVariableCriteriaInfo out = new HAPContainerVariableCriteriaInfo();
		Map<String, HAPInfoCriteria> dataVarsInfoByIdPath = discoverDataVariablesByIdInStructure(structure);
		for(String idPath : dataVarsInfoByIdPath.keySet()) {
			out.addVariable(idPath, dataVarsInfoByIdPath.get(idPath));
		}
		return out;
	}

	public static HAPVariableInfoInStructure discoverDataVariablesDefinitionInStructure(HAPValueStructureDefinition structure) {
		HAPVariableInfoInStructure out = new HAPVariableInfoInStructure();
		Map<String, HAPInfoCriteria> dataVarsInfoByIdPath = discoverDataVariablesByIdInStructure(structure);
		for(String idPath : dataVarsInfoByIdPath.keySet()) {
			HAPComplexPath path = new HAPComplexPath(idPath);
			String id = path.getRootName();
			List<HAPInfoAlias> aliases = structure.discoverRootAliasById(id);
			out.addVariableCriteriaInfo(dataVarsInfoByIdPath.get(idPath), idPath, aliases);
		}
		return out;
	}
	
	
	//find all data variables in context 
	public static Map<String, HAPInfoCriteria> discoverDataVariablesByIdInStructure(HAPValueStructure structure){
		Map<String, HAPInfoCriteria> out = new LinkedHashMap<String, HAPInfoCriteria>();
		for(HAPRoot root : structure.getAllRoots()){
			if(!root.isConstant()){
				discoverDataVariableInElement(root.getLocalId(), root.getDefinition(), out);
			}
		}
		return out;
	}

	//discover data type criteria defined in context node
	//the purpose is to find variables related with data type criteria
	//the data type criteria name is full name in path, for instance, a.b.c.d
	private static void discoverDataVariableInElement(String path, HAPElement contextDefEle, Map<String, HAPInfoCriteria> criterias){
		switch(contextDefEle.getType()) {
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE:
			HAPElementLeafRelative relativeEle = (HAPElementLeafRelative)contextDefEle;
			if(relativeEle.getDefinition()!=null)		discoverDataVariableInElement(path, relativeEle.getSolidStructureElement(), criterias);
			break;
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA:
			HAPElementLeafData dataEle = (HAPElementLeafData)contextDefEle;
			HAPInfoCriteria varInfo = HAPInfoCriteria.buildCriteriaInfo(dataEle.getCriteria());
//			varInfo.setId(path);
			criterias.put(path, varInfo);
			break;
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
			HAPElementNode nodeEle = (HAPElementNode)contextDefEle;
			for(String childName : nodeEle.getChildren().keySet()) {
				String childPath = HAPNamingConversionUtility.cascadeComponentPath(path, childName);
				discoverDataVariableInElement(childPath, nodeEle.getChildren().get(childName), criterias);
			}
			break;
		}
	}

/*
	//build another context which only include variable node in current context
	public HAPValueStructureDefinitionFlat getVariableContext() {
		HAPValueStructureDefinitionFlat out = new HAPValueStructureDefinitionFlat();
		for(String name : this.m_roots.keySet()) {
			HAPRoot contextRoot = this.getRoot(name);
			if(!contextRoot.isConstant()) {
				out.addRoot(name, contextRoot.getId(), contextRoot.cloneRoot());
			}			
		}
		return out;
	}
	

	public static HAPValueStructureDefinitionFlat updateRootName(HAPValueStructureDefinitionFlat structure, HAPUpdateName nameUpdate) {
		HAPValueStructureDefinitionFlat out = new HAPValueStructureDefinitionFlat();
		
		for(String rootName : structure.getRootNames()) {
			HAPRoot root = structure.getRoot(rootName).cloneRoot();
			
			
			
		}
		
		//update context
		for(String rootName : new HashSet<String>(this.getRootNames())) {
			HAPRoot root = this.getRoot(rootName);
			root.setName(nameUpdate.getUpdatedName(root.getName()));
			HAPUtilityContext.processContextRootElement(root, rootName, new HAPProcessorContextDefinitionElement() {
				@Override
				public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object value) {
					if(eleInfo.getElement() instanceof HAPElementLeafRelative) {
						HAPElementLeafRelative relative = (HAPElementLeafRelative)eleInfo.getElement();
						if(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_SELF.equals(relative.getParent())) {
							//update local relative path
							HAPReferenceElement path = relative.getPathFormat();
							relative.setPath(new HAPReferenceElement(new HAPIdContextDefinitionRoot(path.getRootReference().getCategary(), nameUpdate.getUpdatedName(path.getRootReference().getName())), path.getSubPath()));
						}
					}
					return null;
				}

				@Override
				public void postProcess(HAPInfoElement ele, Object value) { }
			}, null);
			//update root name
			this.m_roots.remove(rootName);
			this.addRoot(nameUpdate.getUpdatedName(rootName), root);
		}
	}

	public void updateReferenceName(HAPUpdateName nameUpdate) {
		//update context
		for(String eleName : new HashSet<String>(this.getRootNames())) {
			HAPRoot root = this.getRoot(eleName);
			HAPUtilityContext.processContextRootElement(root, eleName, new HAPProcessorContextDefinitionElement() {
				@Override
				public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object value) {
					if(eleInfo.getElement() instanceof HAPElementLeafRelative) {
						HAPElementLeafRelative relative = (HAPElementLeafRelative)eleInfo.getElement();
						if(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT.equals(relative.getParent())) {
							//update local relative path
							HAPReferenceElement path = relative.getPathFormat();
							relative.setPath(new HAPReferenceElement(new HAPIdContextDefinitionRoot(path.getRootReference().getCategary(), nameUpdate.getUpdatedName(path.getRootReference().getName())), path.getSubPath()));
						}
					}
					return null;
				}

				@Override
				public void postProcess(HAPInfoElement ele, Object value) { }
			}, null);
		}
	}

	*/

}
