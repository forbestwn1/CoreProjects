package com.nosliw.servlet.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nosliw.servlet.HAPBaseServlet;

public class HAPClearResourceCacheServlet extends HAPBaseServlet {

	@Override
	protected void doGet (HttpServletRequest request,	HttpServletResponse response) throws ServletException, IOException {
		this.getResourceManager().clearCache();
	}
}
