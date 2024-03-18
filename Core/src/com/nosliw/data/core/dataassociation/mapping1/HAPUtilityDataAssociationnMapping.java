package com.nosliw.data.core.dataassociation.mapping1;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelative;
import com.nosliw.core.application.common.structure.HAPInfoElement;
import com.nosliw.core.application.common.structure.HAPInfoVariable;
import com.nosliw.core.application.common.structure.HAPProcessorStructureElement;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.valuestructure1.HAPVariableInfoInStructure;

public class HAPUtilityDataAssociationnMapping {

	public static HAPDefinitionDataAssociationMapping reverseMapping(HAPDefinitionDataAssociationMapping mappinigDataAssociation) {
		HAPDefinitionDataAssociationMapping out = new HAPDefinitionDataAssociationMapping();
		Map<String, HAPValueMapping> mappings = mappinigDataAssociation.getMappings();
		for(String targetName : mappings.keySet()) {
			HAPValueMapping mapping = mappings.get(targetName);
			Map<String, HAPRootStructure> items = mapping.getItems();
			for(String rootName : items.keySet()) {
				HAPUtilityStructure.traverseElement(items.get(rootName), rootName, new HAPProcessorStructureElement() {

					@Override
					public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
						if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
							HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)eleInfo.getElement();
							String parent = relativeEle.getParentValueContextName();
							
							HAPElementStructureLeafRelative reverseEle = new HAPElementStructureLeafRelative(eleInfo.getElementPath().getFullName());
							reverseEle.setParentValueContextName(targetName);
							HAPValueMapping reverseValueMapping = out.getMapping(parent, true);
							reverseValueMapping.addMapping(eleInfo.getElementPath().getFullName(), reverseEle);
						}
						return null;
					}

					@Override
					public void postProcess(HAPInfoElement eleInfo, Object value) {}
				}, null);
			}
			
		}
		return out;
	}
	
	
	
	//automatic enhance mapping so that all the variables are mapped as target
	public static HAPValueMapping expandMappingByTargetVariable(HAPValueMapping mapping, HAPVariableInfoInStructure targetVarsContainer) {
		
		HAPValueMapping out = new HAPValueMapping();
		
		Map<String, HAPRootStructure> outMappingRoots = new LinkedHashMap<String, HAPRootStructure>();   //all roots for output mapping
		for(HAPInfoVariable varInfo : targetVarsContainer.getAllVariables()) {
			//find root in mapping for target variable first
			String varSubPath = varInfo.getSubPath().getPath();
			Set<String> rootAliases = varInfo.getRootAliases();
			HAPRootStructure mappingRoot = null;
			String mappingRootName = null;
			for(String rootAlias : rootAliases) {
				List<HAPRootStructure> roots = HAPUtilityStructure.resolveRoot(rootAlias, mapping, false);
				if(roots!=null && roots.size()>=1) {
					mappingRoot = roots.get(0);
					mappingRootName = rootAlias;
					break;
				}
			}
			
			if(mappingRoot==null) {
				//if no root in mapping, create root name for new root
				mappingRootName = rootAliases.iterator().next();
			}
			
			HAPRootStructure newMappingRoot = outMappingRoots.get(mappingRootName);
			if(newMappingRoot==null) {
				//if no new mapping root created, then create new mapping root
				if(mappingRoot!=null)  newMappingRoot = mappingRoot.cloneExceptElement();
				else newMappingRoot = new HAPRootStructure();
				outMappingRoots.put(mappingRootName, newMappingRoot);
			}

			boolean found = false;
			Map<String, HAPElementStructureLeafRelative> relativeElesByIdPath = HAPUtilityStructure.discoverRelativeElement(mappingRoot);  //all relative element in mapping root
			for(String mappingRelativeIdPath : relativeElesByIdPath.keySet()) {
				HAPElementStructureLeafRelative relativeEle = relativeElesByIdPath.get(mappingRelativeIdPath);
				String mappingRelativeSubPath = new HAPComplexPath(mappingRelativeIdPath).getPath().getPath();
				if(mappingRelativeSubPath.equals(varSubPath)) {
					//exactly mapped, add the element to new mapping root
					HAPUtilityStructure.setDescendant(newMappingRoot, varInfo.getSubPath(), relativeEle.cloneStructureElement());
					found = true;
					break;
				}
				else if(varSubPath.startsWith(mappingRelativeSubPath)) {
					//match only part of variable path, extend relative path to full path
					String relativePath = HAPUtilityNamingConversion.cascadePath(relativeEle.getPath(), varSubPath.substring(mappingRelativeSubPath.length()));
					HAPUtilityStructure.setDescendant(newMappingRoot, varInfo.getSubPath(), new HAPElementStructureLeafRelative(relativePath));
					found = true;
					break;
				}
			}
			
			if(found==false) {
				//if variable not mapped at all, create new relative element
				HAPUtilityStructure.setDescendant(newMappingRoot, varInfo.getSubPath(), new HAPElementStructureLeafRelative(new HAPComplexPath(mappingRootName, varSubPath).getFullName()));
			}
		}
		
		//put all roots to new mapping 
		for(String rootName : outMappingRoots.keySet()) {
			HAPUtilityStructure.addRoot(out, rootName, outMappingRoots.get(rootName));
		}
		
		return out;
	}
	
	
}
