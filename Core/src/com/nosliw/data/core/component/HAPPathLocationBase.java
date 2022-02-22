package com.nosliw.data.core.component;

import com.nosliw.common.serialization.HAPSerializableImp;

//this object store information for path base for local resource id reference defined in component
public class HAPPathLocationBase extends HAPSerializableImp{

	private String m_path;
	
	public HAPPathLocationBase(String path) {
		this.m_path = path;
	}
	
	public HAPPathLocationBase() {	}

	public String getPath() {    return this.m_path;    }

	public HAPPathLocationBase cloneLocalReferenceBase() {
		return new HAPPathLocationBase(this.m_path);
	}
	
	@Override
	protected String buildLiterate(){  return this.m_path; }

	@Override
	protected boolean buildObjectByLiterate(String literateValue){	
		this.m_path = literateValue;
		return true;  
	}

}
