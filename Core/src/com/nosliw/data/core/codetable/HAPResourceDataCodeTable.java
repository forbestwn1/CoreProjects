package com.nosliw.data.core.codetable;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValueImp;

public class HAPResourceDataCodeTable  extends HAPResourceDataJSValueImp{

	private HAPCodeTable m_codeTable;
	
	public HAPResourceDataCodeTable(HAPCodeTable codeTable){
		this.m_codeTable = codeTable;
	}
	
	public HAPCodeTable getCodeTable(){ return this.m_codeTable;  }
	
	@Override
	public String getValue() {
		return this.m_codeTable.toStringValue(HAPSerializationFormat.JSON_FULL);
	}

}
