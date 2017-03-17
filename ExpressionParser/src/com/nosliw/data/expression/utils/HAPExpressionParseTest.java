package com.nosliw.data.expression.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.strvalue.io.HAPStringableEntityImporterJSON;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.expression.HAPOperand;
import com.nosliw.data.core.imp.HAPDataTypeManagerImp;
import com.nosliw.data.core.imp.expression.HAPExpressionInfoImp;
import com.nosliw.data.core.imp.expression.HAPExpressionManagerImp;
import com.nosliw.data.imp.expression.parser.HAPExpressionParserImp;
import com.nosliw.data.imp.expression.parser.generated.NosliwExpressionParser;
import com.nosliw.data.imp.expression.parser.generated.ParseException;
import com.nosliw.data.imp.expression.parser.generated.SimpleNode;
import com.nosliw.data.imp.expression.parser.generated.TokenMgrError;

public class HAPExpressionParseTest {

	  public static void main(String args[]) throws ParseException, TokenMgrError {
		  
//		  String str = "!(this:simple)!.fun1(?(bb)?.fun2(:(dd):),:(cc):.fun3())";
		  
//		  String str = "?(key)?.largerThan(&(dddd)&,&(dddd)&)";
		  
		  String str = "?(schoolsData)?.each(parm1:?(validHomeExpression)?,parm2:&(constantData2)&,parm3:&(constantData)&,parm4:<(referenceData)>).aaa.bbb";
//		  String str = "?(schoolsData)?.each(parm1:?(validHomeExpression)?,parm2:&(schoolData)&)";
//		  String str = "?(schoolsData)?.each(parm1:?(validHomeExpression)?)";
		  
		  InputStream is = new ByteArrayInputStream(str.getBytes());
		  
          NosliwExpressionParser parser = new NosliwExpressionParser( is ) ;
          SimpleNode root = parser.Expression("");
          root.dump("");

          HAPDataTypeManagerImp dataTypeMan = new HAPDataTypeManagerImp();
          HAPExpressionManagerImp.getInstance();
          new HAPDataTypeManagerImp();
          
          InputStream inputStream = HAPFileUtility.getInputStreamOnClassPath(HAPExpressionParseTest.class, "expression.json");
          HAPExpressionInfoImp expressionInfo = (HAPExpressionInfoImp)HAPStringableEntityImporterJSON.parseJsonEntity(inputStream, "data.expression", HAPValueInfoManager.getInstance());
          System.out.println(HAPJsonUtility.formatJson(expressionInfo.toStringValue(HAPSerializationFormat.JSON)));

          HAPOperand operand = HAPExpressionParserImp.parseExpression(expressionInfo.getExpression());
          System.out.println(operand);
          
	  }
}
