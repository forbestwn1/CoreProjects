package com.nosliw.core.application.division.manual;

import java.util.List;

import com.nosliw.common.path.HAPPath;

public class HAPManualUtilityDefinitionBrickTraverse {

	
	public static void traverseEntityTreeLeaves(HAPManualWrapperBrick rootBrickWrapper, HAPManualProcessorBrickNodeDownwardWithPath processor, Object data) {
		traverseEntityTreeLeaves(rootBrickWrapper, null, processor, data);
	}
	
	private static void traverseEntityTreeLeaves(HAPManualWrapperBrick rootBrickWrapper, HAPPath path, HAPManualProcessorBrickNodeDownwardWithPath processor, Object data) {
		if(path==null) {
			path = new HAPPath();
		}

		if(processor.processBrickNode(rootBrickWrapper, path, data)) {
			HAPManualBrick leafBrick = HAPManualUtilityBrick.getDescdentBrickDefinition(rootBrickWrapper, path);
			
			if(leafBrick!=null) {
				//if related value is not brick, then stop processing children attribute
				List<HAPManualAttribute> attrs = leafBrick.getAllAttributes();
				for(HAPManualAttribute attr : attrs) {
					HAPPath attrPath = path.appendSegment(attr.getName());
					traverseEntityTreeLeaves(rootBrickWrapper, attrPath, processor, data);
				}
			}
		}
		processor.postProcessBrickNode(rootBrickWrapper, path, data);
	}
}

abstract class HAPProcessorEntityWrapper extends HAPManualProcessorBrickNodeDownwardWithPath{

	private HAPManualProcessorBrickNodeDownwardWithPath m_processor;
	
	public HAPProcessorEntityWrapper(HAPManualProcessorBrickNodeDownwardWithPath processor) {
		this.m_processor = processor;
	}
	
	abstract protected boolean isValidAttribute(HAPManualAttribute attr);
	
	@Override
	public boolean processBrickNode(HAPManualWrapperBrick rootBrickWrapper, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			return this.m_processor.processBrickNode(rootBrickWrapper, path, data);
		}
		else {
			HAPManualAttribute attr = HAPManualUtilityBrick.getDescendantAttribute(rootBrickWrapper.getBrick(), path); 
			if(this.isValidAttribute(attr)) {
				return this.m_processor.processBrickNode(rootBrickWrapper, path, data);
			}
			return false;
		}
	}

	@Override
	public void postProcessBrickNode(HAPManualWrapperBrick rootBrickWrapper, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			this.m_processor.postProcessBrickNode(rootBrickWrapper, path, data);
		}
		else {
			HAPManualAttribute attr = HAPManualUtilityBrick.getDescendantAttribute(rootBrickWrapper.getBrick(), path); 
			if(this.isValidAttribute(attr)) {
				this.m_processor.postProcessBrickNode(rootBrickWrapper, path, data);
			}
		}
	}
}
