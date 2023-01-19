package com.nosliw.data.core.domain;

import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueContext;

//related information related with complex entity
public class HAPInfoEntityComplex {

	private HAPDefinitionEntityInDomainComplex m_definition;
	private HAPExecutableEntityComplex m_executable;
	private HAPDefinitionEntityValueContext m_valueStructureComplex;
	private HAPDefinitionEntityContainerAttachment m_attachmentContainer;
	
	public HAPDefinitionEntityInDomainComplex getDefinition(){  return this.m_definition;	}
	
	public HAPExecutableEntityComplex getExecutable(){  return this.m_executable; }
	
	public HAPDefinitionEntityValueContext getValueStructureComplex(){  return this.m_valueStructureComplex;  }
	
	public HAPDefinitionEntityContainerAttachment getAttachmentContainer(){  return this.m_attachmentContainer; 	}
	
}
