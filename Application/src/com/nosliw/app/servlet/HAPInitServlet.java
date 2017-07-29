package com.nosliw.app.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.core.imp.runtime.js.HAPModuleRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeImpJSBroswerImp;
import com.nosliw.data.core.runtime.js.broswer.HAPRuntimeImpJSBroswer;

public class HAPInitServlet  extends HttpServlet{

	private static final long serialVersionUID = -703775909733982650L;

	public void init() throws ServletException
	   {
			//module init
			HAPModuleRuntimeJS runtimeJSModule = new HAPModuleRuntimeJS().init(HAPValueInfoManager.getInstance());;

			//create runtime
			HAPRuntimeImpJSBroswer runtime = new HAPRuntimeImpJSBroswerImp(runtimeJSModule);
			runtime.start();
			
			//set runtime object to context
			this.getServletContext().setAttribute("runtime", runtime);
	   }
}
