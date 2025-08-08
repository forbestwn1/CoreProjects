package com.nosliw.data.imp.expression.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionDataExpression;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperand;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandAttribute;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandConstant;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandOperation;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandReference;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandVariable;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionParmInOperationOperand;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.data.imp.expression.parser.generated.NosliwExpressionParser;
import com.nosliw.data.imp.expression.parser.generated.SimpleNode;

/**
 * This is utility class to parse a expression as a string
 * The result of parsing is operand structure 
 */
@Component
public class HAPDataExpressionParserImp implements HAPParserDataExpression{
	
	public HAPDataExpressionParserImp(){
	}
	
	@Override
	public HAPDefinitionDataExpression parseExpression(String expression){
		  SimpleNode root = null;
		  try{
			  InputStream is = new ByteArrayInputStream(expression.getBytes());
	          NosliwExpressionParser parser = new NosliwExpressionParser( is ) ;
	          root = parser.Expression("");
		  }
		  catch(Throwable e){
			  e.printStackTrace();
			  System.out.println(expression);
			  return null;
		  }
          HAPDefinitionOperand operand = processExpressionNode(root);
          return new HAPDefinitionDataExpression(operand);
	  }
	  
	  private HAPDefinitionOperand processExpressionNode(SimpleNode parentNode){
		  HAPDefinitionOperand out = null;
		  
		  ExpressionElements expressionEles = getExpressionElements(parentNode);

		  HAPDefinitionOperand operand = null;
		  if(expressionEles.constantNode!=null){
			//it is a constant operand  
			 operand = new HAPDefinitionOperandConstant(HAPExpressionEscape.deescape(getName(expressionEles.constantNode, NosliwExpressionParser.JJTCONSTANTNAME)));
		  }
		  else if(expressionEles.variableNode!=null){
			  //it is a variable operand
			 operand = new HAPDefinitionOperandVariable(getName(expressionEles.variableNode, NosliwExpressionParser.JJTVAIRALBENAME));
		  }
		  else if(expressionEles.referenceNode!=null){
			  //it is a reference operand
			 operand = new HAPDefinitionOperandReference(getName(expressionEles.referenceNode, NosliwExpressionParser.JJTREFERENCENAME));
		  }
		  else if(expressionEles.dataTypeNode!=null){
			  String dataTypeInfo = getName(expressionEles.dataTypeNode, NosliwExpressionParser.JJTDATATYPENAME);
			  String operation = (String)expressionEles.nameNode.jjtGetValue();
			  operand = new HAPDefinitionOperandOperation(dataTypeInfo, operation, getOperationParms(expressionEles.expressionNodes));
		  }
		  
		  out = processExpression1Node(expressionEles.expression1Node, operand);
		  return out;
	  }

	  
	  private String getName(SimpleNode node, int nameId){
		  String out = null;
		  int childNum = node.jjtGetNumChildren();
		  for(int i=0; i<childNum; i++){
			  SimpleNode childNode = (SimpleNode)node.jjtGetChild(i);
			  if(childNode.getId()==nameId){
				  out = (String)childNode.jjtGetValue();
				  break;
			  }
		  }
		  return out;
	  }
	  
	  private HAPDefinitionOperand processExpression1Node(SimpleNode parentNode, HAPDefinitionOperand aheadOperand){
		  if(isNodeEmpty(parentNode)) {
			return aheadOperand;
		}

		  HAPDefinitionOperand out = null;
		  ExpressionElements expressionEles = getExpressionElements(parentNode);
		  String name = (String)expressionEles.nameNode.jjtGetValue();
		  HAPDefinitionOperand operand = null;
		  if("function".equals(parentNode.jjtGetValue())){
			  //function call
			  if(aheadOperand!=null && HAPUtilityBasic.isEquals(aheadOperand.getType(), HAPConstantShared.EXPRESSION_OPERAND_REFERENCE) && HAPUtilityBasic.isEquals(name, "with")) {
				  //reference
				  for(Parm parm : expressionEles.expressionNodes){
					  ((HAPDefinitionOperandReference)aheadOperand).addMapping(parm.name, processExpressionNode(parm.valuNode));
				  }
				  operand = aheadOperand;
			  }
			  else {
				  //operation
				  operand = new HAPDefinitionOperandOperation(aheadOperand, name, getOperationParms(expressionEles.expressionNodes));
			  }
		  }
		  else{
			  //path
			  operand = new HAPDefinitionOperandAttribute(aheadOperand, name);
		  }
		  out = processExpression1Node(expressionEles.expression1Node, operand);
		  return out;
	  }

	  private List<HAPDefinitionParmInOperationOperand> getOperationParms(List<Parm> expressionParms){
		  List<HAPDefinitionParmInOperationOperand> out = new ArrayList<HAPDefinitionParmInOperationOperand>();
		  for(Parm parm : expressionParms){
			  HAPDefinitionOperand op = processExpressionNode(parm.valuNode);
			  out.add(new HAPDefinitionParmInOperationOperand(parm.name, op));
		  }
		  return out;
	  }
	  
	  private ExpressionElements getExpressionElements(SimpleNode parentNode){
		  ExpressionElements out = new ExpressionElements();
		  
		  for(int i=0; i<parentNode.jjtGetNumChildren(); i++){
			  SimpleNode childNode = (SimpleNode)parentNode.jjtGetChild(i);
			  switch(childNode.getId()){
			  case NosliwExpressionParser.JJTCONSTANT:
			  {
				  out.constantNode = childNode;
				  break;
			  }
			  case NosliwExpressionParser.JJTVARIABLE:
			  {
				  out.variableNode = childNode;
				  break;
			  }
			  case NosliwExpressionParser.JJTREFERENCE:
			  {
				  out.referenceNode = childNode;
				  break;
			  }
			  case NosliwExpressionParser.JJTNAME:
			  {
				  out.nameNode =childNode;
				  break;
			  }
			  case NosliwExpressionParser.JJTDATATYPE:
			  {
				  out.dataTypeNode =childNode;
				  break;
			  }
			  case NosliwExpressionParser.JJTEXPRESSION1:
			  {
				  out.expression1Node = childNode;
				  break;
			  }
			  case NosliwExpressionParser.JJTPARAMETER:
			  {
				  out.expressionNodes.add(processParm(childNode));
				  break;
			  }
			  
			  }
		  }
		  return out;
	  }
	  
	  private static Parm processParm(SimpleNode node){
		  Parm out = new Parm();
		  for(int i=0; i<node.jjtGetNumChildren(); i++){
			  SimpleNode childNode = (SimpleNode)node.jjtGetChild(i);
			  switch(childNode.getId()){
			  case NosliwExpressionParser.JJTPARMNAME:
			  {
				  out.name = (String)childNode.jjtGetValue();
				  break;
			  }
			  case NosliwExpressionParser.JJTEXPRESSION:
			  {
				  out.valuNode = childNode;
				  break;
			  }
			  }
		  }
		  return out;
	  }
	  
	  private static boolean isNodeEmpty(SimpleNode node){
		  if(node.jjtGetNumChildren()==0) {
			return true;
		}
		  return false;
	  } 
	  
}

class ExpressionElements{
	  SimpleNode constantNode;
	  SimpleNode variableNode;
	  SimpleNode referenceNode;
	  SimpleNode nameNode;
	  SimpleNode dataTypeNode;
	  List<Parm> expressionNodes = new ArrayList<Parm>();
	  SimpleNode expression1Node;
}


class Parm{
	String name;
	SimpleNode valuNode;
}
