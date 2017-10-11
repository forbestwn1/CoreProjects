package com.nosliw.uiresource.expression;

import java.util.HashSet;
import java.util.Set;

/**
 * Segment in script expression for in-line script 
 * it may also contains constants and variables in it
 */
public class HAPScriptExpressionScriptSegment {

	static final private String TOKEN_CONSTANT_START = "&(";
	static final private String TOKEN_CONSTANT_END= ")&";
	
	static final private String TOKEN_VARIABLE_START = "?(";
	static final private String TOKEN_VARIABLE_END = "?(";

	static final private String TEMPLATE_CONSTANT = "constants.element";
	static final private String TEMPLATE_VARIABLE = "variables.element";
	
	private String m_orignalScript;
	
	//constants in script
	private Set<String> m_constants;
	
	//variables in script
	private Set<String> m_variables;
	
	//script after processing
	private String m_script;
	
	public HAPScriptExpressionScriptSegment(String script){
		this.m_constants = new HashSet<String>();
		this.m_variables = new HashSet<String>();
		this.m_orignalScript = script;
		this.m_script = this.process();
	}
	
	public String getScript(){ return this.m_script;  }
	public Set<String> getConstants(){  return this.m_constants;  }
	public Set<String> getVariables(){  return this.m_variables;  } 
	
	//process script
	//majorly discover constants and variables
	//then replace placeholder for constants and variables with variable name
	//for instance: constants.size, variables.age, ...
	private String process(){
		String script = this.m_orignalScript;
		script = this.process(script, TOKEN_CONSTANT_START, TOKEN_CONSTANT_END, TEMPLATE_CONSTANT, m_constants);
		script = this.process(script, TOKEN_VARIABLE_START, TOKEN_VARIABLE_END, TEMPLATE_VARIABLE, m_variables);
		return script;
	}
	
	private String process(String script, String startToken, String endToken, String template, Set<String> elements){
		String out = script;
		int start = out.indexOf(startToken);
		while(start!=-1){
			int end = out.indexOf(endToken, start);
			String element = out.substring(start+startToken.length(), end);
			elements.add(element);
			String replace = template.replaceAll("element", element);
			out = out.substring(0, start) + replace + out.substring(end+endToken.length());
			start = out.indexOf(startToken);
		}
		return out;
	}
	
}
