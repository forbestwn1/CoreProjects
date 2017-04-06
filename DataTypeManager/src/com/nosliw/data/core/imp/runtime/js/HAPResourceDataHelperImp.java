package com.nosliw.data.core.imp.runtime.js;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntityWithID;
import com.nosliw.data.core.runtime.js.HAPResourceDataHelper;

public class HAPResourceDataHelperImp extends HAPStringableValueEntityWithID implements HAPResourceDataHelper{

	public static String _VALUEINFO_NAME;
	
	@HAPAttribute
	public static String SCRIPT = "script";

	public HAPResourceDataHelperImp(String script){
		this.updateAtomicChildStrValue(SCRIPT, script);
	}
	
	@Override
	public String getScript(){  return this.getAtomicAncestorValueString(SCRIPT);  }
}
