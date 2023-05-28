package com.nosliw.data.core.process1;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPManagerActivityPlugin;
import com.nosliw.data.core.component.HAPDefinitionEntityComponent;
import com.nosliw.data.core.component.HAPDefinitionEntityElementInContainerComponent;
import com.nosliw.data.core.domain.complexentity.HAPElementInContainerEntityDefinition;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachmentEntity;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachmentReference;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.component.HAPDefinitionEntityComponentImp;
import com.nosliw.data.core.process1.resource.HAPElementContainerResourceDefinitionReferenceProcessSuite;
import com.nosliw.data.core.process1.resource.HAPResourceDefinitionProcessSuite;
import com.nosliw.data.core.process1.util.HAPParserProcessDefinition;

public class HAPUtilityProcessComponent {

	public static HAPResourceDefinitionProcessSuite buildProcessSuiteFromComponent(HAPDefinitionEntityComponent component, HAPManagerActivityPlugin activityPluginMan) {
		HAPResourceDefinitionProcessSuite out = new HAPResourceDefinitionProcessSuite();
		if(component instanceof HAPDefinitionEntityComponentImp) {
			component.cloneToComplexResourceDefinition(out);
			Map<String, HAPAttachment> processAtts = component.getAttachmentContainer().getAttachmentByType(HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESS);
			
			for(String name : processAtts.keySet()) {
				HAPAttachment attachment = processAtts.get(name);
				out.addContainerElement(HAPUtilityProcessComponent.getProcessDefinitionElementFromAttachment(attachment, activityPluginMan));
			}
		}
		else if(component instanceof HAPDefinitionEntityElementInContainerComponent) {
			out = buildProcessSuiteFromComponent(((HAPDefinitionEntityElementInContainerComponent)component).getComponentEntity(), activityPluginMan);
		}
		return out;
	}
	
	public static HAPElementInContainerEntityDefinition getProcessDefinitionElementFromAttachment(HAPAttachment attachment, HAPManagerActivityPlugin activityPluginMan) {
		HAPElementInContainerEntityDefinition out = null;
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
	
	public static HAPElementInContainerEntityDefinition getProcessDefinitionElementFromAttachment(String name, HAPDefinitionEntityContainerAttachment attachmentContainer, HAPManagerActivityPlugin activityPluginMan) {
		HAPAttachment attachment = attachmentContainer.getElement(HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESS, name);
		return HAPUtilityProcessComponent.getProcessDefinitionElementFromAttachment(attachment, activityPluginMan);
	}

}
