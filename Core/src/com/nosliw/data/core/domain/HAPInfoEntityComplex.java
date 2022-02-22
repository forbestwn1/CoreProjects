package com.nosliw.data.core.domain;

import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityComplexValueStructure;

//related information related with complex entity
public class HAPInfoEntityComplex {

	private HAPDefinitionEntityComplex m_definition;
	private HAPExecutableEntityComplex m_executable;
	private HAPDefinitionEntityComplexValueStructure m_valueStructureComplex;
	private HAPDefinitionEntityContainerAttachment m_attachmentContainer;
	
	public HAPDefinitionEntityComplex getDefinition(){  return this.m_definition;	}
	
	public HAPExecutableEntityComplex getExecutable(){  return this.m_executable; }
	
	public HAPDefinitionEntityComplexValueStructure getValueStructureComplex(){  return this.m_valueStructureComplex;  }
	
	public HAPDefinitionEntityContainerAttachment getAttachmentContainer(){  return this.m_attachmentContainer; 	}
	
}
