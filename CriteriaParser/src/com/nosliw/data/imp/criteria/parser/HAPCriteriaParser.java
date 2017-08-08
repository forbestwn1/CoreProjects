package com.nosliw.data.imp.criteria.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPOperand;
import com.nosliw.data.core.expression.HAPOperandConstant;
import com.nosliw.data.core.expression.HAPOperandOperation;
import com.nosliw.data.core.expression.HAPOperandReference;
import com.nosliw.data.core.expression.HAPOperandVariable;
import com.nosliw.data.imp.criteria.parser.generated.HAPCriteriaParserGenerated;
import com.nosliw.data.imp.criteria.parser.generated.SimpleNode;


public class HAPCriteriaParser {

	  public HAPDataTypeCriteria parseExpression(String expression){
		  SimpleNode root = null;
		  try{
			  InputStream is = new ByteArrayInputStream(expression.getBytes());
			  HAPCriteriaParserGenerated parser = new HAPCriteriaParserGenerated( is ) ;
	          root = parser.Criteria(0);
		  }
		  catch(Exception e){
			  e.printStackTrace();
			  return null;
		  }
          return processCriteriaNode(root);
	  }

	  private HAPDataTypeCriteria processCriteriaNode(SimpleNode criteriaNode){
		  
		  SimpleNode childNode = (SimpleNode)criteriaNode.jjtGetChild(0);
		  
		  switch(childNode.getId()){
		  case HAPCriteriaParserGenerated.JJTIDCRITERIA:
		  {
			  out.constantNode = childNode;
			  break;
		  }
		  }		  
		  
		  return out;
	  }
	
	  
}
