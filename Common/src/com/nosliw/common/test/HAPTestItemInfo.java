package com.nosliw.common.test;

import com.nosliw.common.utils.HAPConstant;

/*
 * information about test item : 
 * 		what is input
 * 		what is expect output
 * so that it can be used during log
 */
public abstract class HAPTestItemInfo extends HAPTestInfo{
	
	private Object m_input;
	private Object m_output;
	
	public HAPTestItemInfo(HAPTestEnv testEnv) {
		super(new HAPTestDescription(), -1, testEnv);
	}

	private final static String ATTR_INPUT = "input";
	private final static String ATTR_OUTPUT = "output";

	//helper method to deal with predefined input and out attribute
	protected Object getInput(){  return this.m_input; }
	protected void setInput(Object input){ this.m_input = input; }
	protected Object getOutput(){ return this.m_output; }
	protected void setOutput(Object output){  this.m_output = output; }
	
	protected void setInputStr(String input){ this.getTestDescription().updateBasicChild(ATTR_INPUT, input);}
	protected String getInputStr(){  return this.getTestDescription().getBasicAncestorValueString(ATTR_INPUT); }  
	
	protected void setOutputStr(String output){ this.getTestDescription().updateBasicChild(ATTR_OUTPUT, output);}
	protected String getOutputStr(){  return this.getTestDescription().getBasicAncestorValueString(ATTR_OUTPUT); }  
	
	public String getValueStr(String name){  return this.getTestDescription().getBasicAncestorValueString(name); }
	public void setValueStr(String name, String value){  this.getTestDescription().updateBasicChild(name, value); }
	
	abstract public void doTest(HAPResultTestCase testResult);

	@Override
	public String getType(){ return HAPConstant.TEST_TYPE_ITEM; }

	//run the test for test item
	public HAPResult run(HAPResult parentResult){
		HAPResultTestCase testResult = (HAPResultTestCase)parentResult;
		//update variable placeholder
		this.resolveTestDescription();
		//create empty test case result to collect all the result (isSuccess, exception)
		HAPResultTestCase temp = new HAPResultTestCase(null);
		try{
			this.doTest(temp);
		}
		catch(Exception e){
			e.printStackTrace();
			temp.addException(e);
		}
		temp.addTestItemResult(new HAPResultTestItem(this.getTestDescription(), temp.isSuccess()));
		testResult.importResult(temp);
		return testResult;
	}
	
	@Override
	public void beforeRunTest() {
		this.resolveTestDescription();
	}
}
