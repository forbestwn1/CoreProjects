package com.nosliw.application.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.nosliw.common.configure.HAPConfigurableImp;
import com.nosliw.common.test.HAPResult;
import com.nosliw.common.test.HAPTestDescription;
import com.nosliw.common.test.HAPTestSuiteInfo;
import com.nosliw.common.test.HAPTestUtility;
import com.nosliw.common.test.export.html.HAPTestResultExporter;
import com.nosliw.common.utils.HAPClassFilter;

public class HAPTestApplication extends HAPConfigurableImp{

	protected HAPTestApplication() {
		super("testapp.properties");
	}
	
	private void prepareTest(HAPTestSuiteInfo rootSuite){
		new HAPClassFilter(){
			@Override
			protected void process(Class cls, Object data) {
				HAPTestSuiteInfo rootSuite = (HAPTestSuiteInfo)data;
				HAPTestSuiteInfo testSuiteInfo = HAPTestUtility.processTestSuiteClass(cls);
				if(testSuiteInfo!=null)		rootSuite.addTest(testSuiteInfo);
			}
			
		}.process(rootSuite);
	}
	
	private void export(HAPResult testResult){
	    String subfix = "result";
		String file = this.getConfigureValue("testResultPath").getStringValue()+"/"+subfix+".html";
		HAPTestResultExporter.export(testResult, file);
	}
	
	public static void main(String[] args){
		HAPTestApplication app = new HAPTestApplication();
		HAPTestSuiteInfo rootTestSuite = new HAPTestSuiteInfo(new HAPTestDescription("Root", "This is root"));
		app.prepareTest(rootTestSuite);
		HAPResult testResult = rootTestSuite.run(null);
		app.export(testResult);
	}
}
