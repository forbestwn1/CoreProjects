package com.nosliw.data.core.activity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;
import com.nosliw.data.core.complex.attachment.HAPAttachment;
import com.nosliw.data.core.complex.attachment.HAPAttachmentEntity;
import com.nosliw.data.core.complex.attachment.HAPContainerAttachment;
import com.nosliw.data.core.complex.valuestructure.HAPWrapperValueStructure;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPUtilityActivityComponent {

	public static HAPDefinitionActivitySuiteImp buildActivitySuiteFromComponent(HAPDefinitionEntityComplex complexEntity, HAPRuntimeEnvironment runtimeEnv) {
		HAPDefinitionActivitySuiteImp out = new HAPDefinitionActivitySuiteImp();
		
		//build context
		HAPWrapperValueStructure valueStructureWrapper = HAPUtilityComponent.getValueStructure(complexEntity, runtimeEnv);
		out.setValueStructureWrapper(valueStructureWrapper);
		
//		//build constant from attachment
//		for(HAPDefinitionConstant constantDef : HAPUtilityComponentConstant.buildDataConstantDefinition(complexEntity.getAttachmentContainer())) {
//			out.addConstantDefinition(constantDef);
//		}
//		
//		//constant from context
//		Map<String, Object> constantsValue = HAPUtilityStructure.discoverConstantValue(valueStructureWrapper.getValueStructure());
//		for(String id : constantsValue.keySet()) {
//			HAPDefinitionConstant constantDef = new HAPDefinitionConstant(id, constantsValue.get(id));
//			if(constantDef.isData()) {
//				out.addConstantDefinition(constantDef);
//			}
//		}

		//build expression definition
		buildActivitySuiteFromAttachment(out, complexEntity.getAttachmentContainer(), runtimeEnv.getActivityManager().getPluginManager());
		
		return out;
	}
	
	public static void buildActivitySuiteFromAttachment(HAPDefinitionActivitySuiteImp suite, HAPContainerAttachment attachmentContainer, HAPManagerActivityPlugin activityPluginMan) {
		Map<String, HAPAttachment> activityAtts = attachmentContainer.getAttachmentByType(HAPConstantShared.RUNTIME_RESOURCE_TYPE_ACTIVITYSUITE);
		for(String name : activityAtts.keySet()) {
			HAPAttachment attachment = activityAtts.get(name);
			suite.addEntityElement(buildActivity(attachment, activityPluginMan));
		}
	}

	private static HAPDefinitionActivity buildActivity(HAPAttachment attachment, HAPManagerActivityPlugin activityPluginMan) {
		HAPDefinitionActivity out = null;
		if(HAPConstantShared.ATTACHMENT_TYPE_ENTITY.equals(attachment.getType())) {
			HAPAttachmentEntity entityAttachment = (HAPAttachmentEntity)attachment;
			JSONObject attachmentEntityJsonObj = entityAttachment.getEntityJsonObj();
			out = HAPParserActivity.parseActivityDefinition(attachmentEntityJsonObj, activityPluginMan);
		}
		else if(HAPConstantShared.ATTACHMENT_TYPE_REFERENCEEXTERNAL.equals(attachment.getType())) {
		}
		else if(HAPConstantShared.ATTACHMENT_TYPE_REFERENCELOCAL.equals(attachment.getType())) {
		}
		
		return out;
	}
}
