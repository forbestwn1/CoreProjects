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
		
//		String criteriaStr = "abc.abc;1.0.0{||a:cde;123,b:dfe||}";
//		String criteriaStr = "#||abc.abc;1.0.0{||a:cde;123,b:dfe||},abc.abc;1.0.0{||a:cde;123,b:dfe||}||#";
//		String criteriaStr = "?||abc.abc;1.0.0-abc.abc;1.0.0{||a:cde;123,b:dfe||}||?";
//		String criteriaStr = "?||-abc.abc;1.0.0{||a:cde;123,b:dfe||}||?";
		String criteriaStr = "^||abc.abc;1.0.0{||a:cde;123,b:dfe||},abc.abc;1.0.0{||a:cde;123,b:dfe||}||^";
		HAPDataTypeCriteria criteria = HAPCriteriaParser.getInstance().parseCriteria(criteriaStr);
		System.out.println(HAPSerializeManager.getInstance().toStringValue(criteria, HAPSerializationFormat.LITERATE));
		
		  InputStream is = new ByteArrayInputStream(criteriaStr.getBytes());
		  HAPCriteriaParserGenerated parser = new HAPCriteriaParserGenerated( is ) ;
		  SimpleNode root = parser.Criteria(0);
		  root.dump("");

		  
		  
	}

}
