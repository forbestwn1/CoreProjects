package com.nosliw.data.core.dataassociation.mapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.structure.HAPElementLeafRelative;
import com.nosliw.data.core.structure.HAPInfoVariable;
import com.nosliw.data.core.structure.HAPRoot;
import com.nosliw.data.core.structure.HAPStructure;
import com.nosliw.data.core.structure.HAPUtilityStructure;
import com.nosliw.data.core.valuestructure.HAPVariableInfoInStructure;

public class HAPUtilityStructureDataAssociation {

	public static HAPDefinitionDataAssociation replaceVarNameWithId(HAPDefinitionDataAssociation da, HAPStructure target, HAPStructure source) {
		
	}
	
	public static void mirroringVariableInStructure(HAPStructure sourceStructure) {
		
	}
	
	//automatic enhance mapping so that all the variables are mapped as target
	public static HAPMapping expandMappingByTargetVariable(HAPMapping mapping, HAPVariableInfoInStructure targetVarsContainer) {
		
		HAPMapping out = new HAPMapping();
		
		Map<String, HAPRoot> outMappingRoots = new LinkedHashMap<String, HAPRoot>();   //all roots for output mapping
		for(HAPInfoVariable varInfo : targetVarsContainer.getAllVariables()) {
			//find root in mapping for target variable first
			String varSubPath = varInfo.getSubPath().getPath();
			Set<String> rootAliases = varInfo.getRootAliases();
			HAPRoot mappingRoot = null;
			String mappingRootName = null;
			for(String rootAlias : rootAliases) {
				List<HAPRoot> roots = HAPUtilityStructure.resolveRoot(rootAlias, mapping, false);
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
			
			HAPRoot newMappingRoot = outMappingRoots.get(mappingRootName);
			if(newMappingRoot==null) {
				//if no new mapping root created, then create new mapping root
				if(mappingRoot!=null)  newMappingRoot = mappingRoot.cloneExceptElement();
				else newMappingRoot = new HAPRoot();
				outMappingRoots.put(mappingRootName, newMappingRoot);
			}

			boolean found = false;
			Map<String, HAPElementLeafRelative> relativeElesByIdPath = HAPUtilityStructure.discoverRelativeElement(mappingRoot);  //all relative element in mapping root
			for(String mappingRelativeIdPath : relativeElesByIdPath.keySet()) {
				HAPElementLeafRelative relativeEle = relativeElesByIdPath.get(mappingRelativeIdPath);
				String mappingRelativeSubPath = new HAPComplexPath(mappingRelativeIdPath).getPath().getPath();
				if(mappingRelativeSubPath.equals(varSubPath)) {
					//exactly mapped, add the element to new mapping root
					HAPUtilityStructure.setDescendant(newMappingRoot, varInfo.getSubPath(), relativeEle.cloneStructureElement());
					found = true;
					break;
				}
				else if(varSubPath.startsWith(mappingRelativeSubPath)) {
					//match only part of variable path, extend relative path to full path
					String relativePath = HAPNamingConversionUtility.cascadePath(relativeEle.getReferencePath(), varSubPath.substring(mappingRelativeSubPath.length()));
					HAPUtilityStructure.setDescendant(newMappingRoot, varInfo.getSubPath(), new HAPElementLeafRelative(relativePath));
					found = true;
					break;
				}
			}
			
			if(found==false) {
				//if variable not mapped at all, create new relative element
				HAPUtilityStructure.setDescendant(newMappingRoot, varInfo.getSubPath(), new HAPElementLeafRelative(new HAPComplexPath(mappingRootName, varSubPath).getFullName()));
			}
		}
		
		//put all roots to new mapping 
		for(String rootName : outMappingRoots.keySet()) {
			HAPUtilityStructure.addRoot(out, rootName, outMappingRoots.get(rootName));
		}
		
		return out;
	}
	
	
}
