package com.nosliw.common.erro;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.nosliw.common.log.HAPLogUtility;

public class HAPErrorUtility {

	public static String log(Exception e){
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		String out = HAPLogUtility.txtToHtml(stringWriter.toString());
		printWriter.close();
		return out;
	}

	public static void invalid(String message) {
		throw new RuntimeException();
	}
	
}
