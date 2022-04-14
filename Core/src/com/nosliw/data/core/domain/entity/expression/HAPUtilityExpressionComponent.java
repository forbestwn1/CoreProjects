package com.nosliw.data.core.domain.entity.expression;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.common.HAPUtilityWithValueStructure;
import com.nosliw.data.core.complex.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.complex.HAPUtilityComplexConstant;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachmentEntity;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.valuestructure.HAPValueStructureWrapper;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPUtilityStructure;

public class HAPUtilityExpressionComponent {

	public static HAPDefinitionEntityExpressionSuite buildExpressiionSuiteFromComponent(HAPDefinitionEntityInDomainComplex complexEntity, HAPRuntimeEnvironment runtimeEnv) {
		HAPDefinitionEntityExpressionSuite out = new HAPDefinitionEntityExpressionSuite();
		
		//build value structure
		HAPValueStructureWrapper valueStructureWrapper = HAPUtilityExpression.getValueStructure(complexEntity, runtimeEnv);
		HAPUtilityWithValueStructure.setValueStructure(out, valueStructureWrapper);
		
		//build constant from attachment
		for(HAPDefinitionConstant constantDef : HAPUtilityComplexConstant.buildDataConstantDefinition(complexEntity.getAttachmentContainer())) {
			out.addConstantDefinition(constantDef);
		}
		
		//constant from context
		Map<String, Object> constantsValue = HAPUtilityStructure.discoverConstantValue(valueStructureWrapper.getValueStructure());
		for(String id : constantsValue.keySet()) {
			HAPDefinitionConstant constantDef = new HAPDefinitionConstant(id, constantsValue.get(id));
			if(constantDef.isData()) {
				out.addConstantDefinition(constantDef);
			}
		}

		//build expression definition
		buildExpressionSuiteFromAttachment(out, complexEntity.getAttachmentContainer());
		
		return out;
	}
	
	public static void buildExpressionSuiteFromAttachment(HAPDefinitionExpressionSuite1 suite, HAPDefinitionEntityContainerAttachment attachmentContainer) {
		Map<String, HAPAttachment> expressionAtts = attachmentContainer.getAttachmentByType(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSION);
		for(String name : expressionAtts.keySet()) {
			HAPAttachment attachment = expressionAtts.get(name);
			suite.addEntityElement(buildExpressionGroup(attachment));
		}
	}

	private static HAPDefinitionExpressionGroup1 buildExpressionGroup(HAPAttachment attachment) {
		HAPDefinitionEntityExpressionGroup out = new HAPDefinitionEntityExpressionGroup();
		attachment.cloneToEntityInfo(out);
		if(HAPConstantShared.ATTACHMENT_TYPE_ENTITY.equals(attachment.getType())) {
			HAPAttachmentEntity entityAttachment = (HAPAttachmentEntity)attachment;
			entityAttachment.cloneToEntityInfo(out);
			JSONObject attachmentEntityJsonObj = entityAttachment.getEntityJsonObj();
			HAPParserExpressionDefinition.parseExpressionDefinitionList(out, attachmentEntityJsonObj);
		}
		else if(HAPConstantShared.ATTACHMENT_TYPE_REFERENCEEXTERNAL.equals(attachment.getType())) {
		}
		else if(HAPConstantShared.ATTACHMENT_TYPE_REFERENCELOCAL.equals(attachment.getType())) {
		}
		return out;
	}
}
