package com.nosliw.data.core.domain;

import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;
import com.nosliw.data.core.complex.attachment.HAPContainerAttachment;
import com.nosliw.data.core.domain.entity.valuestructure.HAPComplexValueStructure;

//related information related with complex entity
public class HAPInfoEntityComplex {

	private HAPDefinitionEntityComplex m_definition;
	private HAPExecutableEntityComplex m_executable;
	private HAPComplexValueStructure m_valueStructureComplex;
	private HAPContainerAttachment m_attachmentContainer;
	
	public HAPDefinitionEntityComplex getDefinition(){  return this.m_definition;	}
	
	public HAPExecutableEntityComplex getExecutable(){  return this.m_executable; }
	
	public HAPComplexValueStructure getValueStructureComplex(){  return this.m_valueStructureComplex;  }
	
	public HAPContainerAttachment getAttachmentContainer(){  return this.m_attachmentContainer; 	}
	
}
