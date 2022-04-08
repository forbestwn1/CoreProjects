package com.nosliw.data.core.complex;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPConfigureComplexRelationValueStructure extends HAPSerializableImp{

	//value structure inheritance
	public String getInheritanceMode() {   return null;    }
	public boolean isInheritable() {   return true;   }
	
	//value structure runtime transparent
	public String getRuntimeDataMode() {   return null;    }
	
	public boolean isShareRuntimeData() {   return true;    }
	
	//ascalate
	
	//
	
	public void mergeHard(HAPConfigureComplexRelationValueStructure configure) {
	}
	

}
