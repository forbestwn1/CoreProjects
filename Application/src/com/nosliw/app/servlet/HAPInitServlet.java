package com.nosliw.app.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.nosliw.app.instance.HAPApplicationInstance;

public class HAPInitServlet  extends HttpServlet{

	   public void init() throws ServletException
	   {
			HAPApplicationInstance instance = HAPApplicationInstance.getApplicationInstantce();
//			this.getServletContext().setAttribute("appInstantce", instance);
	   }
}
