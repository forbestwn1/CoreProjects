package com.nosliw.data.core.script.expressionscript;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPBasicUtility;

/**
 * Segment in script expression for in-line script 
 * it may also contains constants and variables in it
 */
public class HAPScriptExpressionScriptSegment {

	private String m_orignalScript;

	//store segment elements
	//   string
	//   script constant
	//   script variable
	private List<Object> m_elements;

	private List<String> m_constants = new ArrayList<String>();
	
	private List<String> m_variableNames = new ArrayList<String>();
	
	//define the segment parsing infor
	private Object[][] m_definitions = {
			{"&(", ")&", HAPScriptExpressionScriptConstant.class, m_constants}, 
			{"?(", ")?", HAPScriptExpressionScriptVariable.class, m_variableNames}
	};
	

	public HAPScriptExpressionScriptSegment(String script){
		this.m_elements = new ArrayList<Object>();
		this.m_orignalScript = script;
		this.processSegments();
	}
	
	public List<Object> getElements(){	return this.m_elements;	}
	
	public List<String> getConstantNames(){  return this.m_constants;  }
	public List<String> getVariableNames(){  return this.m_variableNames;  }
	
	public void updateVariables(Map<String, String>) {}
	
	private void processSegments(){
		try{
			String content = this.m_orignalScript;
			int[] indexs = this.index(content);
			while(indexs[0]!=-1){
				int type = indexs[1];
				String startToken = (String)m_definitions[type][0];
				String endToken = (String)m_definitions[type][1];
				int startIndex = indexs[0];
				if(startIndex>0){
					this.m_elements.add(content.substring(0, startIndex));
				}
				int endIndex = content.indexOf(endToken);
				Class cs = (Class)m_definitions[type][2];
				Constructor cons = cs.getConstructor(String.class);
				String name = content.substring(startIndex+startToken.length(), endIndex);
				this.m_elements.add(cons.newInstance(name));
				((List<String>)this.m_definitions[type][3]).add(name);
				content = content.substring(endIndex+endToken.length());
				indexs = this.index(content);
			}
			if(HAPBasicUtility.isStringNotEmpty(content)){
				this.m_elements.add(content);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private int[] index(String content){
		int invalidValue = 99999;
		int[] indexs = new int[2];
		int currentIndex = invalidValue;
		int currentType = invalidValue;
		for(int i=0; i<m_definitions.length; i++){
			int index = content.indexOf((String)m_definitions[i][0]);
			if(index==-1)  continue;
			else if(index < currentIndex){
				currentIndex = index;
				currentType = i;
			}
		}
		if(currentIndex==invalidValue){
			indexs[0] = -1;
			indexs[1] = -1;
		}
		else{
			indexs[0] = currentIndex;
			indexs[1] = currentType;
		}
		return indexs;
	}
}
