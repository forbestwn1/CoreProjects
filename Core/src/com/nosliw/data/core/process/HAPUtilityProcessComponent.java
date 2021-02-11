package com.nosliw.data.core.process;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPComponentContainerElement;
import com.nosliw.data.core.component.HAPComponentImp;
import com.nosliw.data.core.component.HAPResourceDefinitionContainerElement;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentEntity;
import com.nosliw.data.core.component.attachment.HAPAttachmentReference;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.util.HAPParserProcessDefinition;

public class HAPUtilityProcessComponent {

	public static HAPDefinitionProcessSuite buildProcessSuiteFromComponent(HAPComponent component, HAPManagerActivityPlugin activityPluginMan) {
		HAPDefinitionProcessSuite out = new HAPDefinitionProcessSuite();
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
	
	public static HAPResourceDefinitionContainerElement getProcessDefinitionElementFromAttachment(HAPAttachment attachment, HAPManagerActivityPlugin activityPluginMan) {
		HAPResourceDefinitionContainerElement out = null;
		if(HAPConstantShared.ATTACHMENT_TYPE_ENTITY.equals(attachment.getType())) {
			HAPAttachmentEntity entityAttachment = (HAPAttachmentEntity)attachment;
			out = HAPParserProcessDefinition.parseProcess(entityAttachment.getEntityJsonObj(), activityPluginMan);
		}
		else if(HAPConstantShared.ATTACHMENT_TYPE_REFERENCEEXTERNAL.equals(attachment.getType())) {
			HAPAttachmentReference referenceAttachment = (HAPAttachmentReference)attachment;
			out = new HAPDefinitionProcessSuiteElementReference(referenceAttachment.getReferenceId());
		}
		else if(HAPConstantShared.ATTACHMENT_TYPE_REFERENCELOCAL.equals(attachment.getType())) {
			HAPAttachmentReference referenceAttachment = (HAPAttachmentReference)attachment;
			out = new HAPDefinitionProcessSuiteElementReference(referenceAttachment.getReferenceId());
		}
		out.setName(attachment.getName());
		return out;
	}
	
	public static HAPResourceDefinitionContainerElement getProcessDefinitionElementFromAttachment(String name, HAPContainerAttachment attachmentContainer, HAPManagerActivityPlugin activityPluginMan) {
		HAPAttachment attachment = attachmentContainer.getElement(HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESS, name);
		return HAPUtilityProcessComponent.getProcessDefinitionElementFromAttachment(attachment, activityPluginMan);
	}

}
