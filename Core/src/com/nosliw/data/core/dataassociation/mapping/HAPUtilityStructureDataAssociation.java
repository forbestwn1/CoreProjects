package com.nosliw.data.core.dataassociation.mapping;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.structure.HAPElementLeafRelative;
import com.nosliw.data.core.structure.HAPInfoVariable;
import com.nosliw.data.core.structure.HAPRoot;
import com.nosliw.data.core.structure.HAPUtilityStructure;
import com.nosliw.data.core.valuestructure.HAPContainerVariableCriteriaInfo;

public class HAPUtilityStructureDataAssociation {

	public static HAPMapping expandMappingByTargetVariable(HAPMapping mapping, HAPContainerVariableCriteriaInfo targetVarsContainer) {
		
		HAPMapping out = new HAPMapping();
		
		
		
		Set<String> targetVarIdPaths = targetVarsContainer.getVariableCriteriaInfos().keySet();
		for(String targetVarIdPath : targetVarIdPaths) {
			HAPInfoVariable varInfo = targetVarsContainer.getVariableInfoById(targetVarIdPath);
			Set<String> rootAliases = varInfo.getRootAliases();
			HAPRoot mappingRoot = null;
			String mappingRootAlias = null;
			String mappingIdPath = null;
			for(String rootAlias : rootAliases) {
				List<HAPRoot> roots = HAPUtilityStructure.resolveRoot(rootAlias, mapping, false);
				if(roots!=null && roots.size()>=1) {
					mappingRoot = roots.get(0);
					mappingRootAlias = rootAlias;
					mappingIdPath = new HAPComplexPath(mappingRoot.getLocalId(), varInfo.getSubPath()).getFullName();
					break;
				}
			}
			
			if(mappingRoot==null) {
				mappingRootAlias = rootAliases.iterator().next();
			}
			

			HAPRoot newRoot = new HAPRoot();

//			mappingRoot = mapping.addRoot(new HAPReferenceRootInMapping(mappingRootAlias), mappingRoot);
			
			boolean found = false;
			Map<String, HAPElementLeafRelative> relativeElesByIdPath = HAPUtilityStructure.discoverRelativeElement(mappingRoot);
			for(String idPath : relativeElesByIdPath.keySet()) {
				HAPElementLeafRelative relativeEle = relativeElesByIdPath.get(idPath);
				if(idPath.equals(mappingIdPath)) {
					HAPUtilityStructure.setDescendant(newRoot, varInfo.getSubPath(), relativeEle.cloneStructureElement());
					found = true;
				}
				else if(mappingIdPath.startsWith(idPath)) {
					String relativePath = HAPNamingConversionUtility.cascadePath(mappingIdPath.substring(idPath.length()), relativeEle.getReferencePath());
					HAPUtilityStructure.setDescendant(newRoot, varInfo.getSubPath(), new HAPElementLeafRelative(relativePath));
					found = true;
				}
			}
			
			if(found==false) {
				HAPUtilityStructure.setDescendant(newRoot, varInfo.getSubPath(), new HAPElementLeafRelative(relativePath));
			}
		}
		
		return out;
	}
	
	
}
