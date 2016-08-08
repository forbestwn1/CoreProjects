package com.nosliw.common.interpolate;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.test.HAPAssert;
import com.nosliw.common.test.HAPResultTestCase;
import com.nosliw.common.test.HAPTestCase;
import com.nosliw.common.test.HAPTestEnv;
import com.nosliw.common.test.HAPTestItemInfo;

public class HAPPatternProcessorInterpolationStaticTest extends HAPPatternProcessorInterpolationStatic{

	static final String TOKEN_START = "{{";
	static final String TOKEN_END = "}}";
	
	
	public HAPPatternProcessorInterpolationStaticTest() {
		super(TOKEN_START, TOKEN_END);
	}

//	@HAPTestCase(name="${getName()}", description="general static interpolation")
//	public void test(HAPResultTestCase result, HAPTestEnv testEnv) {
//		//prepare test items
//		Map<String, String> variables = new LinkedHashMap<String, String>();
//		variables.put("var1", "var1Value");
//		variables.put("var2", "var2Value");
//		variables.put("var3", "var3Value");
//		
//		HAPTestItemDescriptionImp[] testItems = {
//				new HAPTestItemDescriptionImp(this, "This is a "+TOKEN_START+"var1"+TOKEN_END+" and "+TOKEN_START+"var2"+TOKEN_END+" value", "This is a var1Value and var2Value value", variables),
//		};
//
//		for(HAPTestItemDescriptionImp testItem : testItems){
//			testItem.test(result, testEnv);
//		}
//	}

	/*
	 * test item description
	 */
//	class HAPTestItemDescriptionImp extends HAPTestItemInfo{
//		public final static String ATTR_VARIABLES = "variables";
//		
//		private HAPPatternProcessorInterpolationStaticTest m_testObj;
//		
//		public HAPTestItemDescriptionImp(HAPPatternProcessorInterpolationStaticTest testObj, String input, String output, Map<String, String> variables){
//			this.setInput(input);
//			this.setOutput(output);
//			this.setValue(ATTR_VARIABLES, variables);
//			this.m_testObj = testObj;
//		}
//		
//		@Override
//		public String log() {
//			return this.getInputStr() + "----->" + this.getOputputStr();
//		}
//		
//		@Override
//		public void doTest(HAPResultTestCase testResult, HAPTestEnv testEnv){
//			HAPAssert.assertEquals(this.getOputputStr(), this.m_testObj.parse(this.getInputStr(), this.getValue(ATTR_VARIABLES)), testResult);
//		}
//	}
	
}
