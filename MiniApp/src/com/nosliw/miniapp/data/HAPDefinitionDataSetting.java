package com.nosliw.miniapp.data;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.context.HAPContextNodeRoot;
import com.nosliw.data.context.HAPContextNodeRootAbsolute;

public class HAPDefinitionDataSetting extends HAPDefinitionData{

	//definition of data
	private HAPContextNodeRootAbsolute m_definition;
	
	@Override
	public String getType() {  return HAPConstant.MINIAPPDATA_TYPE_SETTING;  }

	@Override
	protected boolean buildObjectByFullJson(Object json){
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}
	
}
