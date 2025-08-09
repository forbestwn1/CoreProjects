package com.nosliw.core.application.common.dataexpression;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.core.data.HAPDataTypeId;
import com.nosliw.core.data.matcher.HAPMatchers;

public interface HAPOperandOperation extends HAPOperand{

	@HAPAttribute
	public static final String DATATYPEID = "dataTypeId";
	
	@HAPAttribute
	public static final String OPERATION = "operation";
	
	@HAPAttribute
	public static final String BASE = "base";

	@HAPAttribute
	public static final String PARMS = "parms";

	@HAPAttribute
	public static final String MATCHERSPARMS = "matchersParms";

	HAPDataTypeId getDataTypeId();

	String getOperaion();

	HAPOperand getBase();
	
	Map<String, HAPOperand> getParms();
	
	Map<String, HAPMatchers> getParmMatchers();
}
