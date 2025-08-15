package com.nosliw.servlet.utils;

import java.io.IOException;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.servlet.HAPBaseServlet;
import com.nosliw.servlet.core.HAPGatewayServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HAPResetGatewayServlet extends HAPBaseServlet{

	private static final long serialVersionUID = -1256294161988262894L;

	@Override
	public void doGet (HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		
		HAPGatewayServlet.index = 0;
		HAPServiceData serviceData = HAPServiceData.createSuccessData();
		this.printContent(serviceData, response);
	}
}
