package com.nosliw.data.core.criteria;


import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.imp.criteria.parser.generated.HAPCriteriaParserGenerated;
import com.nosliw.data.imp.criteria.parser.generated.ParseException;
import com.nosliw.data.imp.criteria.parser.generated.SimpleNode;

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
				"@||(abcdrfg||@"
		};

		for(String criteriaStr : criteriasStr){
			HAPDataTypeCriteria criteria = HAPCriteriaParser.getInstance().parseCriteria(criteriaStr);
			String criteriaLiterate = HAPSerializeManager.getInstance().toStringValue(criteria, HAPSerializationFormat.LITERATE);
			System.out.println(criteriaLiterate);
			if(!criteriaLiterate.equals(criteriaStr)){
				throw new IllegalStateException();
			}
		}
		
//		  InputStream is = new ByteArrayInputStream(criteriaStr.getBytes());
//		  HAPCriteriaParserGenerated parser = new HAPCriteriaParserGenerated( is ) ;
//		  SimpleNode root = parser.Criteria(0);
//		  root.dump("");
		
	}

}
