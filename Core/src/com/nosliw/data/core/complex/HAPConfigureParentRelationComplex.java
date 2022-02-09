package com.nosliw.data.core.complex;

import java.util.Set;

import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public class HAPConfigureParentRelationComplex {

	private HAPConfigureComplexRelationValueStructure m_valueStructureConfigure;
	private HAPConfigureComplexRelationAttachment m_attachmentMode;
	private HAPConfigureComplexRelationInfo m_infoMode;
	
	public void replaceAliasWithId(Set<String> alias, HAPIdEntityInDomain parentId) {
		if(this.m_parentId==null&&alias.contains(m_parentAlias)) {
			this.m_parentAlias = null;
			this.m_parentId = parentId;
		}
	}
	
	//attachment merge
	public HAPConfigureComplexRelationAttachment getAttachmentRelationMode() {   return this.m_attachmentMode;    }
	public void setAttachmentRelationMode(HAPConfigureComplexRelationAttachment attachmentRelationConfigure) {   this.m_attachmentMode = attachmentRelationConfigure;   }
	
	//info merge
	public HAPConfigureComplexRelationInfo getInfoRelationMode() {    return this.m_infoMode;    }
	public void setInfoRelationMode(HAPConfigureComplexRelationInfo infoRelationConfigure) {    this.m_infoMode = infoRelationConfigure;     }
	
	public HAPConfigureComplexRelationValueStructure getValueStructureRelationMode() {   return this.m_valueStructureConfigure;    }
	public void setValueStructureRelationMode(HAPConfigureComplexRelationValueStructure valueStructureConfigure) {    this.m_valueStructureConfigure = valueStructureConfigure;    }
}
