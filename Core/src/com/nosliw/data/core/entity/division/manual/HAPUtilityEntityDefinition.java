package com.nosliw.data.core.entity.division.manual;

import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.entity.HAPAttributeExecutable;
import com.nosliw.data.core.entity.HAPIdEntityType;
import com.nosliw.data.core.entity.HAPInfoAttributeValue;

public class HAPUtilityEntityDefinition {

	public static boolean isAttributeAutoProcess(HAPManualAttribute attr) {
		
	}
	
	public static HAPIdEntityType parseEntityTypeId(Object obj) {
		
	}
	
	private static void traverseExecutableTreeLeaves(HAPManualInfoEntity rootEntityInfo, HAPPath path, HAPManualProcessorEntityDownward processor, Object data) {
		if(path==null) {
			path = new HAPPath();
		}
		if(processor.processEntityNode(rootEntityInfo, path, data)) {
			HAPManualEntity rootEntity = rootEntityInfo.getEntity();
			HAPManualEntity leafEntity = null;
			if(path.isEmpty()) {
				leafEntity = rootEntity;
			} else {
				leafEntity = rootEntity.getDescendantEntity(path);
			}
			
			List<HAPAttributeExecutable> attrsExe = leafEntity.getAttributes();
			for(HAPAttributeExecutable attrExe : attrsExe) {
				HAPPath attrPath = path.appendSegment(attrExe.getName());
				
				HAPInfoAttributeValue attrValueInfo = attrExe.getValueInfo();
				String attrValueType = attrValueInfo.getValueType();
				if(attrValueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_ENTITY)) {
					traverseExecutableTreeLeaves(rootEntityInfo, attrPath, processor, data);
				}
			}
		}
		processor.postProcessEntityNode(rootEntityInfo, path, data);
	}
}
