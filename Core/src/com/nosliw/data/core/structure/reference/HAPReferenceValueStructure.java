package com.nosliw.data.core.structure.reference;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPReferenceValueStructure extends HAPSerializableImp{

	//refer to name of value structure
	private String m_name;
	
	//refer to unique value structure definition id
	private String m_id;

	public String getName() {    return this.m_name;    }
	
	public String getId() {    return this.m_id;     }
	
}
