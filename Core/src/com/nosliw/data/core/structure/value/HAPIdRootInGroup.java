package com.nosliw.data.core.structure.value;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.structure.HAPReferenceRoot;

public class HAPIdRootInGroup implements HAPReferenceRoot{

	private String m_categary;
	
	private String m_name;
	
	public HAPIdRootInGroup(String categary, String name) {
		this.m_categary = categary;
		this.m_name = name;
	}
	
	public HAPIdRootInGroup(String name) {
		String[] segs = HAPNamingConversionUtility.splitTextByElements(name, HAPConstantShared.SEPERATOR_CONTEXT_CATEGARY_NAME);
		if(segs.length>=1)   this.m_name = segs[0];
		if(segs.length>=2)   this.m_categary = segs[1];
	}
	
	public String getCategary() {		return this.m_categary;	}
	public void setCategary(String categary) {  this.m_categary = categary;    }
	
	public String getName() {   return this.m_name;   }

	public String getFullName() {  return HAPNamingConversionUtility.cascadeElements(new String[] {this.m_name, this.m_categary}, HAPConstantShared.SEPERATOR_CONTEXT_CATEGARY_NAME);   }

	public String getPath() {  return HAPNamingConversionUtility.buildPath(m_categary, m_name);   }
	
	@Override
	public HAPIdRootInGroup clone() {
		HAPIdRootInGroup out = new HAPIdRootInGroup(this.m_categary, this.m_name);
		return out;
	}
	
	@Override
	public String toString() {   return this.getFullName();	}
}
