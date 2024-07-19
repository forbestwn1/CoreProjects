package com.nosliw.core.application.brick.ui.uicontent;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.common.scriptexpression.HAPContainerScriptExpression;

@HAPEntityWithAttribute
public interface HAPBlockComplexUIContent extends HAPBrick{

	@HAPAttribute
	static final public String HTML = "html";

	@HAPAttribute
	static final public String SCRIPTEXPRESSIONINCONTENT = "scriptExpressionInContent";

	@HAPAttribute
	static final public String SCRIPTEXPRESSIONS = "scriptExpressions";


	String getHtml();
	
	List<HAPUIEmbededScriptExpressionInContent> getScriptExpressionInContent();
	
	HAPContainerScriptExpression getScriptExpressions();


	
	
	
	@HAPAttribute
	static final public String CUSTOMERTAG = "customerTag";  
	
	@HAPAttribute
	static final public String SCRIPTEXPRESSIONINATTRIBUTE = "scriptExpressionInAttribute";
	
	@HAPAttribute
	static final public String SCRIPTEXPRESSIONINTAGATTRIBUTE = "scriptExpressionInTagAttribute";

	@HAPAttribute
	static final public String NORMALTAGEVENT = "normalTagEvent";  
	
	@HAPAttribute
	static final public String CUSTOMTAGEVENT = "customTagEvent";  
	
	@HAPAttribute
	static final public String SCRIPT = "scripttaskgroup";  
	
	@HAPAttribute
	static final public String SERVICE = "service";  
	
}
