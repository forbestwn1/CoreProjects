package com.nosliw.core.application;

import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.entity.division.manual.HAPContextProcess;
import com.nosliw.data.core.entity.division.manual.HAPManualAttribute;
import com.nosliw.data.core.entity.division.manual.HAPUtilityDefinitionEntity;

public class HAPUtilityEntityExecutableTraverse {

	public static void trasversExecutableEntityTreeUpward(HAPBrick entity, HAPProcessorEntityExecutableUpward processor, Object object) {
		HAPPath path = new HAPPath();
		boolean result =  processor.process(entity, null, object);
		while(result) {
			HAPExecutableEntity parent = entity.getParent();
			if(parent==null) {
				break;
			} else {
				result = processor.process(parent, path.appendSegment(HAPConstantShared.NAME_PARENT), processContext, object);
			}
		}
	}
	
	//traverse only entity leaves that marked as auto process
	public static void traverseExecutableTreeAutoProcessed(HAPWrapperBrick rootEntityInfo, HAPHandlerDownward processor, HAPContextProcess processContext) {
		traverseExecutableEntity(
				rootEntityInfo, 
			new HAPProcessorEntityExecutableWrapper(processor) {
				@Override
				protected boolean isValidAttribute(HAPAttributeInBrick attr) {
					HAPManualAttribute attrDef = (HAPManualAttribute)HAPUtilityDefinitionEntity.getDefTreeNodeFromExeTreeNode(attr, processContext.getCurrentBundle());
					
					HAPUtilityDefinitionEntity.isAttributeAutoProcess(attrDef, null)
					
					if(attr.isAttributeAutoProcess()) {
						return true;
					}
					return false;
				}
			}, 
			processContext);
	}
	
	//traverse only leaves that is local complex entity
	public static void traverseExecutableTreeLocalComplexEntity(HAPWrapperBrick rootEntityInfo, HAPHandlerDownward processor, HAPManagerApplicationBrick entityMan, Object data) {
		traverseExecutableTreeComplexEntity(
				rootEntityInfo, 
			new HAPProcessorEntityExecutableWrapper(processor) {
				@Override
				protected boolean isValidAttribute(HAPAttributeInBrick attr) {
					if(attr.getValueWrapper() instanceof HAPWithBrick) {
						return true;
					}
					return false;
				}
			}, 
			entityMan,
			data);
	}
	
	
	//traverse only leafs that is complex entity
	public static void traverseExecutableTreeComplexEntity(HAPWrapperBrick rootEntityInfo, HAPHandlerDownward processor, HAPManagerApplicationBrick entityMan, Object data) {
		traverseExecutableEntity(
			rootEntityInfo, 
			new HAPProcessorEntityExecutableWrapper(processor) {
				@Override
				protected boolean isValidAttribute(HAPAttributeInBrick attr) {
					HAPWrapperValueInAttribute attrValueInfo = attr.getValueWrapper();
					if(attrValueInfo instanceof HAPWithBrick) {
						return HAPUtilityEntity.isEntityComplex(((HAPWithBrick)attrValueInfo).getBrickTypeId(), entityMan);
					}
					return false;
				}
			}, 
			data);
	}
	
	//traverse only entity leaves that marked as auto process
	public static void traverseExecutableEntity(HAPWrapperBrick rootEntityInfo, HAPHandlerDownward processor, Object data) {
		traverseExecutableTree(
			rootEntityInfo, 
			new HAPProcessorEntityExecutableWrapper(processor) {
				@Override
				protected boolean isValidAttribute(HAPAttributeInBrick attr) {
					if(attr.getValueWrapper() instanceof HAPWithBrick) {
						return true;
					}
					return false;
				}
			}, 
			data);
	}
	
	//traverse all leave (complex, simiple, solid, not solid ...)
	public static void traverseExecutableTree(HAPWrapperBrick rootEntityInfo, HAPHandlerDownward processor, Object data) {
		traverseExecutableTreeLeaves(rootEntityInfo, null, processor, data);
	}
	
	private static void traverseExecutableTreeLeaves(HAPWrapperBrick rootEntityInfo, HAPPath path, HAPHandlerDownward processor, Object data) {
		if(path==null) {
			path = new HAPPath();
		}
		
		if(processor.processBrickNode(rootEntityInfo, path, data)) {
			HAPBrick leafEntity = HAPUtilityEntity.getDescdentEntity(rootEntityInfo, path);
			
			if(leafEntity!=null) {
				List<HAPAttributeInBrick> attrsExe = leafEntity.getAttributes();
				for(HAPAttributeInBrick attrExe : attrsExe) {
					HAPPath attrPath = path.appendSegment(attrExe.getName());
					traverseExecutableTreeLeaves(rootEntityInfo, attrPath, processor, data);
				}
			}
		}
		
		processor.postProcessBrickNode(rootEntityInfo, path, data);
	}
}

abstract class HAPProcessorEntityExecutableWrapper extends HAPHandlerDownward{

	private HAPHandlerDownward m_processor;
	
	public HAPProcessorEntityExecutableWrapper(HAPHandlerDownward processor) {
		this.m_processor = processor;
	}
	
	abstract protected boolean isValidAttribute(HAPAttributeInBrick attr);
	
	@Override
	public boolean processBrickNode(HAPWrapperBrick rootEntityInfo, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			return this.m_processor.processBrickNode(rootEntityInfo, path, data);
		}
		else {
			HAPAttributeInBrick attr = HAPUtilityEntity.getDescendantAttribute(rootEntityInfo.getBrick(), path); 
			if(this.isValidAttribute(attr)) {
				return this.m_processor.processBrickNode(rootEntityInfo, path, data);
			}
			return false;
		}
	}

	@Override
	public void postProcessBrickNode(HAPWrapperBrick rootEntityInfo, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			this.m_processor.postProcessBrickNode(rootEntityInfo, path, data);
		}
		else {
			HAPAttributeInBrick attr = HAPUtilityEntity.getDescendantAttribute(rootEntityInfo.getBrick(), path);
			if(this.isValidAttribute(attr)) {
				this.m_processor.postProcessBrickNode(rootEntityInfo, path, data);
			}
		}
	}
}
