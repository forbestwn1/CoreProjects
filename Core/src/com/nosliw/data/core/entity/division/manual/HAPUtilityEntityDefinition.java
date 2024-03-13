package com.nosliw.data.core.entity.division.manual;

import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.entity.HAPIdEntityType;
import com.nosliw.data.core.entity.HAPManagerEntity;
import com.nosliw.data.core.entity.HAPUtilityEntity;

public class HAPUtilityEntityDefinition {

	public static boolean isAttributeAutoProcess(HAPManualAttribute attr, HAPManagerEntity entityMan) {
		HAPManualEntityRelationAutoProcess relation = (HAPManualEntityRelationAutoProcess)getEntityRelation(attr, HAPConstantShared.MANUAL_RELATION_TYPE_AUTOPROCESS);
		if(relation!=null) {
			return relation.isAutoProcess();
		}
		
		HAPManualInfoAttributeValue attrValueInfo = attr.getValueInfo();
		if(attrValueInfo instanceof HAPManualWithEntity) {
			boolean isComplex = HAPUtilityEntity.isEntityComplex(((HAPManualWithEntity)attrValueInfo).getEntityTypeId(), entityMan); 
			
			if(isComplex) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
		
	}
	
	public static HAPIdEntityType parseEntityTypeId(Object obj) {
		
	}
	
	public static HAPManualEntityRelation getEntityRelation(HAPManualAttribute attr, String relationType) {
		for(HAPManualEntityRelation relation : attr.getRelations()) {
			if(relation.getType().equals(relationType)) {
				return relation;
			}
		}
		return null;
	}
	
	
	public static void traverseEntityTreeLeaves(HAPManualInfoEntity rootEntityInfo, HAPManualProcessorEntityDownward processor, Object data) {
		traverseEntityTreeLeaves(rootEntityInfo, null, processor, data);
	}
	
	private static void traverseEntityTreeLeaves(HAPManualInfoEntity rootEntityInfo, HAPPath path, HAPManualProcessorEntityDownward processor, Object data) {
		if(path==null) {
			path = new HAPPath();
		}

		if(processor.processEntityNode(rootEntityInfo, path, data)) {
			HAPManualEntity rootEntity = rootEntityInfo.getEntity();
			HAPManualEntity leafEntity = null;
			if(path.isEmpty()) {
				leafEntity = rootEntity;
			} else {
				HAPManualAttribute attr = rootEntity.getDescendantAttribute(path);
				if(attr.getValueInfo() instanceof HAPManualWithEntity) {
					leafEntity = ((HAPManualWithEntity)attr.getValueInfo()).getEntity();
				}
			}
			
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
