package com.nosliw.data.core.imp.runtime.js;

import com.nosliw.common.strvalue.HAPStringableValueEntityWithID;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSHelper;

public class HAPResourceDataHelperImp extends HAPStringableValueEntityWithID implements HAPResourceDataJSHelper{

	public static String _VALUEINFO_NAME;
	
	public HAPResourceDataHelperImp(){}
	
	public HAPResourceDataHelperImp(String script){
		this.updateAtomicChildStrValue(VALUE, script);
	}
	
	@Override
	public String getValue(){  return this.getAtomicAncestorValueString(VALUE);  }
}
