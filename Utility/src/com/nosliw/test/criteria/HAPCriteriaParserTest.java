package com.nosliw.test.criteria;


import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.criteria.HAPCriteriaParser;

import com.nosliw.data.imp.criteria.parser.generated.ParseException;

public class HAPCriteriaParserTest {

	public static void main(String[] args) throws ParseException {

		String[] criteriasStr = {
				"abc.abc;1.0.0%||a:cde;1.1.1,b:dfe;1.0.0||%",
				"abc.abc;1.0.0%%||a:cde;1.1.1,b:dfe;1.0.0||%%",
				"#||abc.abc;1.0.0%||a:cde;1.1.1,b:dfe;1.0.0||%,abc.abc;1.0.0%||a:cde;1.2.3,b:dfe;1.0.0||%||#",
				"`||abc.abc;1.0.0-abc.abc;1.0.0%||a:cde;1.2.3,b:dfe;1.0.0||%||`",
				"`||-abc.abc;1.0.0%||a:cde;1.2.3,b:dfe;1.0.0||%||`",
				"^||abc.abc;1.0.0%||a:cde;1.2.3,b:dfe;1.0.0||%,abc.abc;1.0.0%||a:cde;1.2.3,b:dfe;1.0.0||%||^",
				"*",
				"~||abcdrfg||~",
				"@||(abcdrfg||@",
				"test.array;1.0.0%||element:@||!(test.expression)!.outputCriteria(expression;;;&(expression)&;;parm;;;&(parms)&)||@||%"
		};

		for(String criteriaStr : criteriasStr){
			HAPVariableInfo criteria = HAPCriteriaParser.getInstance().parseCriteria(criteriaStr);
			String criteriaLiterate = HAPSerializeManager.getInstance().toStringValue(criteria, HAPSerializationFormat.LITERATE);
			if(criteriaLiterate.equals(criteriaStr)){
				System.out.println(criteriaLiterate);
			}
			else{
				System.out.println();
				System.out.println("Expected :" + criteriaStr);
				System.out.println("But got  :" + criteriaLiterate);
				System.out.println();
			}
		}
		
//		  InputStream is = new ByteArrayInputStream(criteriaStr.getBytes());
//		  HAPCriteriaParserGenerated parser = new HAPCriteriaParserGenerated( is ) ;
//		  SimpleNode root = parser.Criteria(0);
//		  root.dump("");
		
	}

}
