package com.nosliw.uiresource.page.execute;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPFileUtility;
import com.steadystate.css.parser.CSSOMParser;

@HAPEntityWithAttribute
public class HAPExecutableStyle extends HAPSerializableImp{

	@HAPAttribute
	public static final String ID = "id";

	@HAPAttribute
	public static final String DEFINITION = "definition";

	@HAPAttribute
	public static final String CHILDREN = "children";

	private String m_id;
	private String m_definition;
	private List<HAPExecutableStyle> m_children;
	private String m_script;

	public HAPExecutableStyle(String id) {
		this.m_children = new ArrayList<HAPExecutableStyle>();
		this.m_id = id;
	}
	
	public String getId() {   return this.m_id;    }
	
	public String getDefinition() {   return this.m_definition;   }
	public void setDefinition(String def) {    this.m_definition = def;     }

	public void addChild(HAPExecutableStyle child) {   this.m_children.add(child);   }
	
	public String getScript() {
		if(this.m_script==null) {
			this.m_script = this.buildStyleScript(null);
		}
		return this.m_script;
	}
	
	private String buildSelector(String id) {
		
		return "["+HAPConstantShared.UIRESOURCE_ATTRIBUTE_STATICID+"='"+id+"']";
	}

	protected String buildStyleScript(List<String> parentIds) {
		StringBuffer out = new StringBuffer();
		try {
	         if(parentIds==null)  parentIds = new ArrayList<String>();
			if(HAPBasicUtility.isStringNotEmpty(this.m_definition)) {
				
		         StringBuffer parentSelectors = new StringBuffer();
//		         for(String parentId : parentIds) {
//		        	 parentSelectors.append(this.buildSelector(parentId)+" ");
//		         }
		         
		         {
		        	 //children version
			         // parse and create a stylesheet composition
					 InputSource source = new InputSource(new StringReader(this.m_definition));
			         CSSOMParser parser = new CSSOMParser();
			         CSSStyleSheet stylesheet = parser.parseStyleSheet(source, null, null);
			         CSSRuleList ruleList = stylesheet.getCssRules();
			         for (int i = 0; i < ruleList.getLength(); i++) 
			         {
			           CSSRule rule = ruleList.item(i);
			           if (rule instanceof CSSStyleRule) 
			           {
			               CSSStyleRule styleRule=(CSSStyleRule)rule;
			               String selectorText = styleRule.getSelectorText();
			               styleRule.setSelectorText(parentSelectors.toString() + this.buildSelector(this.m_id) + " " + selectorText);
			            }
			          }
			          out.append(stylesheet.toString());
//			          out.append("\n");
		         }

		         {
		        	 //root version
			         // parse and create a stylesheet composition
					 InputSource source = new InputSource(new StringReader(this.m_definition));
			         CSSOMParser parser = new CSSOMParser();
			         CSSStyleSheet stylesheet = parser.parseStyleSheet(source, null, null);
			         CSSRuleList ruleList = stylesheet.getCssRules();
			         for (int i = 0; i < ruleList.getLength(); i++) 
			         {
			           CSSRule rule = ruleList.item(i);
			           if (rule instanceof CSSStyleRule) 
			           {
			               CSSStyleRule styleRule=(CSSStyleRule)rule;
			               String selectorText = styleRule.getSelectorText();
			               styleRule.setSelectorText(parentSelectors.toString() + this.buildSelector(this.m_id) + "" + selectorText);
			            }
			          }
			          out.append(stylesheet.toString());
//			          out.append("\n");
		         }
			}

	          List<String> ids = new ArrayList<String>(parentIds);
	          ids.add(this.m_id);
     	 		for(HAPExecutableStyle child : this.m_children) {
					out.append(child.buildStyleScript(ids));
//			          out.append("\n");
				}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	
		return out.toString();
	}

	public static void main(String[] args) throws IOException {
		
		 InputSource source = new InputSource(new InputStreamReader(HAPFileUtility.getInputStreamOnClassPath(HAPExecutableStyle.class, "style.css"), "UTF-8"));
         CSSOMParser parser = new CSSOMParser();
         // parse and create a stylesheet composition
         CSSStyleSheet stylesheet = parser.parseStyleSheet(source, null, null);
		
         CSSRuleList ruleList = stylesheet.getCssRules();
         for (int i = 0; i < ruleList.getLength(); i++) 
         {
           CSSRule rule = ruleList.item(i);
           if (rule instanceof CSSStyleRule) 
           {
               CSSStyleRule styleRule=(CSSStyleRule)rule;
               String selectorText = styleRule.getSelectorText();
               styleRule.setSelectorText("nosliw_wrapper[nosliwdefid=\"red8\"] " + selectorText);
//               ps.println("selector:" + i + ": " + styleRule.getSelectorText());
               CSSStyleDeclaration styleDeclaration = styleRule.getStyle();

            }// end of StyleRule instance test
          } // end of ruleList loop

         System.out.println(stylesheet);
         
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, HAPJsonUtility.buildJson(this.m_id, HAPSerializationFormat.JSON));
		jsonMap.put(DEFINITION, HAPJsonUtility.buildJson(StringEscapeUtils.escapeHtml(this.getScript()), HAPSerializationFormat.JSON).replaceAll("(\\r|\\n)", ""));
	}
}
