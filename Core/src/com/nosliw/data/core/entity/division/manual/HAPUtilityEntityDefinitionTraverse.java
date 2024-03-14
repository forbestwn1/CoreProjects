package com.nosliw.data.core.entity.division.manual;

import java.util.List;

import com.nosliw.common.path.HAPPath;

public class HAPUtilityEntityDefinitionTraverse {

	
	public static void traverseEntityTreeLeaves(HAPManualInfoEntity rootEntityInfo, HAPManualProcessorEntityDownward processor, Object data) {
		traverseEntityTreeLeaves(rootEntityInfo, null, processor, data);
	}
	
	private static void traverseEntityTreeLeaves(HAPManualInfoEntity rootEntityInfo, HAPPath path, HAPManualProcessorEntityDownward processor, Object data) {
		if(path==null) {
			path = new HAPPath();
		}

		if(processor.processEntityNode(rootEntityInfo, path, data)) {
			HAPManualEntity leafEntity = HAPUtilityDefinitionEntity.getDescdentEntityDefinition(rootEntityInfo, path);
			
			if(leafEntity!=null) {
				List<HAPManualAttribute> attrs = leafEntity.getAllAttributes();
				for(HAPManualAttribute attr : attrs) {
					HAPPath attrPath = path.appendSegment(attr.getName());
					traverseEntityTreeLeaves(rootEntityInfo, attrPath, processor, data);
				}
			}
		}
		processor.postProcessEntityNode(rootEntityInfo, path, data);
	}
}

abstract class HAPProcessorEntityWrapper extends HAPManualProcessorEntityDownward{

	private HAPManualProcessorEntityDownward m_processor;
	
	public HAPProcessorEntityWrapper(HAPManualProcessorEntityDownward processor) {
		this.m_processor = processor;
	}
	
	abstract protected boolean isValidAttribute(HAPManualAttribute attr);
	
	@Override
	public boolean processEntityNode(HAPManualInfoEntity rootEntityInfo, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			return this.m_processor.processEntityNode(rootEntityInfo, path, data);
		}
		else {
			HAPManualAttribute attr = rootEntityInfo.getEntity().getDescendantAttribute(path);
			if(this.isValidAttribute(attr)) {
				return this.m_processor.processEntityNode(rootEntityInfo, path, data);
			}
			return false;
		}
	}

	@Override
	public void postProcessEntityNode(HAPManualInfoEntity rootEntityInfo, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			this.m_processor.postProcessEntityNode(rootEntityInfo, path, data);
		}
		else {
			HAPManualAttribute attr = rootEntityInfo.getEntity().getDescendantAttribute(path);
			if(this.isValidAttribute(attr)) {
				this.m_processor.postProcessEntityNode(rootEntityInfo, path, data);
			}
		}
	}
}
