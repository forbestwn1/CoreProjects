package com.nosliw.data.core.runtime.js;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPResourceHelper;
import com.nosliw.data.core.runtime.HAPRuntimeImp;

public abstract class HAPRuntimeImpJS extends HAPRuntimeImp{

	public HAPRuntimeImpJS(){
		super();
		HAPResourceHelper resourceHelper = HAPResourceHelper.getInstance();
		resourceHelper.registerResourceId(HAPConstant.RUNTIME_RESOURCE_TYPE_JSHELPER, HAPResourceIdJSHelper.class, HAPJSHelperId.class);
		resourceHelper.registerResourceId(HAPConstant.RUNTIME_RESOURCE_TYPE_JSLIBRARY, HAPResourceIdJSLibrary.class, HAPJSLibraryId.class);
	}
}
