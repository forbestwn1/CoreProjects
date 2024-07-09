package com.nosliw.core.application.division.manual;

import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPWrapperValue;
import com.nosliw.core.application.HAPWrapperValueOfBrick;
import com.nosliw.core.application.HAPWrapperValueOfReferenceResource;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAdapter;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickRelationAutoProcess;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValue;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValueBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValueReferenceResource;
import com.nosliw.core.application.division.manual.executable.HAPManualAdapter;
import com.nosliw.core.application.division.manual.executable.HAPManualAttributeInBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickAdapter;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickBlockComplex;
import com.nosliw.core.application.division.manual.executable.HAPManualExeUtilityBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualWrapperValueOfBrick;

public class HAPManualUtilityProcessor {

	public static boolean isAttributeAutoProcess(HAPManualDefinitionAttributeInBrick attr, HAPManagerApplicationBrick entityMan) {
		//check attribute relation configure first
		HAPManualDefinitionBrickRelationAutoProcess relation = (HAPManualDefinitionBrickRelationAutoProcess)HAPManualDefinitionUtilityBrick.getEntityRelation(attr, HAPConstantShared.MANUAL_RELATION_TYPE_AUTOPROCESS);
		if(relation!=null) {
			return relation.isAutoProcess();
		}
		
		HAPManualDefinitionWrapperValue attrValueWrapper = attr.getValueWrapper();
		String valueWrapperType = attrValueWrapper.getValueType();
		if(!valueWrapperType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_VALUE)) {
			if(valueWrapperType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK)) {
				HAPManualDefinitionWrapperValueBrick brickValueWrapper = (HAPManualDefinitionWrapperValueBrick)attrValueWrapper;
				//no value context attribute
				if(brickValueWrapper.getBrickTypeId().getBrickType().equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public static HAPManualBrick buildExecutableTree(HAPManualDefinitionBrick brickDef, HAPManualContextProcessBrick processContext, HAPManualManagerBrick manualBrickMan) {
		HAPManualBrick rootBrickExe = HAPManualExeUtilityBrick.newRootBrickInstance(brickDef.getBrickTypeId(), manualBrickMan); 
		buildExecutableTree(brickDef, rootBrickExe, processContext, manualBrickMan);
		return rootBrickExe;
	}

	private static void buildExecutableTree(HAPManualDefinitionBrick brickDef, HAPManualBrick brick, HAPManualContextProcessBrick processContext, HAPManualManagerBrick manualBrickMan) {
		HAPBundle bundle = processContext.getCurrentBundle();
		HAPManagerApplicationBrick brickManager = getBrickManager(processContext);
		
		HAPIdBrickType entityTypeId = brickDef.getBrickTypeId();

		if(manualBrickMan.getBrickTypeInfo(entityTypeId).getIsComplex()) {
			((HAPManualBrickBlockComplex)brick).setValueStructureDomain(bundle.getValueStructureDomain());
		}
		
		List<HAPManualDefinitionAttributeInBrick> attrsDef = brickDef.getAllAttributes();
		for(HAPManualDefinitionAttributeInBrick attrDef : attrsDef) {
			if(HAPManualUtilityProcessor.isAttributeAutoProcess(attrDef, brickManager)) {
				HAPManualAttributeInBrick attrExe = new HAPManualAttributeInBrick();
				attrExe.setName(attrDef.getName());
				brick.setAttribute(attrExe);

				HAPManualDefinitionWrapperValue attrValueInfo = attrDef.getValueWrapper();
				String attrValueType = attrValueInfo.getValueType();
				if(attrValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK)) {
					HAPManualDefinitionBrick attrBrickDef = ((HAPManualWithBrick)attrValueInfo).getBrick();
					HAPManualBrick attrBrick = HAPManualExeUtilityBrick.newBrickInstance(attrBrickDef.getBrickTypeId(), manualBrickMan);
					attrExe.setValueWrapper(new HAPManualWrapperValueOfBrick(attrBrick));
					buildExecutableTree(attrBrickDef, attrBrick, processContext, manualBrickMan);
				}
				else if(attrValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE)) {
					//resource reference
					HAPManualDefinitionWrapperValueReferenceResource resourceRefValueDef = (HAPManualDefinitionWrapperValueReferenceResource)attrValueInfo;
					HAPWrapperValueOfReferenceResource resourceRefValue = new HAPWrapperValueOfReferenceResource(resourceRefValueDef.getResourceId());
					attrExe.setValueWrapper(resourceRefValue);
				}
				
				//adapter
				for(HAPManualDefinitionAdapter defAdapterInfo : attrDef.getAdapters()) {
					HAPManualDefinitionWrapperValue adapterValueWrapper = defAdapterInfo.getValueWrapper();
					String adapterValueType = adapterValueWrapper.getValueType();
					
					HAPWrapperValue adapterValueWrapperExe = null;
					if(adapterValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_BRICK)) {
						//brick
						HAPManualDefinitionWrapperValueBrick adpaterValueDefWrapperBrick = (HAPManualDefinitionWrapperValueBrick)adapterValueWrapper;
						HAPManualBrickAdapter adapterBrick = (HAPManualBrickAdapter)HAPManualExeUtilityBrick.newRootBrickInstance(adpaterValueDefWrapperBrick.getBrick().getBrickTypeId(), manualBrickMan);
						adapterValueWrapperExe = new HAPWrapperValueOfBrick(adapterBrick);
						buildExecutableTree(adpaterValueDefWrapperBrick.getBrick(), adapterBrick, processContext, manualBrickMan);
					}
					else if(adapterValueType.equals(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE)) {
						//resource reference
						HAPManualDefinitionWrapperValueReferenceResource adpaterValueDefWrapperResourceRef = (HAPManualDefinitionWrapperValueReferenceResource)adapterValueWrapper;
						adapterValueWrapperExe = new HAPWrapperValueOfReferenceResource(adpaterValueDefWrapperResourceRef.getResourceId());
					}
					HAPManualAdapter adapter = new HAPManualAdapter(adapterValueWrapperExe);
					defAdapterInfo.cloneToEntityInfo(adapter);
					attrExe.addAdapter(adapter);
				}
			}
		}
	}
	
	private static HAPManagerApplicationBrick getBrickManager(HAPManualContextProcessBrick processContext) {   return processContext.getRuntimeEnv().getBrickManager(); 	}

}
