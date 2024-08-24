package com.nosliw.data.core.valuestructure1;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.common.structure.HAPInfoAlias;
import com.nosliw.core.application.common.structure.HAPReferenceRootInStrucutre;
import com.nosliw.core.application.common.structure.HAPRootStructure;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickValueContext;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickValueStructure;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickWrapperValueStructure;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.valuecontext.HAPExecutableEntityValueContext;
import com.nosliw.data.core.domain.valuecontext.HAPInfoPartSimple;
import com.nosliw.data.core.domain.valuecontext.HAPUtilityValueContext;

public class HAPUtilityValueStructure {

	

	
	public static HAPContainerVariableInfo discoverDataVariablesInStructure(String valueStructureComplexId, HAPDomainValueStructure valueStructureDomain) {
		
		HAPManualBrickValueStructure valueStructure = valueStructureDomain.getValueStructureDefInfoByRuntimeId(valueStructureComplexId).getValueStructure();
		
		
		HAPManualBrickValueContext valueStructureComplex = valueStructureDomain.getValueContext(valueStructureComplexId);
		HAPContainerVariableInfo out = new HAPContainerVariableInfo();
		List<HAPInfoPartSimple> partsInfo = HAPUtilityValueContext.getAllSimpleParts(valueStructureComplex);
		for(HAPInfoPartSimple partInfo : partsInfo) {
			discoverDataVariablesInStructure(out, partInfo.getSimpleValueStructurePart().getRuntimeId(), partInfo.getSimpleValueStructurePart().getValueStructureBlock());
		}
		return out;
	}

	public static void discoverDataVariablesInStructure(HAPContainerVariableInfo varCriteriaInfoContainer, String sturctureId, HAPValueStructureInValuePort11111 structure) {
		Map<String, HAPInfoCriteria> dataVarsInfoByIdPath = discoverDataVariablesByIdInStructure(structure);
		for(String idPath : dataVarsInfoByIdPath.keySet()) {
			varCriteriaInfoContainer.addVariable(HAPUtilityNamingConversion.cascadeComponentPath(sturctureId, idPath), dataVarsInfoByIdPath.get(idPath));
		}
	}


	
	public static HAPValueStructureInValuePort11111 newValueStructure(String valueStructureType) {
		if(HAPConstantShared.STRUCTURE_TYPE_VALUEFLAT.equals(valueStructureType))  return new HAPValueStructureDefinitionFlat();
		if(HAPConstantShared.STRUCTURE_TYPE_VALUEGROUP.equals(valueStructureType))  return new HAPValueStructureDefinitionGroup();
		if(HAPConstantShared.STRUCTURE_TYPE_VALUEEMPTY.equals(valueStructureType))  return new HAPValueStructureDefinitionEmpty();
		return null;
	}
	
	public static HAPValueStructureInValuePort11111 getValueStructureFromWrapper(HAPManualBrickWrapperValueStructure wrapper) {
		if(wrapper==null)   return null;
		return wrapper.getValueStructureBlock();
	}
	
	public static HAPValueStructureDefinitionFlat getFlateFromWrapper(HAPManualBrickWrapperValueStructure wrapper) {
		return (HAPValueStructureDefinitionFlat)getValueStructureFromWrapper(wrapper);
	}
	
	public static HAPValueStructureDefinitionGroup getGroupFromWrapper(HAPManualBrickWrapperValueStructure wrapper) {
		return (HAPValueStructureDefinitionGroup)getValueStructureFromWrapper(wrapper);
	}
	
	public static HAPRootStructure getRootFromGroupStructure(HAPValueStructureDefinitionGroup groupStructure, String categary, String name) {
		List<HAPRootStructure> roots = groupStructure.resolveRoot(new HAPReferenceRootInGroup(categary, name), false);
		if(roots==null || roots.size()==0)   return null;
		return roots.get(0);
	}
	
	public static HAPExecutableValueStructure buildExecuatableValueStructure(HAPValueStructureInValuePort11111 valueStructure) {
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
	
	public static Map<String, Object> replaceValueNameWithId(HAPManualBrickValueContext valueStructureComplex, Map<String, Object> values){

	}
	
	public static Map<String, Object> replaceValueNameWithId(HAPValueStructureInValuePort11111 valueStructure, Map<String, Object> values){
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
	
	public static HAPValueStructureInValuePort11111 hardMerge(HAPValueStructureInValuePort11111 child, HAPValueStructureInValuePort11111 parent) {
		if(child==null) return (HAPValueStructureInValuePort11111)parent.cloneStructure();
		if(parent==null)  return (HAPValueStructureInValuePort11111)child.cloneStructure();
		
		String type1 = child.getStructureType();
		String type2 = parent.getStructureType();
		if(!type1.equals(type2))  throw new RuntimeException();
		
		HAPValueStructureInValuePort11111 out = null;
		out = (HAPValueStructureInValuePort11111)child.cloneStructure();
		out.hardMergeWith(parent);
		return out;
	}

	public static HAPVariableInfoInStructure discoverDataVariablesDefinitionInStructure(HAPExecutableEntityValueContext valueStructureComplex, HAPDomainValueStructure valueStructureDomain) {
		HAPVariableInfoInStructure out = new HAPVariableInfoInStructure();
		
		
		List<HAPInfoPartSimple> partsInfo = HAPUtilityValueContext.getAllSimpleParts(valueStructureComplex);
		for(HAPInfoPartSimple partInfo : partsInfo) {
			discoverDataVariablesDefinitionInStructure(out, partInfo.getSimpleValueStructurePart().getRuntimeId(), partInfo.getSimpleValueStructurePart().getValueStructureBlock());
		}
		return out;
	}
	
	public static void discoverDataVariablesDefinitionInStructure(HAPVariableInfoInStructure varInfoInStructure, String sturctureId, HAPValueStructureInValuePort11111 structure) {
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
				public Pair<Boolean, HAPElement> process(HAPInfoElementResolve eleInfo, Object value) {
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
				public void postProcess(HAPInfoElementResolve ele, Object value) { }
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
				public Pair<Boolean, HAPElement> process(HAPInfoElementResolve eleInfo, Object value) {
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
				public void postProcess(HAPInfoElementResolve ele, Object value) { }
			}, null);
		}
	}

	*/

}
