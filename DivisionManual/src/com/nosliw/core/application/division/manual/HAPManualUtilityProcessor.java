package com.nosliw.core.application.division.manual;

import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPAdapter;
import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBrickAdapter;
import com.nosliw.core.application.HAPBrickBlockComplex;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPWrapperValue;
import com.nosliw.core.application.HAPWrapperValueOfBrick;
import com.nosliw.core.application.HAPWrapperValueOfReferenceResource;

public class HAPManualUtilityProcessor {

	public static boolean isAttributeAutoProcess(HAPManualAttribute attr, HAPManagerApplicationBrick entityMan) {
		//check attribute relation configure first
		HAPManualBrickRelationAutoProcess relation = (HAPManualBrickRelationAutoProcess)HAPManualUtilityBrick.getEntityRelation(attr, HAPConstantShared.MANUAL_RELATION_TYPE_AUTOPROCESS);
		if(relation!=null) {
			return relation.isAutoProcess();
		}
		
		HAPManualWrapperValue attrValueWrapper = attr.getValueWrapper();
		String valueWrapperType = attrValueWrapper.getValueType();
		if(!valueWrapperType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_VALUE)) {
			if(valueWrapperType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK)) {
				HAPManualWrapperValueBrick brickValueWrapper = (HAPManualWrapperValueBrick)attrValueWrapper;
				//no value context attribute
				if(brickValueWrapper.getBrickTypeId().getBrickType().equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public static HAPBrick buildExecutableTree(HAPManualBrick brickDef, HAPManualContextProcessBrick processContext) {
		HAPBrick rootBrickExe = newBrickInstance(brickDef, getBrickManager(processContext));
		buildExecutableTree(brickDef, rootBrickExe, processContext);
		return rootBrickExe;
	}

	private static void buildExecutableTree(HAPManualBrick brickDef, HAPBrick brick, HAPManualContextProcessBrick processContext) {
		HAPBundle bundle = processContext.getCurrentBundle();
		HAPManagerApplicationBrick brickManager = getBrickManager(processContext);
		
		HAPIdBrickType entityTypeId = brickDef.getBrickTypeId();

		if(brickManager.getBrickTypeInfo(entityTypeId).getIsComplex()) {
			((HAPBrickBlockComplex)brick).setValueStructureDomain(bundle.getValueStructureDomain());
		}
		
		List<HAPManualAttribute> attrsDef = brickDef.getAllAttributes();
		for(HAPManualAttribute attrDef : attrsDef) {
			if(HAPManualUtilityProcessor.isAttributeAutoProcess(attrDef, brickManager)) {
				HAPAttributeInBrick attrExe = new HAPAttributeInBrick();
				attrExe.setName(attrDef.getName());
				brick.setAttribute(attrExe);

				HAPManualWrapperValue attrValueInfo = attrDef.getValueWrapper();
				String attrValueType = attrValueInfo.getValueType();
				if(attrValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK)) {
					HAPManualBrick attrBrickDef = ((HAPManualWithBrick)attrValueInfo).getBrick();
					HAPBrick attrBrick = newBrickInstance(attrBrickDef, brickManager);
					attrExe.setValueWrapper(new HAPWrapperValueOfBrick(attrBrick));
					buildExecutableTree(attrBrickDef, attrBrick, processContext);
				}
				else if(attrValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE)) {
					//resource reference
					HAPManualWrapperValueReferenceResource resourceRefValueDef = (HAPManualWrapperValueReferenceResource)attrValueInfo;
					HAPWrapperValueOfReferenceResource resourceRefValue = new HAPWrapperValueOfReferenceResource(resourceRefValueDef.getResourceId());
					attrExe.setValueWrapper(resourceRefValue);
				}
				
				//adapter
				for(HAPManualAdapter defAdapterInfo : attrDef.getAdapters()) {
					HAPManualWrapperValue adapterValueWrapper = defAdapterInfo.getValueWrapper();
					String adapterValueType = adapterValueWrapper.getValueType();
					
					HAPWrapperValue adapterValueWrapperExe = null;
					if(adapterValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK)) {
						//brick
						HAPManualWrapperValueBrick adpaterValueDefWrapperBrick = (HAPManualWrapperValueBrick)adapterValueWrapper;
						HAPBrickAdapter adapterBrick = (HAPBrickAdapter)newBrickInstance(adpaterValueDefWrapperBrick.getBrick(), brickManager);
						adapterValueWrapperExe = new HAPWrapperValueOfBrick(adapterBrick);
						buildExecutableTree(adpaterValueDefWrapperBrick.getBrick(), adapterBrick, processContext);
					}
					else if(adapterValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE)) {
						//resource reference
						HAPManualWrapperValueReferenceResource adpaterValueDefWrapperResourceRef = (HAPManualWrapperValueReferenceResource)adapterValueWrapper;
						adapterValueWrapperExe = new HAPWrapperValueOfReferenceResource(adpaterValueDefWrapperResourceRef.getResourceId());
					}
					HAPAdapter adapter = new HAPAdapter(adapterValueWrapperExe);
					defAdapterInfo.cloneToEntityInfo(adapter);
					attrExe.addAdapter(adapter);
				}
			}
		}
	}
	
	private static HAPBrick newBrickInstance(HAPManualBrick brickDef, HAPManagerApplicationBrick brickManager) {
		return brickManager.newBrickInstance(brickDef.getBrickTypeId());
	}

	private static HAPManagerApplicationBrick getBrickManager(HAPManualContextProcessBrick processContext) {   return processContext.getRuntimeEnv().getBrickManager(); 	}

}
