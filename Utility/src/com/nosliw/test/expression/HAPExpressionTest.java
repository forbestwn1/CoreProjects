package com.nosliw.test.expression;

import com.nosliw.data.core.operand.HAPOperand;
import com.nosliw.data.imp.expression.parser.HAPExpressionParserImp;

public class HAPExpressionTest {

	public static void main(String[] args) {
		HAPExpressionParserImp parser = new HAPExpressionParserImp();
		
		HAPOperand operand = parser.parseExpression("!(test.string)!.subString(bbb:<(test10.123;test3@test10dataexpressionsuite|test2)>.with(aa.a.b:?(testVar4.a.b)?,to:?(testVar5)?),from.a.b:?(testVar4.a.aa)?,to:&(testVar5.a)&)");
		System.out.println(operand);
	}
	
	
}
