package com.nosliw.test.expression;

import com.nosliw.data.core.application.common.dataexpression.HAPOperand;
import com.nosliw.data.imp.expression.parser.HAPDataExpressionParserImp;

public class HAPExpressionTest {

	public static void main(String[] args) {
		HAPDataExpressionParserImp parser = new HAPDataExpressionParserImp();
		
		HAPOperand operand = parser.parseExpression("!(test.string)!.subString(bbb:<(test10.123;test3@test10dataexpressionsuite|test2)>.with(aa.a.b:?(testVar4.a.b)?,to:?(testVar5)?),from.a.b:?(testVar4.a.aa)?,to:&(testVar5.a)&)");
		System.out.println(operand);
	}
	
	
}
