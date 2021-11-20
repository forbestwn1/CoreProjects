package com.nosliw.data.core.complex;

import java.util.Set;

public class HAPInfoParentEntity {

	private String m_parentId;
	private String m_alias;

	private HAPConfigureComplexRelationValueStructure m_valueStructureConfigure;
	private HAPConfigureComplexRelationAttachment m_attachmentMode;
	private HAPConfigureComplexRelationInfo m_infoMode;
	
	public void replaceAliasWithId(Set<String> alias, String parentId) {
		if(this.m_parentId==null&&alias.contains(m_alias)) {
			this.m_alias = null;
			this.m_parentId = parentId;
		}
	}
	
	public String getParentId() {    return this.m_parentId;   }
	
	//attachment merge
	public HAPConfigureComplexRelationAttachment getAttachmentRelationMode() {   return this.m_attachmentMode;    }
	
	//info merge
	public HAPConfigureComplexRelationInfo getInfoRelationMode() {    return this.m_infoMode;    }
	
	public HAPConfigureComplexRelationValueStructure getValueStructureRelationMode() {   return this.m_valueStructureConfigure;    }
}
