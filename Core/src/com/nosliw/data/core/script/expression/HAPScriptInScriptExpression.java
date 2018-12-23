package com.nosliw.data.core.script.expression;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.updatename.HAPEntityWithName;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPBasicUtility;

/**
 * Segment in script expression for in-line script 
 * it may also contains constants and variables in it
 */
public class HAPScriptInScriptExpression implements HAPEntityWithName{

	private String m_orignalScript;

	//store segment elements
	//   string
	//   script constant
	//   script variable
	private List<Object> m_elements;

	//define the segment parsing infor
	private Object[][] m_definitions = {
			{"&(", ")&", HAPConstantInScript.class}, 
			{"?(", ")?", HAPVariableInScript.class}
	};
	

	public HAPScriptInScriptExpression(String script){
		this.m_elements = new ArrayList<Object>();
		this.m_orignalScript = script;
		this.processSegments();
	}
	
	public List<Object> getElements(){	return this.m_elements;	}
	
	@Override
	public Set<String> getConstantNames(){
		Set<String> out = new HashSet<String>();
		for(Object ele : this.m_elements) {
			if(ele instanceof HAPConstantInScript) {
				out.add(((HAPConstantInScript)ele).getConstantName());
			}
		}
		return out;  
	}
	
	@Override
	public Set<String> getVariableNames(){
		Set<String> out = new HashSet<String>();
		for(Object ele : this.m_elements) {
			if(ele instanceof HAPVariableInScript) {
				out.add(((HAPVariableInScript)ele).getVariableName());
			}
		}
		return out;  
	}
	
	@Override
	public void updateVariableNames(HAPUpdateName nameUpdate) {
		for(Object ele : this.m_elements) {
			if(ele instanceof HAPVariableInScript) {
				HAPVariableInScript varEle = (HAPVariableInScript)ele;
				varEle.updateName(nameUpdate);
			}
		}
	}

	@Override
	public void updateConstantNames(HAPUpdateName nameUpdate) {
		for(Object ele : this.m_elements) {
			if(ele instanceof HAPConstantInScript) {
				HAPConstantInScript constantEle = (HAPConstantInScript)ele;
				constantEle.updateName(nameUpdate);
			}
		}
	}

	public void updateConstantValue(Map<String, Object> constantsValue) {
		List<Object> newEles = new ArrayList<Object>();
		for(Object ele : this.m_elements) {
			if(ele instanceof HAPVariableInScript) {
				HAPVariableInScript varEle = (HAPVariableInScript)ele;
				Object constantValue = constantsValue.get(varEle.getVariableName());
				if(constantValue!=null) {
					HAPConstantInScript constantEle = new HAPConstantInScript(varEle.getVariableName());
					constantEle.setValue(constantValue);
				}
				else {
					newEles.add(ele);
				}
			}
			else if(ele instanceof HAPConstantInScript) {
				HAPConstantInScript constantEle = (HAPConstantInScript)ele;
				constantEle.setValue(constantsValue.get(constantEle.getConstantName()));
				newEles.add(constantEle);
			}
			else {
				newEles.add(ele);
			}
		}
		this.m_elements = newEles;
	}
	
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
