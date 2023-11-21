package com.nosliw.ui.entity.uicontent;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;


@HAPEntityWithAttribute
public class HAPExecutableEntityComplexUIContent extends  HAPExecutableEntityComplex{

	@HAPAttribute
	static final public String HTML = "html";

	@HAPAttribute
	static final public String CUSTOMERTAG = "customerTag";  
	
	@HAPAttribute
	static final public String SCRIPTEXPRESSIONINCONTENT = "scriptExpressionInContent";
	
	@HAPAttribute
	static final public String SCRIPTEXPRESSIONINATTRIBUTE = "scriptExpressionInAttribute";
	
	@HAPAttribute
	static final public String SCRIPTEXPRESSIONINTAGATTRIBUTE = "scriptExpressionInTagAttribute";

	@HAPAttribute
	static final public String NORMALTAGEVENT = "normalTagEvent";  
	
	@HAPAttribute
	static final public String CUSTOMTAGEVENT = "customTagEvent";  
	
	@HAPAttribute
	static final public String SCRIPT = "script";  
	
	@HAPAttribute
	static final public String SERVICE = "service";  
	

	public HAPExecutableEntityComplexUIContent() {
		this.setAttributeValueObject(HAPExecutableEntityComplexUIContent.SCRIPTEXPRESSIONINTAGATTRIBUTE, new ArrayList<HAPUIEmbededScriptExpressionInAttribute>());
		this.setAttributeValueObject(HAPExecutableEntityComplexUIContent.SCRIPTEXPRESSIONINATTRIBUTE, new ArrayList<HAPUIEmbededScriptExpressionInAttribute>());
		this.setAttributeValueObject(HAPExecutableEntityComplexUIContent.SCRIPTEXPRESSIONINCONTENT, new ArrayList<HAPUIEmbededScriptExpressionInContent>());
		this.setAttributeValueObject(HAPExecutableEntityComplexUIContent.NORMALTAGEVENT, new ArrayList<HAPElementEvent>());
		this.setAttributeValueObject(HAPExecutableEntityComplexUIContent.CUSTOMTAGEVENT, new ArrayList<HAPElementEvent>());
	}
	
	public void setHTML(String content) {    this.setAttributeValueObject(HTML, content);        }
	public String getHTML() {     return (String)this.getAttributeValue(HTML);     }
		
	public void addScriptExpressionInCustomTagAttribute(HAPUIEmbededScriptExpressionInAttribute scriptExpression) {  getScriptExpressionInCustomTagAttributes().add(scriptExpression);    }
	public void addScriptExpressionInNormalTagAttribute(HAPUIEmbededScriptExpressionInAttribute scriptExpression) {  getScriptExpressionInNormalTagAttributes().add(scriptExpression);    }
	public void addScriptExpressionInContent(HAPUIEmbededScriptExpressionInContent scriptExpression) {  getScriptExpressionInContents().add(scriptExpression);    }
	 
	public List<HAPUIEmbededScriptExpressionInAttribute> getScriptExpressionInCustomTagAttributes() {  return (List<HAPUIEmbededScriptExpressionInAttribute>)this.getAttributeValue(HAPExecutableEntityComplexUIContent.SCRIPTEXPRESSIONINTAGATTRIBUTE);    }
	public List<HAPUIEmbededScriptExpressionInAttribute> getScriptExpressionInNormalTagAttributes() {  return (List<HAPUIEmbededScriptExpressionInAttribute>)this.getAttributeValue(HAPExecutableEntityComplexUIContent.SCRIPTEXPRESSIONINATTRIBUTE);    }
	public List<HAPUIEmbededScriptExpressionInContent> getScriptExpressionInContents() {  return (List<HAPUIEmbededScriptExpressionInContent>)this.getAttributeValue(HAPExecutableEntityComplexUIContent.SCRIPTEXPRESSIONINCONTENT);    }

	public void addNormalTagEvent(HAPElementEvent event) {   this.getNormalTagEvents().add(event);    }
	public void addCustomTagEvent(HAPElementEvent event) {   this.getCustomTagEvent().add(event);    }
	
	public List<HAPElementEvent> getNormalTagEvents(){    return (List<HAPElementEvent>)this.getAttributeValue(HAPExecutableEntityComplexUIContent.NORMALTAGEVENT);     }
	public List<HAPElementEvent> getCustomTagEvent(){    return (List<HAPElementEvent>)this.getAttributeValue(HAPExecutableEntityComplexUIContent.CUSTOMTAGEVENT);     }

}
