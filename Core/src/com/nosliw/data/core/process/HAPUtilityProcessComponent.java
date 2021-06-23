package com.nosliw.data.core.process;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPManagerActivityPlugin;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPComponentContainerElement;
import com.nosliw.data.core.component.HAPComponentImp;
import com.nosliw.data.core.component.HAPElementContainerResourceDefinition;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentEntity;
import com.nosliw.data.core.component.attachment.HAPAttachmentReference;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.process.resource.HAPElementContainerResourceDefinitionReferenceProcessSuite;
import com.nosliw.data.core.process.resource.HAPResourceDefinitionProcessSuite;
import com.nosliw.data.core.process.util.HAPParserProcessDefinition;

public class HAPUtilityProcessComponent {

	public static HAPResourceDefinitionProcessSuite buildProcessSuiteFromComponent(HAPComponent component, HAPManagerActivityPlugin activityPluginMan) {
		HAPResourceDefinitionProcessSuite out = new HAPResourceDefinitionProcessSuite();
		if(component instanceof HAPComponentImp) {
			component.cloneToComplexResourceDefinition(out);
			Map<String, HAPAttachment> processAtts = component.getAttachmentContainer().getAttachmentByType(HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESS);
			
			for(String name : processAtts.keySet()) {
				HAPAttachment attachment = processAtts.get(name);
				out.addContainerElement(HAPUtilityProcessComponent.getProcessDefinitionElementFromAttachment(attachment, activityPluginMan));
			}
		}
		else if(component instanceof HAPComponentContainerElement) {
			out = buildProcessSuiteFromComponent(((HAPComponentContainerElement)component).getComponentEntity(), activityPluginMan);
		}
		return out;
	}
	
	public static HAPElementContainerResourceDefinition getProcessDefinitionElementFromAttachment(HAPAttachment attachment, HAPManagerActivityPlugin activityPluginMan) {
		HAPElementContainerResourceDefinition out = null;
		if(HAPConstantShared.ATTACHMENT_TYPE_ENTITY.equals(attachment.getType())) {
			HAPAttachmentEntity entityAttachment = (HAPAttachmentEntity)attachment;
			out = HAPParserProcessDefinition.parseProcess(entityAttachment.getEntityJsonObj(), activityPluginMan);
		}
		else if(HAPConstantShared.ATTACHMENT_TYPE_REFERENCEEXTERNAL.equals(attachment.getType())) {
			HAPAttachmentReference referenceAttachment = (HAPAttachmentReference)attachment;
			out = new HAPElementContainerResourceDefinitionReferenceProcessSuite(referenceAttachment.getReferenceId());
		}
		else if(HAPConstantShared.ATTACHMENT_TYPE_REFERENCELOCAL.equals(attachment.getType())) {
			HAPAttachmentReference referenceAttachment = (HAPAttachmentReference)attachment;
			out = new HAPElementContainerResourceDefinitionReferenceProcessSuite(referenceAttachment.getReferenceId());
		}
		out.setName(attachment.getName());
		return out;
	}
	
	public static HAPElementContainerResourceDefinition getProcessDefinitionElementFromAttachment(String name, HAPContainerAttachment attachmentContainer, HAPManagerActivityPlugin activityPluginMan) {
		HAPAttachment attachment = attachmentContainer.getElement(HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESS, name);
		return HAPUtilityProcessComponent.getProcessDefinitionElementFromAttachment(attachment, activityPluginMan);
	}

}
