package com.nosliw.data.core.entity;

import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.entity.division.manual.HAPContextProcess;
import com.nosliw.data.core.entity.division.manual.HAPManualAttribute;
import com.nosliw.data.core.entity.division.manual.HAPUtilityDefinitionEntity;

public class HAPUtilityEntityExecutableTraverse {

	public static void trasversExecutableEntityTreeUpward(HAPEntityExecutable entity, HAPProcessorEntityExecutableUpward processor, Object object) {
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
	public static void traverseExecutableTreeAutoProcessed(HAPInfoEntity rootEntityInfo, HAPProcessorEntityExecutableDownward processor, HAPContextProcess processContext) {
		traverseExecutableEntity(
				rootEntityInfo, 
			new HAPProcessorEntityExecutableWrapper(processor) {
				@Override
				protected boolean isValidAttribute(HAPAttributeExecutable attr) {
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
	public static void traverseExecutableTreeLocalComplexEntity(HAPInfoEntity rootEntityInfo, HAPProcessorEntityExecutableDownward processor, HAPManagerEntity entityMan, Object data) {
		traverseExecutableTreeComplexEntity(
				rootEntityInfo, 
			new HAPProcessorEntityExecutableWrapper(processor) {
				@Override
				protected boolean isValidAttribute(HAPAttributeExecutable attr) {
					if(attr.getValueInfo() instanceof HAPWithEntity) {
						return true;
					}
					return false;
				}
			}, 
			entityMan,
			data);
	}
	
	
	//traverse only leafs that is complex entity
	public static void traverseExecutableTreeComplexEntity(HAPInfoEntity rootEntityInfo, HAPProcessorEntityExecutableDownward processor, HAPManagerEntity entityMan, Object data) {
		traverseExecutableEntity(
			rootEntityInfo, 
			new HAPProcessorEntityExecutableWrapper(processor) {
				@Override
				protected boolean isValidAttribute(HAPAttributeExecutable attr) {
					HAPInfoAttributeValue attrValueInfo = attr.getValueInfo();
					if(attrValueInfo instanceof HAPWithEntity) {
						return HAPUtilityEntity.isEntityComplex(((HAPWithEntity)attrValueInfo).getEntityTypeId(), entityMan);
					}
					return false;
				}
			}, 
			data);
	}
	
	//traverse only entity leaves that marked as auto process
	public static void traverseExecutableEntity(HAPInfoEntity rootEntityInfo, HAPProcessorEntityExecutableDownward processor, Object data) {
		traverseExecutableTree(
			rootEntityInfo, 
			new HAPProcessorEntityExecutableWrapper(processor) {
				@Override
				protected boolean isValidAttribute(HAPAttributeExecutable attr) {
					if(attr.getValueInfo() instanceof HAPWithEntity) {
						return true;
					}
					return false;
				}
			}, 
			data);
	}
	
	//traverse all leave (complex, simiple, solid, not solid ...)
	public static void traverseExecutableTree(HAPInfoEntity rootEntityInfo, HAPProcessorEntityExecutableDownward processor, Object data) {
		traverseExecutableTreeLeaves(rootEntityInfo, null, processor, data);
	}
	
	private static void traverseExecutableTreeLeaves(HAPInfoEntity rootEntityInfo, HAPPath path, HAPProcessorEntityExecutableDownward processor, Object data) {
		if(path==null) {
			path = new HAPPath();
		}
		
		if(processor.processEntityNode(rootEntityInfo, path, data)) {
			HAPEntityExecutable leafEntity = HAPUtilityEntity.getDescdentEntity(rootEntityInfo, path);
			
			if(leafEntity!=null) {
				List<HAPAttributeExecutable> attrsExe = leafEntity.getAttributes();
				for(HAPAttributeExecutable attrExe : attrsExe) {
					HAPPath attrPath = path.appendSegment(attrExe.getName());
					traverseExecutableTreeLeaves(rootEntityInfo, attrPath, processor, data);
				}
			}
		}
		
		processor.postProcessEntityNode(rootEntityInfo, path, data);
	}
}

abstract class HAPProcessorEntityExecutableWrapper extends HAPProcessorEntityExecutableDownward{

	private HAPProcessorEntityExecutableDownward m_processor;
	
	public HAPProcessorEntityExecutableWrapper(HAPProcessorEntityExecutableDownward processor) {
		this.m_processor = processor;
	}
	
	abstract protected boolean isValidAttribute(HAPAttributeExecutable attr);
	
	@Override
	public boolean processEntityNode(HAPInfoEntity rootEntityInfo, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			return this.m_processor.processEntityNode(rootEntityInfo, path, data);
		}
		else {
			HAPAttributeExecutable attr = HAPUtilityEntity.getDescendantAttribute(rootEntityInfo.getEntity(), path); 
			if(this.isValidAttribute(attr)) {
				return this.m_processor.processEntityNode(rootEntityInfo, path, data);
			}
			return false;
		}
	}

	@Override
	public void postProcessEntityNode(HAPInfoEntity rootEntityInfo, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			this.m_processor.postProcessEntityNode(rootEntityInfo, path, data);
		}
		else {
			HAPAttributeExecutable attr = HAPUtilityEntity.getDescendantAttribute(rootEntityInfo.getEntity(), path);
			if(this.isValidAttribute(attr)) {
				this.m_processor.postProcessEntityNode(rootEntityInfo, path, data);
			}
		}
	}
}
