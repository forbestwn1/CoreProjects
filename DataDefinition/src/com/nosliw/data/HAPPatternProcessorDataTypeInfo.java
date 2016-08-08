package com.nosliw.data;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.pattern.HAPPatternProcessorImp;
import com.nosliw.common.test.HAPResultTestCase;
import com.nosliw.common.test.HAPTestCaseItem;
import com.nosliw.common.test.HAPTestEnv;
import com.nosliw.common.test.HAPTestItemInfo;
import com.nosliw.common.test.HAPTestSuite;
import com.nosliw.common.test.HAPAssert;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.info.HAPDataTypeInfo;

@HAPTestSuite(name="${this.getTestSuiteName()}", description="${this.getTestSuiteDescription()}")
public class HAPPatternProcessorDataTypeInfo extends HAPPatternProcessorImp{

	@Override
	public Object parse(String text, Object data) {
		String categary = null;
		String type = null;
		if(!HAPBasicUtility.isStringEmpty(text)){
	    	String[] parts = HAPNamingConversionUtility.splitText(text, HAPConstant.CONS_SEPERATOR_PART);
			type = parts[0];
			if(HAPBasicUtility.isStringEmpty(type))  type = null;
			if(parts.length>=2)   categary = parts[1];
			if(HAPBasicUtility.isStringEmpty(categary))  categary = null;
		}
		
		return new HAPDataTypeInfo(categary, type);
	}

	@Override
	public String compose(Object obj, Object data) {
		HAPDataTypeInfo dataTypeInfo = (HAPDataTypeInfo)obj;
		return HAPNamingConversionUtility.cascadeTexts(dataTypeInfo.getType(), dataTypeInfo.getCategary(), HAPConstant.CONS_SEPERATOR_PART);
	}

	@HAPTestCaseItem(name="${this.getName()}", description="type{{CONS_SEPERATOR_PART}}categary, ")
	public List<HAPTestItemInfo> test(HAPTestEnv testEnv) {
		List<HAPTestItemInfo> out = new ArrayList<HAPTestItemInfo>();
		out.add(new HAPTestItemDescriptionImp(this, "type"+HAPConstant.CONS_SEPERATOR_PART+"categary", "type", "categary","type"+ HAPConstant.CONS_SEPERATOR_PART+"categary", testEnv));
		out.add(new HAPTestItemDescriptionImp(this, HAPConstant.CONS_SEPERATOR_PART+"categary", null, "categary", HAPConstant.CONS_SEPERATOR_PART+"categary", testEnv));
		out.add(new HAPTestItemDescriptionImp(this, "type"+HAPConstant.CONS_SEPERATOR_PART, "type", null, "type"+HAPConstant.CONS_SEPERATOR_PART, testEnv));
		out.add(new HAPTestItemDescriptionImp(this, "type", "type", null, "type"+HAPConstant.CONS_SEPERATOR_PART, testEnv));
		out.add(new HAPTestItemDescriptionImp(this, null, null, null, ""+HAPConstant.CONS_SEPERATOR_PART+"", testEnv));
		out.add(new HAPTestItemDescriptionImp(this, "", null, null, ""+HAPConstant.CONS_SEPERATOR_PART+"", testEnv));
		return out;
	}
	
	/*
	 * test item description
	 */
	class HAPTestItemDescriptionImp extends HAPTestItemInfo{
		public final static String ATTR_OUTTYPE = "outType";
		public final static String ATTR_OUTCATEGARY = "outCategary";
		
		private HAPPatternProcessorDataTypeInfo m_testObj;
		
		public HAPTestItemDescriptionImp(HAPPatternProcessorDataTypeInfo testObj, String input, String type, String categary, String output, HAPTestEnv testEnv){
			super(testEnv);
			this.setInputStr(input);
			this.setOutputStr(output);
			this.setValueStr(ATTR_OUTTYPE, type);
			this.setValueStr(ATTR_OUTCATEGARY, categary);
			this.m_testObj = testObj;
			this.setDescription(input + "----->" + "type:" + type + "  /  " + "categary:" +  categary);
		}
		
		@Override
		public void doTest(HAPResultTestCase testResult){
			HAPDataTypeInfo dataTypeInfo = (HAPDataTypeInfo)m_testObj.parse(this.getInputStr(), null);
			String dataTypeInfoCompse = m_testObj.compose(dataTypeInfo, null);
			HAPAssert.assertEquals(dataTypeInfoCompse, this.getOutputStr(), testResult);
			HAPAssert.assertEquals(dataTypeInfo.getType(), this.getValueStr(HAPTestItemDescriptionImp.ATTR_OUTTYPE), testResult);
			HAPAssert.assertEquals(dataTypeInfo.getCategary(), this.getValueStr(HAPTestItemDescriptionImp.ATTR_OUTCATEGARY), testResult);
		}
	}
}
