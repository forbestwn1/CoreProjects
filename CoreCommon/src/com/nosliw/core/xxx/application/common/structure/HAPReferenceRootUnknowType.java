package com.nosliw.core.xxx.application.common.structure;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPReferenceRootUnknowType extends HAPSerializableImp implements HAPReferenceRootInStrucutre{

	private String m_content;
	
	public HAPReferenceRootUnknowType() {}
	
	public HAPReferenceRootUnknowType(String content) {
		this.m_content = content;
	}
	
	public String getContent() {    return this.m_content;  }
	
	@Override
	protected String buildLiterate(){  return this.m_content; }

	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		this.m_content = literateValue;
		return true;  
	}

	@Override
	public HAPReferenceRootInStrucutre cloneStructureRootReference() {  return new HAPReferenceRootUnknowType(this.m_content);  }

	@Override
	public String getStructureType() {   return HAPConstantShared.STRUCTURE_TYPE_UNKNOWN;  }

}
