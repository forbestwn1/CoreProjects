package com.nosliw.data.imp.expression.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.expression.data1.HAPParserDataExpression;
import com.nosliw.data.core.operand.HAPOperand;
import com.nosliw.data.core.operand.HAPOperandAttribute;
import com.nosliw.data.core.operand.HAPOperandConstant;
import com.nosliw.data.core.operand.HAPOperandOperation;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPOperandVariable;
import com.nosliw.data.core.operand.HAPParmInOperationOperand;
import com.nosliw.data.imp.expression.parser.generated.NosliwExpressionParser;
import com.nosliw.data.imp.expression.parser.generated.SimpleNode;

/**
 * This is utility class to parse a expression as a string
 * The result of parsing is operand structure 
 */
public class HAPDataExpressionParserImp implements HAPParserDataExpression{
	
	public HAPDataExpressionParserImp(){
	}
	
	  @Override
	public HAPOperand parseExpression(String expression){
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
          return processExpressionNode(root);
	  }
	  
	  private HAPOperand processExpressionNode(SimpleNode parentNode){
		  HAPOperand out = null;
		  
		  ExpressionElements expressionEles = getExpressionElements(parentNode);

		  HAPOperand operand = null;
		  if(expressionEles.constantNode!=null){
			//it is a constant operand  
			 operand = new HAPOperandConstant(HAPExpressionEscape.deescape(getName(expressionEles.constantNode, NosliwExpressionParser.JJTCONSTANTNAME)));
		  }
		  else if(expressionEles.variableNode!=null){
			  //it is a variable operand
			 operand = new HAPOperandVariable(getName(expressionEles.variableNode, NosliwExpressionParser.JJTVAIRALBENAME));
		  }
		  else if(expressionEles.referenceNode!=null){
			  //it is a reference operand
			 operand = new HAPOperandReference(getName(expressionEles.referenceNode, NosliwExpressionParser.JJTREFERENCENAME));
		  }
		  else if(expressionEles.dataTypeNode!=null){
			  String dataTypeInfo = getName(expressionEles.dataTypeNode, NosliwExpressionParser.JJTDATATYPENAME);
			  String operation = (String)expressionEles.nameNode.jjtGetValue();
			  operand = new HAPOperandOperation(dataTypeInfo, operation, getOperationParms(expressionEles.expressionNodes));
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
	  
	  private HAPOperand processExpression1Node(SimpleNode parentNode, HAPOperand aheadOperand){
		  if(isNodeEmpty(parentNode))  return aheadOperand;

		  HAPOperand out = null;
		  ExpressionElements expressionEles = getExpressionElements(parentNode);
		  String name = (String)expressionEles.nameNode.jjtGetValue();
		  HAPOperand operand = null;
		  if("function".equals(parentNode.jjtGetValue())){
			  //function call
			  if(aheadOperand!=null && HAPUtilityBasic.isEquals(aheadOperand.getType(), HAPConstantShared.EXPRESSION_OPERAND_REFERENCE) && HAPUtilityBasic.isEquals(name, "with")) {
				  //reference
				  for(Parm parm : expressionEles.expressionNodes){
					  ((HAPOperandReference)aheadOperand).addMapping(parm.name, processExpressionNode(parm.valuNode));
				  }
				  operand = aheadOperand;
			  }
			  else {
				  //operation
				  operand = new HAPOperandOperation(aheadOperand, name, getOperationParms(expressionEles.expressionNodes));
			  }
		  }
		  else{
			  //path
			  operand = new HAPOperandAttribute(aheadOperand, name);
		  }
		  out = processExpression1Node(expressionEles.expression1Node, operand);
		  return out;
	  }

	  private List<HAPParmInOperationOperand> getOperationParms(List<Parm> expressionParms){
		  List<HAPParmInOperationOperand> out = new ArrayList<HAPParmInOperationOperand>();
		  for(Parm parm : expressionParms){
			  HAPOperand op = processExpressionNode(parm.valuNode);
			  out.add(new HAPParmInOperationOperand(parm.name, op));
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
		  if(node.jjtGetNumChildren()==0)   return true;
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
