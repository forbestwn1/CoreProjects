package com.nosliw.data.core.process;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.expression.HAPMatchers;

public class HAPBackToGlobalContextImp implements HAPBackToGlobalContext{

//	@HAPAttribute
//	public static String OUTPUTASSOCIATION = "outputAssociation";
//	
//	@HAPAttribute
//	public static String OUTPUTMATCHERS = "outputMatchers";

	//match from data association output to target context variable
	private Map<String, HAPMatchers> m_outputMatchers;

	//associate output of activity to variable in process 
	private HAPExecutableDataAssociationGroup m_outputAssociation;

	@Override	
	public HAPExecutableDataAssociationGroup getOutputDataAssociation() {   return this.m_outputAssociation;   }
	@Override
	public void setOutputDataAssociation(HAPExecutableDataAssociationGroup output) {   this.m_outputAssociation = output;    }

	@Override
	public void addOutputMatchers(String path, HAPMatchers matchers) {   this.m_outputMatchers.put(path, matchers);     }
	@Override
	public Map<String, HAPMatchers> getOutputMatchers() {  return this.m_outputMatchers; }
}
