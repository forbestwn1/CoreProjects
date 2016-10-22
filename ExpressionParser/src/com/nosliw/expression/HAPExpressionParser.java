package com.nosliw.expression;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data1.HAPDataTypeInfo;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.data1.HAPOperand;
import com.nosliw.expression.parser.NosliwExpressionParser;
import com.nosliw.expression.parser.ParseException;
import com.nosliw.expression.parser.SimpleNode;
import com.nosliw.expression.parser.TokenMgrError;

public class HAPExpressionParser {

	  public static void main(String args[]) throws ParseException, TokenMgrError {
		  
//		  String str = "!(this:simple)!.fun1(?(bb)?.fun2(:(dd):),:(cc):.fun3())";
		  
//		  String str = "?(key)?.largerThan(&(dddd)&,&(dddd)&)";
		  
		  String str = "?(schoolsData)?.each(?(validHomeExpression)?,&(schoolData)&,&(out)&)";
		  
		  InputStream is = new ByteArrayInputStream(str.getBytes());
		  
          NosliwExpressionParser parser = new NosliwExpressionParser( is ) ;
          SimpleNode root = parser.Expression("");
          root.dump("");
	  }

	  public static HAPOperand parseExpression(String expression, HAPDataTypeManager dataTypeMan){
		  SimpleNode root = null;
		  try{
			  InputStream is = new ByteArrayInputStream(expression.getBytes());
	          NosliwExpressionParser parser = new NosliwExpressionParser( is ) ;
	          root = parser.Expression("");
		  }
		  catch(Exception e){
			  e.printStackTrace();
			  return null;
		  }
          return processExpressionNode(root, dataTypeMan);
	  }
	  
	  private static HAPOperand processExpressionNode(SimpleNode parentNode, HAPDataTypeManager dataTypeMan){
		  HAPOperand out = null;
		  
		  ExpressionElements expressionEles = getExpressionElements(parentNode);

		  HAPOperand operand = null;
		  if(expressionEles.constantNode!=null){
			//it is a constant operand  
			 operand = new HAPOperandConstant((String)expressionEles.constantNode.jjtGetValue(), dataTypeMan);
		  }
		  else if(expressionEles.variableNode!=null){
			  //it is a variable operand
			 operand = new HAPOperandVariable(((String)expressionEles.variableNode.jjtGetValue()), dataTypeMan);
		  }
		  else if(expressionEles.dataTypeNode!=null){
			  String dataTypeInfo = (String)expressionEles.dataTypeNode.jjtGetValue();
			  String operation = (String)expressionEles.nameNode.jjtGetValue();
			  if(HAPConstant.DATAOPERATION_NEWDATA.equals(operation)){
				  //new operation
				  operand = new HAPOperandNewOperation(HAPDataTypeInfo.parseDataTypeInfo(dataTypeInfo), getOperationParms(expressionEles.expressionNodes, dataTypeMan), dataTypeMan);
			  }
			  else{
				  //normal data type operation
				  operand = new HAPOperandDataTypeOperation(HAPDataTypeInfo.parseDataTypeInfo(dataTypeInfo), operation, getOperationParms(expressionEles.expressionNodes, dataTypeMan), dataTypeMan);
			  }
		  }
		  
		  out = processExpression1Node(expressionEles.expression1Node, operand, dataTypeMan);
		  return out;
	  }

	  private static HAPOperand processExpression1Node(SimpleNode parentNode, HAPOperand aheadOperand, HAPDataTypeManager dataTypeMan){
		  if(isNodeEmpty(parentNode))  return aheadOperand;

		  HAPOperand out = null;
		  ExpressionElements expressionEles = getExpressionElements(parentNode);
		  String name = (String)expressionEles.nameNode.jjtGetValue();
		  HAPOperand operand = null;
		  if("function".equals(parentNode.jjtGetValue())){
			  //function call
			  operand = new HAPOperandDataOperaion(aheadOperand, name, getOperationParms(expressionEles.expressionNodes, dataTypeMan), dataTypeMan);
		  }
		  else{
			  //path
			  operand = new HAPOperandAttribute(aheadOperand, name, dataTypeMan);
		  }
		  out = processExpression1Node(expressionEles.expression1Node, operand, dataTypeMan);
		  return out;
	  }

	  private static HAPOperand[] getOperationParms(List<SimpleNode> expressionParmNodes, HAPDataTypeManager dataTypeMan){
		  HAPOperand[] out = new HAPOperand[expressionParmNodes.size()];

		  for(SimpleNode parmNode : expressionParmNodes){
			  int index = Integer.parseInt((String)parmNode.jjtGetValue());
			  out[index] = processExpressionNode(parmNode, dataTypeMan);
		  }
		  return out;
	  }
	  
	  private static ExpressionElements getExpressionElements(SimpleNode parentNode){
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
			  case NosliwExpressionParser.JJTEXPRESSION:
			  {
				  out.expressionNodes.add(childNode);
				  break;
			  }
			  case NosliwExpressionParser.JJTEXPRESSION1:
			  {
				  out.expression1Node = childNode;
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
	  SimpleNode nameNode;
	  SimpleNode dataTypeNode;
	  List<SimpleNode> expressionNodes = new ArrayList<SimpleNode>();
	  SimpleNode expression1Node;
}
