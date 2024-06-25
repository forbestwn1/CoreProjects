package com.nosliw.core.application;

import java.util.List;

import com.nosliw.common.path.HAPPath;

public class HAPUtilityBrickTraverse {

	//traverse only leaves that is local complex entity
	public static void traverseTreeWithLocalBrickComplex(HAPWrapperBrickRoot rootBrickWrapper, HAPHandlerDownward processor, HAPManagerApplicationBrick brickMan, Object data) {
		traverseTreeWithLocalBrick(
				rootBrickWrapper, 
			new HAPHandlerBrickWrapper(processor, true) {
				@Override
				protected boolean isValidAttribute(HAPAttributeInBrick attr) {
					HAPWrapperValue attrValueInfo = attr.getValueWrapper();
					return HAPUtilityBrick.isBrickComplex(((HAPWithBrick)attrValueInfo).getBrick().getBrickType(), brickMan);
				}
			}, 
			brickMan,
			data);
	}
	
	
	//traverse only local brick
	public static void traverseTreeWithLocalBrick(HAPWrapperBrickRoot rootBrickWrapper, HAPHandlerDownward processor, HAPManagerApplicationBrick brickMan, Object data) {
		traverseTree(
			rootBrickWrapper, 
			new HAPHandlerBrickWrapper(processor) {
				@Override
				protected boolean isValidAttribute(HAPAttributeInBrick attr) {
					if(attr.getValueWrapper() instanceof HAPWithBrick) {
						return true;
					}
					return false;
				}
			}, 
			brickMan,
			data);
	}
	
	//traverse all leave (complex, simiple, solid, not solid ...)
	public static void traverseTree(HAPWrapperBrickRoot rootBrickWrapper, HAPHandlerDownward processor, HAPManagerApplicationBrick brickMan, Object data) {
		traverseTree(rootBrickWrapper, null, processor, brickMan, data);
	}
	
	private static void traverseTree(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path, HAPHandlerDownward processor, HAPManagerApplicationBrick brickMan, Object data) {
		if(path==null) {
			path = new HAPPath();
		}
		
		if(processor.processBrickNode(rootBrickWrapper, path, data)) {
			HAPBrick leafBrick = HAPUtilityBrick.getDescdentBrickLocal(rootBrickWrapper, path);
			
			if(leafBrick!=null) {
				//only process child for brick
				List<HAPAttributeInBrick> attrsExe = leafBrick.getAttributes();
				for(HAPAttributeInBrick attrExe : attrsExe) {
					HAPPath attrPath = path.appendSegment(attrExe.getName());
					traverseTree(rootBrickWrapper, attrPath, processor, brickMan, data);
				}
			}
		}
		
		processor.postProcessBrickNode(rootBrickWrapper, path, data);
	}
}

abstract class HAPHandlerBrickWrapper extends HAPHandlerDownward{

	private HAPHandlerDownward m_processor;
	
	private boolean m_continueIfNotValidAttribute = false;
	
	public HAPHandlerBrickWrapper(HAPHandlerDownward processor) {
		this(processor, false);
	}

	public HAPHandlerBrickWrapper(HAPHandlerDownward processor, boolean continueIfNotValidAttribute) {
		this.m_processor = processor;
		this.m_continueIfNotValidAttribute = continueIfNotValidAttribute;
	}

	abstract protected boolean isValidAttribute(HAPAttributeInBrick attr);
	
	@Override
	public boolean processBrickNode(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			return this.m_processor.processBrickNode(rootBrickWrapper, path, data);
		}
		else {
			HAPAttributeInBrick attr = HAPUtilityBrick.getDescendantAttribute(rootBrickWrapper.getBrick(), path); 
			if(this.isValidAttribute(attr)) {
				return this.m_processor.processBrickNode(rootBrickWrapper, path, data);
			}
			return m_continueIfNotValidAttribute;
		}
	}

	@Override
	public void postProcessBrickNode(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			this.m_processor.postProcessBrickNode(rootBrickWrapper, path, data);
		}
		else {
			HAPAttributeInBrick attr = HAPUtilityBrick.getDescendantAttribute(rootBrickWrapper.getBrick(), path);
			if(this.isValidAttribute(attr)) {
				this.m_processor.postProcessBrickNode(rootBrickWrapper, path, data);
			}
		}
	}
}

//public static void trasversExecutableEntityTreeUpward(HAPBrick entity, HAPProcessorEntityExecutableUpward processor, Object object) {
//HAPPath path = new HAPPath();
//boolean result =  processor.process(entity, null, object);
//while(result) {
//	HAPExecutableEntity parent = entity.getParent();
//	if(parent==null) {
//		break;
//	} else {
//		result = processor.process(parent, path.appendSegment(HAPConstantShared.NAME_PARENT), processContext, object);
//	}
//}
//}

//traverse only entity leaves that marked as auto process
//public static void traverseExecutableTreeAutoProcessed(HAPWrapperBrickRoot rootEntityInfo, HAPHandlerDownward processor, HAPManualContextProcess processContext) {
//traverseExecutableEntity(
//		rootEntityInfo, 
//	new HAPHandlerBrickWrapper(processor) {
//		@Override
//		protected boolean isValidAttribute(HAPAttributeInBrick attr) {
//			HAPManualAttribute attrDef = (HAPManualAttribute)HAPUtilityDefinitionBrick.getDefTreeNodeFromExeTreeNode(attr, processContext.getCurrentBundle());
//			
//			HAPUtilityDefinitionBrick.isAttributeAutoProcess(attrDef, null)
//			
//			if(attr.isAttributeAutoProcess()) {
//				return true;
//			}
//			return false;
//		}
//	}, 
//	processContext);
//}

