package com.nosliw.common.interpolate;

import com.nosliw.common.test.HAPAssert;
import com.nosliw.common.test.HAPResultTestCase;
import com.nosliw.common.test.HAPTestCase;
import com.nosliw.common.test.HAPTestEnv;
import com.nosliw.common.test.HAPTestItemInfo;

public class HAPPatternProcessorInterpolationDynamicTest extends HAPPatternProcessorInterpolationDynamic{

	static final String TOKEN_START = "{{";
	static final String TOKEN_END = "}}";
	
	
	public HAPPatternProcessorInterpolationDynamicTest() {
		super(TOKEN_START, TOKEN_END);
	}

//	@HAPTestCase(name="${getName()}", description="general dynamic interpolation: based on method and attribute of baseObject")
//	public void test(HAPResultTestCase result, HAPTestEnv testEnv) {
//		//prepare test items
//		BaseObject baseObject = new BaseObject();
//		
//		HAPTestItemDescriptionImp[] testItems = {
//				new HAPTestItemDescriptionImp(this, "{{getName()}} play soccer at {{time}} this morning", "John play soccer at 10:00pm this morning", baseObject)
//		};
//
//		for(HAPTestItemDescriptionImp testItem : testItems){
//			testItem.test(result, testEnv);
//		}
//	}

	class BaseObject{
		public String time = "10:00pm";
		
		public String getName(){
			return "John";
		}
	}
	
	/*
	 * test item description
	 */
//	class HAPTestItemDescriptionImp extends HAPTestItemInfo{
//		public final static String ATTR_BASEOBJECT = "baseObject";
//		
//		private HAPPatternProcessorInterpolationDynamicTest m_testObj;
//		
//		public HAPTestItemDescriptionImp(HAPPatternProcessorInterpolationDynamicTest testObj, String input, String output, Object baseObject){
//			this.setInput(input);
//			this.setOutput(output);
//			this.setValue(ATTR_BASEOBJECT, baseObject);
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
//			HAPAssert.assertEquals(this.getOputputStr(), this.m_testObj.parse(this.getInputStr(), this.getValue(ATTR_BASEOBJECT)), testResult);
//		}
//	}
}
