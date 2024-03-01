package com.nosliw.data.core.valuestructure1;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.domain.valuecontext.HAPExecutableEntityValueContext;
import com.nosliw.data.core.domain.valuecontext.HAPInfoPartSimple;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityValueContext;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPDefinitionEntityValueContext;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPDefinitionWrapperValueStructure;
import com.nosliw.data.core.operand.HAPContainerVariableCriteriaInfo;
import com.nosliw.data.core.structure.HAPInfoAlias;
import com.nosliw.data.core.structure.HAPReferenceRootInStrucutre;

public class HAPUtilityValueStructure {

	

	
	public static HAPContainerVariableCriteriaInfo discoverDataVariablesInStructure(String valueStructureComplexId, HAPDomainValueStructure valueStructureDomain) {
		
		HAPDefinitionEntityValueStructure valueStructure = valueStructureDomain.getValueStructureDefInfoByRuntimeId(valueStructureComplexId).getValueStructure();
		
		
		HAPDefinitionEntityValueContext valueStructureComplex = valueStructureDomain.getValueContext(valueStructureComplexId);
		HAPContainerVariableCriteriaInfo out = new HAPContainerVariableCriteriaInfo();
		List<HAPInfoPartSimple> partsInfo = HAPUtilityValueContext.getAllSimpleParts(valueStructureComplex);
		for(HAPInfoPartSimple partInfo : partsInfo) {
			discoverDataVariablesInStructure(out, partInfo.getSimpleValueStructurePart().getRuntimeId(), partInfo.getSimpleValueStructurePart().getValueStructure());
		}
		return out;
	}

	public static void discoverDataVariablesInStructure(HAPContainerVariableCriteriaInfo varCriteriaInfoContainer, String sturctureId, HAPValueStructure structure) {
		Map<String, HAPInfoCriteria> dataVarsInfoByIdPath = discoverDataVariablesByIdInStructure(structure);
		for(String idPath : dataVarsInfoByIdPath.keySet()) {
			varCriteriaInfoContainer.addVariable(HAPUtilityNamingConversion.cascadeComponentPath(sturctureId, idPath), dataVarsInfoByIdPath.get(idPath));
		}
	}


	
	public static HAPValueStructure newValueStructure(String valueStructureType) {
		if(HAPConstantShared.STRUCTURE_TYPE_VALUEFLAT.equals(valueStructureType))  return new HAPValueStructureDefinitionFlat();
		if(HAPConstantShared.STRUCTURE_TYPE_VALUEGROUP.equals(valueStructureType))  return new HAPValueStructureDefinitionGroup();
		if(HAPConstantShared.STRUCTURE_TYPE_VALUEEMPTY.equals(valueStructureType))  return new HAPValueStructureDefinitionEmpty();
		return null;
	}
	
	public static HAPValueStructure getValueStructureFromWrapper(HAPDefinitionWrapperValueStructure wrapper) {
		if(wrapper==null)   return null;
		return wrapper.getValueStructure();
	}
	
	public static HAPValueStructureDefinitionFlat getFlateFromWrapper(HAPDefinitionWrapperValueStructure wrapper) {
		return (HAPValueStructureDefinitionFlat)getValueStructureFromWrapper(wrapper);
	}
	
	public static HAPValueStructureDefinitionGroup getGroupFromWrapper(HAPDefinitionWrapperValueStructure wrapper) {
		return (HAPValueStructureDefinitionGroup)getValueStructureFromWrapper(wrapper);
	}
	
	public static HAPRootStructure getRootFromGroupStructure(HAPValueStructureDefinitionGroup groupStructure, String categary, String name) {
		List<HAPRootStructure> roots = groupStructure.resolveRoot(new HAPReferenceRootInGroup(categary, name), false);
		if(roots==null || roots.size()==0)   return null;
		return roots.get(0);
	}
	
	public static HAPExecutableValueStructure buildExecuatableValueStructure(HAPValueStructure valueStructure) {
		if(valueStructure==null)   return null;
		HAPExecutableValueStructure out = new HAPExecutableValueStructure();
		for(HAPRootStructure root : valueStructure.getAllRoots()) {
			out.addRoot(root);
			String rootId = root.getLocalId();
			List<HAPInfoAlias> aliases = valueStructure.discoverRootAliasById(rootId);
			for(HAPInfoAlias alias : aliases) {
				out.addNameMapping(alias.getName(), rootId);
			}
		}
		return out;
	}
	
	public static Map<String, Object> replaceValueNameWithId(HAPDefinitionEntityValueContext valueStructureComplex, Map<String, Object> values){

	}
	
	public static Map<String, Object> replaceValueNameWithId(HAPValueStructure valueStructure, Map<String, Object> values){
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		for(String rootName : values.keySet()) {
			HAPReferenceRootInStrucutre rootReference = null;
			String structureType = valueStructure.getStructureType();
			if(structureType.equals(HAPConstantShared.STRUCTURE_TYPE_VALUEGROUP)) {
				rootReference = new HAPReferenceRootInGroup(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, rootName);
			}
			else if(structureType.equals(HAPConstantShared.STRUCTURE_TYPE_VALUEFLAT)) {
				rootReference = new HAPReferenceRootInFlat(rootName);
			}
			List<HAPRootStructure> roots = valueStructure.resolveRoot(rootReference, false);
			if(roots!=null && roots.size()>0) {
				out.put(roots.get(0).getLocalId(), values.get(rootName));
			}
		}
		return out;
	}
	
	public static HAPValueStructure hardMerge(HAPValueStructure child, HAPValueStructure parent) {
		if(child==null) return (HAPValueStructure)parent.cloneStructure();
		if(parent==null)  return (HAPValueStructure)child.cloneStructure();
		
		String type1 = child.getStructureType();
		String type2 = parent.getStructureType();
		if(!type1.equals(type2))  throw new RuntimeException();
		
		HAPValueStructure out = null;
		out = (HAPValueStructure)child.cloneStructure();
		out.hardMergeWith(parent);
		return out;
	}

	public static HAPVariableInfoInStructure discoverDataVariablesDefinitionInStructure(HAPExecutableEntityValueContext valueStructureComplex, HAPDomainValueStructure valueStructureDomain) {
		HAPVariableInfoInStructure out = new HAPVariableInfoInStructure();
		
		
		List<HAPInfoPartSimple> partsInfo = HAPUtilityValueContext.getAllSimpleParts(valueStructureComplex);
		for(HAPInfoPartSimple partInfo : partsInfo) {
			discoverDataVariablesDefinitionInStructure(out, partInfo.getSimpleValueStructurePart().getRuntimeId(), partInfo.getSimpleValueStructurePart().getValueStructure());
		}
		return out;
	}
	
	public static void discoverDataVariablesDefinitionInStructure(HAPVariableInfoInStructure varInfoInStructure, String sturctureId, HAPValueStructure structure) {
		Map<String, HAPInfoCriteria> dataVarsInfoByIdPath = discoverDataVariablesByIdInStructure(structure);
		for(String idPath : dataVarsInfoByIdPath.keySet()) {
			HAPComplexPath path = new HAPComplexPath(idPath);
			String id = path.getRoot();
			List<HAPInfoAlias> aliases = structure.discoverRootAliasById(id);
			varInfoInStructure.addVariableCriteriaInfo(dataVarsInfoByIdPath.get(idPath), HAPUtilityNamingConversion.cascadeComponentPath(sturctureId, idPath), aliases);
		}
	}
	
	
	public static String getContextGroupInheritMode(HAPInfo info) {  
		String out = HAPConstant.INHERITMODE_CHILD;
		if("false".equals(info.getValue(HAPValueStructureDefinitionGroup.INFO_INHERIT)))  out = HAPConstant.INHERITMODE_NONE;
		return out;				
	}
 
	public static void setContextGroupInheritModeNone(HAPInfo info) {		info.setValue(HAPValueStructureDefinitionGroup.INFO_INHERIT, "false");	}
	public static void setContextGroupInheritModeChild(HAPInfo info) {		info.setValue(HAPValueStructureDefinitionGroup.INFO_INHERIT, "true");	}
	
	public static boolean getContextGroupPopupMode(HAPInfo info) {  
		boolean out = true;
		if("false".equals(info.getValue(HAPValueStructureDefinitionGroup.INFO_POPUP)))  out = false;
		return out;				
	} 
 
	public static boolean getContextGroupEscalateMode(HAPInfo info) {  
		boolean out = false;
		if("true".equals(info.getValue(HAPValueStructureDefinitionGroup.INFO_ESCALATE)))  out = true;
		return out;				
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
