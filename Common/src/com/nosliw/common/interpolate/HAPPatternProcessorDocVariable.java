package com.nosliw.common.interpolate;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.configure.HAPConfigureManager;
import com.nosliw.common.test.HAPAssert;
import com.nosliw.common.test.HAPResultTestCase;
import com.nosliw.common.test.HAPTestCase;
import com.nosliw.common.test.HAPTestEnv;
import com.nosliw.common.utils.HAPConstant;

public class HAPPatternProcessorDocVariable extends HAPPatternProcessorInterpolationStatic{

	public HAPPatternProcessorDocVariable(){
		super(HAPConstant.CONS_SEPERATOR_VARSTART, HAPConstant.CONS_SEPERATOR_VAREND);
	}
	
	@HAPTestCase(description="PorcessVariable:  {{CONS_SEPERATOR_VARSTART}}part1{{CONS_SEPERATOR_VAREND}}part2{{CONS_SEPERATOR_VARSTART}}var2{{CONS_SEPERATOR_VAREND}}part3, the variables within the text are replaced with variables values ")
	public void processVariable(HAPResultTestCase result, HAPTestEnv testEnv) {
		HAPConfigureImp configure = HAPConfigureManager.getInstance().newConfigure();
		configure.addConfigureItem("plus", "+");
		configure.addConfigureItem("deduct", "-");
		configure.addConfigureItem("equals", "=");
		
		String input;
		String output;
		
		input = "234{{plus}}123{{equals}}357";
		output = "234+123=357";
		this.test(input, output, configure, result);

		input = "{{minus}}234{{plus}}123{{equals}}357";
		output = "{{minus}}234+123=357";
		this.test(input, output, configure, result);

		input = "234{plus}}123{{equals}}357";
		output = "234{plus}}123=357";
		this.test(input, output, configure, result);

		input = "234{{plus}123{{equals}}357";
		output = "234{{plus}123{{equals}}357";
		this.test(input, output, configure, result);
	}
	
	private void test(String input, String output, HAPConfigureImp configure, HAPResultTestCase result){
		try{
			HAPInterpolateOutput out = (HAPInterpolateOutput)this.parse(input, configure);
			HAPAssert.assertEquals(output, out.getOutput(), result);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
