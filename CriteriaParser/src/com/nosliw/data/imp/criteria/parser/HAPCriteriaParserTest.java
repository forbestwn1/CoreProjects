package com.nosliw.data.imp.criteria.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.nosliw.data.imp.criteria.parser.generated.HAPCriteriaParserGenerated;
import com.nosliw.data.imp.criteria.parser.generated.ParseException;
import com.nosliw.data.imp.criteria.parser.generated.SimpleNode;

public class HAPCriteriaParserTest {

	public static void main(String[] args) throws ParseException {
		String criteria = "abc.abc;1.0.0";
		
		  InputStream is = new ByteArrayInputStream(criteria.getBytes());
		  HAPCriteriaParserGenerated parser = new HAPCriteriaParserGenerated( is ) ;
		  SimpleNode root = parser.Criteria(0);
		  root.dump("");

	}

}
