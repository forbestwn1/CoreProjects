package com.nosliw.data.core.runtime.js;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

/**
 * this interface define all the call back method that invoked from js runtime env  
 */
@HAPEntityWithAttribute
public interface HAPRuntimeGatewayJS {
	
	@HAPAttribute
	final public static String REQUEST_DISCOVERRESOURCES = "requestDiscoverResources";
	@HAPAttribute
	final public static String REQUEST_DISCOVERRESOURCES_RESOURCEIDS = "resourceIds";

	@HAPAttribute
	final public static String REQUEST_DISCOVERANDLOADRESOURCES = "requestDiscoverAndLoadResources";
	@HAPAttribute
	final public static String REQUEST_DISCOVERANDLOADRESOURCES_RESOURCEIDS = "resourceIds";

	@HAPAttribute
	final public static String REQUEST_LOADRESOURCES = "requestLoadResources";
	@HAPAttribute
	final public static String REQUEST_LOADRESOURCES_RESOURCEINFOS = "resourceInfos";

	@HAPAttribute
	final public static String REQUEST_GETEXPRESSIONS = "getExpressions";
	@HAPAttribute
	final public static String REQUEST_GETEXPRESSIONS_EXPRESSIONS = "expressions";
	@HAPAttribute
	final public static String REQUEST_GETEXPRESSIONS_ELEMENT_SUITE = "suite";
	@HAPAttribute
	final public static String REQUEST_GETEXPRESSIONS_ELEMENT_EXPRESSIONNAME = "expressionName";
	@HAPAttribute
	final public static String REQUEST_GETEXPRESSIONS_ELEMENT_VARIABLES = "variables";
	

}
