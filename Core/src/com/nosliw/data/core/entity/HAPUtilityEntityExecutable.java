package com.nosliw.data.core.entity;

import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.HAPAttributeEntityExecutable;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.entity.division.manual.HAPContextProcess;

public class HAPUtilityEntityExecutable {

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
	public static void traverseExecutableTreeAutoProcessed(HAPEntityExecutable rootEntity, HAPProcessorEntityExecutableDownward processor, HAPContextProcess processContext) {
		traverseExecutableTree(
			rootEntity, 
			new HAPProcessorEntityExecutableWrapper(processor) {
				@Override
				protected boolean isValidAttribute(HAPAttributeEntityExecutable attr) {
					if(attr.isAttributeAutoProcess()) {
						return true;
					}
					return false;
				}
			}, 
			processContext);
	}
	
	//traverse only leaves that is local complex entity
	public static void traverseExecutableTreeLocalComplexEntity(HAPInfoEntity rootEntityInfo, HAPProcessorEntityExecutableDownward processor, Object data) {
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
			data);
	}
	
	
	//traverse only leafs that is complex entity
	public static void traverseExecutableTreeComplexEntity(HAPInfoEntity rootEntityInfo, HAPProcessorEntityExecutableDownward processor, Object data) {
		traverseExecutableEntity(
			rootEntityInfo, 
			new HAPProcessorEntityExecutableWrapper(processor) {
				@Override
				protected boolean isValidAttribute(HAPAttributeExecutable attr) {
					return attr.getValueInfo().getEntityTypeInfo().getIsComplex();
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
			HAPEntityExecutable rootEntity = rootEntityInfo.getEntity();
			HAPEntityExecutable leafEntity = null;
			
			if(path.isEmpty()) {
				leafEntity = rootEntity;
			} else {
				HAPAttributeExecutable leafAttr = rootEntity.getDescendantAttribute(path);
				if(leafAttr.getValueInfo() instanceof HAPWithEntity) {
					leafEntity = ((HAPWithEntity)leafAttr.getValueInfo()).getEntity();
				}
			}
			
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
			HAPAttributeExecutable attr = rootEntityInfo.getEntity().getDescendantAttribute(path);
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
			HAPAttributeExecutable attr = rootEntityInfo.getEntity().getDescendantAttribute(path);
			if(this.isValidAttribute(attr)) {
				this.m_processor.postProcessEntityNode(rootEntityInfo, path, data);
			}
		}
	}
}
