package com.nosliw.miniapp.data;

import com.nosliw.common.utils.HAPConstant;

public class HAPDefinitionMiniAppDataSetting extends HAPDefinitionMiniAppData{

	@Override
	public String getType() {  return HAPConstant.MINIAPPDATA_TYPE_SETTING;  }

	@Override
	protected boolean buildObjectByFullJson(Object json){
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}
	
}
