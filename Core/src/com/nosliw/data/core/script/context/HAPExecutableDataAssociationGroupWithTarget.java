package com.nosliw.data.core.script.context;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.runtime.HAPExecutable;

@HAPEntityWithAttribute
public interface HAPExecutableDataAssociationGroupWithTarget extends HAPExecutable{

	@HAPAttribute
	public static String OUTPUTASSOCIATION = "outputAssociation";
	
	@HAPAttribute
	public static String OUTPUTMATCHERS = "outputMatchers";

	HAPExecutableDataAssociationGroup getOutputDataAssociation();
	void setOutputDataAssociation(HAPExecutableDataAssociationGroup output);

	void addOutputMatchers(String path, HAPMatchers matchers);
	Map<String, HAPMatchers> getOutputMatchers();
}
