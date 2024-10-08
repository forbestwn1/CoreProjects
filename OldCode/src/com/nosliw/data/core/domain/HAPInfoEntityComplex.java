package com.nosliw.data.core.domain;

import com.nosliw.core.application.division.manual.HAPManualBrickComplex;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickValueContext;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;

//related information related with complex entity
public class HAPInfoEntityComplex {

	private HAPManualBrickComplex m_definition;
	private HAPExecutableEntityComplex m_executable;
	private HAPManualBrickValueContext m_valueStructureComplex;
	private HAPDefinitionEntityContainerAttachment m_attachmentContainer;
	
	public HAPManualBrickComplex getDefinition(){  return this.m_definition;	}
	
	public HAPExecutableEntityComplex getExecutable(){  return this.m_executable; }
	
	public HAPManualBrickValueContext getValueStructureComplex(){  return this.m_valueStructureComplex;  }
	
	public HAPDefinitionEntityContainerAttachment getAttachmentContainer(){  return this.m_attachmentContainer; 	}
	
}
