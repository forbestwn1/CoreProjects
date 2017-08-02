package com.nosliw.data.core.imp.init;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.imp.HAPDataTypeManagerImp;
import com.nosliw.data.core.imp.runtime.js.HAPResourceDiscoveryJSImp;

public class HAPModuleInit {

	public static void init(){

		//init db connection
//		HAPDBAccess111.getInstance();
		
		//import all the value infos
		
		
		HAPValueInfoManager.getInstance().importFromFolder(HAPFileUtility.getClassFolderName(HAPResourceDiscoveryJSImp.class), false);
		HAPValueInfoManager.getInstance().importFromFolder(HAPFileUtility.getClassFolderName(HAPDataTypeManagerImp.class), false);

//		new HAPRuntimeImpJSRhino();
		
	}
	
}
