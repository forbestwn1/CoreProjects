package com.nosliw.core.application;

import java.util.List;

import com.nosliw.common.path.HAPPath;

public class HAPUtilityBrickTraverse {

	public static void traverseTreeWithLocalBrick(HAPBundle bundle, String rootName, HAPHandlerDownward processor, HAPManagerApplicationBrick brickMan, Object data) {
		traverseTree(
			bundle, 
			rootName,
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
	
	public static void traverseTree(HAPBundle bundle, String rootName, HAPHandlerDownward processor, HAPManagerApplicationBrick brickMan, Object data) {
		traverseTree(bundle, new HAPPath(HAPUtilityBundle.buildBranchPathSegment(rootName)), processor, brickMan, data);
	}
	
	private static void traverseTree(HAPBundle bundle, HAPPath path, HAPHandlerDownward processor, HAPManagerApplicationBrick brickMan, Object data) {
		if(processor.processBrickNode(bundle, path, data)) {
			HAPBrick leafBrick = HAPUtilityBrick.getDescdentBrickLocal(bundle, path);
			
			if(leafBrick!=null) {
				//only process child for brick
				List<HAPAttributeInBrick> attrsExe = leafBrick.getAttributes();
				for(HAPAttributeInBrick attrExe : attrsExe) {
					HAPPath attrPath = path.appendSegment(attrExe.getName());
					traverseTree(bundle, attrPath, processor, brickMan, data);
				}
			}
		}
		
		processor.postProcessBrickNode(bundle, path, data);
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

