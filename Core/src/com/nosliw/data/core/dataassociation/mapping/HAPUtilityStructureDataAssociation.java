package com.nosliw.data.core.dataassociation.mapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.structure.HAPElementLeafRelative;
import com.nosliw.data.core.structure.HAPInfoVariable;
import com.nosliw.data.core.structure.HAPRoot;
import com.nosliw.data.core.structure.HAPUtilityStructure;
import com.nosliw.data.core.structure.HAPUtilityStructurePath;
import com.nosliw.data.core.valuestructure.HAPContainerVariableCriteriaInfo;

public class HAPUtilityStructureDataAssociation {

	//automatic enahnce mapping so that all the variables are mapped as target
	public static HAPMapping expandMappingByTargetVariable(HAPMapping mapping, HAPContainerVariableCriteriaInfo targetVarsContainer) {
		
		HAPMapping out = new HAPMapping();
		
		Map<String, HAPRoot> newRoots = new LinkedHashMap<String, HAPRoot>();
		for(HAPInfoVariable varInfo : targetVarsContainer.getAllVariables()) {
			//find target root in mapping first
			String varSubPath = varInfo.getSubPath().getPath();
			Set<String> rootAliases = varInfo.getRootAliases();
			HAPRoot mappingRoot = null;
			String mappingRootAlias = null;
			for(String rootAlias : rootAliases) {
				List<HAPRoot> roots = HAPUtilityStructure.resolveRoot(rootAlias, mapping, false);
				if(roots!=null && roots.size()>=1) {
					mappingRoot = roots.get(0);
					mappingRootAlias = rootAlias;
					break;
				}
			}
			
			if(mappingRoot==null) {
				//if no root in mapping, create root name for new root
				mappingRootAlias = rootAliases.iterator().next();
			}
			
			HAPRoot newRoot = newRoots.get(mappingRootAlias);
			if(newRoot==null) {
				if(mappingRoot!=null)  newRoot = mappingRoot.cloneRootBase();
				else newRoot = new HAPRoot();
				newRoots.put(mappingRootAlias, newRoot);
			}

			boolean found = false;
			Map<String, HAPElementLeafRelative> relativeElesByIdPath = HAPUtilityStructure.discoverRelativeElement(mappingRoot);
			for(String idPath : relativeElesByIdPath.keySet()) {
				HAPElementLeafRelative relativeEle = relativeElesByIdPath.get(idPath);
				if(idPath.equals(varSubPath)) {
					//exactly mapped
					HAPUtilityStructure.setDescendant(newRoot, varInfo.getSubPath(), relativeEle.cloneStructureElement());
					found = true;
					break;
				}
				else if(varSubPath.startsWith(idPath)) {
					//match only part of variable path
					String relativePath = HAPNamingConversionUtility.cascadePath(varSubPath.substring(idPath.length()), relativeEle.getReferencePath());
					HAPUtilityStructure.setDescendant(newRoot, varInfo.getSubPath(), new HAPElementLeafRelative(relativePath));
					found = true;
					break;
				}
			}
			
			if(found==false) {
				//if not mapped at all
				HAPUtilityStructure.setDescendant(newRoot, varInfo.getSubPath(), new HAPElementLeafRelative(new HAPComplexPath(mappingRootAlias, varSubPath).getFullName()));
			}
		}
		
		for(String rootName : newRoots.keySet()) {
			out.addRoot(HAPUtilityStructurePath.parseRootReferenceLiterate(rootName, out.getStructureType()), newRoots.get(rootName));
		}
		
		return out;
	}
	
	
}
