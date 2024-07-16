package com.nosliw.core.application.common.scriptexpression;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.core.application.common.dataexpression.HAPContainerDataExpression;
import com.nosliw.core.application.common.withvariable.HAPWithVariable;

@HAPEntityWithAttribute
public interface HAPExpressionScript extends HAPWithVariable, HAPSerializable{

	@HAPAttribute
	public final static String TYPE = "type";

	@HAPAttribute
	public static final String SCRIPTFUNCTION = "scriptFunction";

	@HAPAttribute
	public static final String SUPPORTFUNCTION = "supportFunction";

	@HAPAttribute
	public static final String DATAEXPRESSION = "dataExpression";

	String getType();

	HAPContainerDataExpression getDataExpressionContainer();
	
}
