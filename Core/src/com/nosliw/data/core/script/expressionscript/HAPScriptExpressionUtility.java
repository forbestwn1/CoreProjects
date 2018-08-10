package com.nosliw.data.core.script.expressionscript;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.operand.HAPOperand;
import com.nosliw.data.core.operand.HAPOperandAttribute;
import com.nosliw.data.core.operand.HAPOperandConstant;
import com.nosliw.data.core.operand.HAPOperandOperation;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandVariable;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.operand.HAPParmInOperationOperand;

public class HAPScriptExpressionUtility {

	public static final String UIEXPRESSION_TOKEN_OPEN = "<%=";
	public static final String UIEXPRESSION_TOKEN_CLOSE = "%>";
	
	/**
	 * parse text to discover script expression in it
	 * @param text
	 * @param idGenerator
	 * @param expressionMan
	 * @return a list of text and ui expression object
	 */
	public static List<Object> discoverUIExpressionInText(
			String text, 
			HAPExpressionSuiteManager expressionMan){
		List<Object> out = new ArrayList<Object>();
		int i = 0;
		
		int start = text.indexOf(UIEXPRESSION_TOKEN_OPEN);
		while(start != -1){
			if(start>0)   out.add(text.substring(0, start));
			int expEnd = text.indexOf(UIEXPRESSION_TOKEN_CLOSE, start);
			int end = expEnd + UIEXPRESSION_TOKEN_CLOSE.length();
			String expression = text.substring(start+UIEXPRESSION_TOKEN_OPEN.length(), expEnd);
			HAPScriptExpression uiExpression = new HAPScriptExpression(i+"", expression, expressionMan);
			out.add(uiExpression);
			//keep searching the rest
			text=text.substring(end);
			start = text.indexOf(UIEXPRESSION_TOKEN_OPEN);
			i++;
		}
		if(!HAPBasicUtility.isStringEmpty(text)){
			out.add(text);
		}
		return out;
	}

	//process variables in expression, 
	//	for attribute operation a.b.c.d which have responding definition in context, 
	//			replace attribute operation with one variable operation
	//  for attribute operation a.b.c.d which have responding defintion a.b.c in context, 
	//			replace attribute operation with one variable operation(a.b.c) and getChild operation
	public static void processAttributeOperandInExpression(HAPDefinitionExpression expressionDefinition, final Map<String, HAPVariableInfo> varsInfo){
		processAttributeOperandInExpressionOperand(expressionDefinition.getOperand(), varsInfo);
	}
	
	private static void processAttributeOperandInExpressionOperand(HAPOperandWrapper operand, final Map<String, HAPVariableInfo> varCriterias){
		List<HAPAttributeOperandChainInfo> data = new ArrayList<HAPAttributeOperandChainInfo>();
		HAPOperandUtility.processAllOperand(operand, data, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				List<HAPAttributeOperandChainInfo> stack = (List<HAPAttributeOperandChainInfo>)data;
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_ATTRIBUTEOPERATION)){
					HAPOperandAttribute attrOperand = (HAPOperandAttribute)operand.getOperand();
					HAPAttributeOperandChainInfo attrInfo = null;
					if(stack.size()==0 || !stack.get(stack.size()-1).open){
						//new attrInfo
						attrInfo = new HAPAttributeOperandChainInfo();
						attrInfo.lastAttrOperand = operand;
						stack.add(attrInfo);
					}
					else{
						//use latest
						attrInfo = stack.get(stack.size()-1);
					}
					attrInfo.insertSegment(attrOperand.getAttribute());
				}
				else{
					if(stack.size()>=1 && stack.get(stack.size()-1).open){
						HAPAttributeOperandChainInfo attrInfo = stack.get(stack.size()-1);
						attrInfo.startOperand = operand;
						attrInfo.open = false;
					}
				}
				return true;
			}
			
			@Override
			public void postPross(HAPOperandWrapper operand, Object data){
				List<HAPAttributeOperandChainInfo> stack = (List<HAPAttributeOperandChainInfo>)data;
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_ATTRIBUTEOPERATION)){
					HAPOperandAttribute attrOperand = (HAPOperandAttribute)operand.getOperand();
					HAPAttributeOperandChainInfo attrInfo = stack.get(stack.size()-1);
					attrInfo.veryPath.add(attrOperand.getAttribute());
					if(attrInfo.path.size()==attrInfo.veryPath.size()){
						//at end of attribute chain
						processAttributeChain(attrInfo, varCriterias);
						stack.remove(stack.size()-1);
					}
				}
				else{
					if(stack.size()>=1 && !stack.get(stack.size()-1).open && stack.get(stack.size()-1).startOperand==operand){
						//find start operand
						stack.get(stack.size()-1).open = true;
					}
					
//					if(stack.size()>=1 && stack.get(stack.size()-1).open){
//						HAPAttributeOperandChainInfo attrInfo = stack.get(stack.size()-1);
//						attrInfo.startOperand = operand;
//						attrInfo.open = false;
//					}
				}
			}
		});
	}
	
	private static void processAttributeChain(HAPAttributeOperandChainInfo attrInfo, Map<String, HAPVariableInfo> contextVars){
		String startOpType = attrInfo.startOperand.getOperand().getType();
		if(startOpType.equals(HAPConstant.EXPRESSION_OPERAND_VARIABLE)){
			//attribute start with variable
			HAPOperandVariable varOperand = (HAPOperandVariable)attrInfo.startOperand.getOperand();
			attrInfo.path.add(0, varOperand.getVariableName());
			String path = HAPNamingConversionUtility.cascadePath(attrInfo.path.toArray(new String[0]));
			for(String name : contextVars.keySet()){
				if(path.equals(name)){
					//replace with variable operand
					attrInfo.lastAttrOperand.setOperand(new HAPOperandVariable(name));
					break;
				}
				else if(path.startsWith(name+".")){
					//replace with variable operand.child
					HAPOperand operand = new HAPOperandVariable(name);
					String[] pathSegs = HAPNamingConversionUtility.parsePaths(path.substring(name.length()+1));
					for(String pathSeg : pathSegs){
						List<HAPParmInOperationOperand> parms = new ArrayList<HAPParmInOperationOperand>();
						parms.add(new HAPParmInOperationOperand(HAPConstant.DATAOPERATION_COMPLEX_GETCHILDDATA_NAME, new HAPOperandConstant("#string:simple:"+pathSeg)));
						operand = new HAPOperandOperation(operand, HAPConstant.DATAOPERATION_COMPLEX_GETCHILDDATA, parms);
					}
					attrInfo.lastAttrOperand.setOperand(operand);
					break;
				}
			}
		}
	}
	
}

class HAPAttributeOperandChainInfo{
	public HAPOperandWrapper startOperand;
	
	public HAPOperandWrapper lastAttrOperand;
	
	public List<String> path;
	
	public List<String> veryPath;
	
	public boolean open;
	
	public HAPAttributeOperandChainInfo(){
		this.path = new ArrayList<String>();
		this.veryPath = new ArrayList<String>();
		this.open = true;
	}
	
	public void insertSegment(String seg){
		path.add(0, seg);
	}
}
