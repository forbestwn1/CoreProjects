package com.nosliw.data.core.script.context;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPNamingConversionUtility;

public class HAPContextDefinitionRootId {

	private String m_categary;
	
	private String m_name;
	
	public HAPContextDefinitionRootId(String categary, String name) {
		this.m_categary = categary;
		this.m_name = name;
	}
	
	public HAPContextDefinitionRootId(String name) {
		String[] segs = HAPNamingConversionUtility.splitTextByElements(name, HAPConstant.SEPERATOR_CONTEXT_CATEGARY_NAME);
		this.m_name = segs[0];
		if(segs.length>=2)   this.m_categary = segs[1];
	}
	
	public String getCategary() {		return this.m_categary;	}
	public void setCategary(String categary) {  this.m_categary = categary;    }
	
	public String getName() {   return this.m_name;   }

	public String getFullName() {  return HAPNamingConversionUtility.cascadeElements(new String[] {this.m_name, this.m_categary}, HAPConstant.SEPERATOR_CONTEXT_CATEGARY_NAME);   }

	public String getPath() {  return HAPNamingConversionUtility.buildPath(m_categary, m_name);   }
	
	@Override
	public HAPContextDefinitionRootId clone() {
		HAPContextDefinitionRootId out = new HAPContextDefinitionRootId(this.m_categary, this.m_name);
		return out;
	}
	
	@Override
	public String toString() {   return this.getFullName();	}
	
}
