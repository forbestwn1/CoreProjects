package com.nosliw.test.expression;

import com.nosliw.data.imp.expression.parser.HAPExpressionParserImp;

public class HAPExpressionTest {

	public static void main(String[] args) {
		HAPExpressionParserImp parser = new HAPExpressionParserImp();
		
		parser.parseExpression("!(test.string)!.subString(bbb:<(test10#test3)>.with(aa_a_b:?(testVar4_a_b)?,to:?(testVar5)?),from_a_b:?(testVar4)?,to:?(testVar5)?)");
	}
	
	
}
