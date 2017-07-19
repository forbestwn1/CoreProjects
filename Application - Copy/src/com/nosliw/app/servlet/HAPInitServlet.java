package com.nosliw.app.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.imp.runtime.js.HAPResourceDiscoveryJSImp;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSConverter;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSHelper;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSLibrary;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSOperation;
import com.nosliw.data.core.runtime.js.HAPResourceManagerJS;
import com.nosliw.data.core.runtime.js.broswer.HAPRuntimeImpJSBroswer;

public class HAPInitServlet  extends HttpServlet{

	   public void init() throws ServletException
	   {
//			HAPApplicationInstance instance = HAPApplicationInstance.getApplicationInstantce();
//			this.getServletContext().setAttribute("appInstantce", instance);
		   
			HAPResourceManagerJS resourceMan = new HAPResourceManagerJS();
			resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_OPERATION, new HAPResourceManagerJSOperation());
			resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_CONVERTER, new HAPResourceManagerJSConverter());
			resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_JSLIBRARY, new HAPResourceManagerJSLibrary());
			resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_JSHELPER, new HAPResourceManagerJSHelper());
			
//			HAPDataTypeManager dataTypeMan = new HAPDataTypeManagerImp();
		   
			HAPRuntimeImpJSBroswer runtime = new HAPRuntimeImpJSBroswer(new HAPResourceDiscoveryJSImp(), resourceMan);
			this.getServletContext().setAttribute("runtime", runtime);
		   
		   
	   }
}
