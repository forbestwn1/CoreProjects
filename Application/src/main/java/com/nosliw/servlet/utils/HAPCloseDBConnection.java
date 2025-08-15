package com.nosliw.servlet.utils;

import java.io.IOException;
import java.sql.SQLException;

import com.nosliw.data.core.imp.io.HAPDBSource;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HAPCloseDBConnection extends HttpServlet{

	@Override
	protected void doGet (HttpServletRequest request,	HttpServletResponse response) throws ServletException, IOException {
		try {
			HAPDBSource.getDefaultDBSource().getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
