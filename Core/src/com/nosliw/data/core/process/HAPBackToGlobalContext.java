package com.nosliw.data.core.process;

import java.util.Map;

import com.nosliw.data.core.expression.HAPMatchers;

public interface HAPBackToGlobalContext {

	HAPExecutableDataAssociationGroup getOutputDataAssociation();
	void setOutputDataAssociation(HAPExecutableDataAssociationGroup output);

	void addOutputMatchers(String path, HAPMatchers matchers);
	Map<String, HAPMatchers> getOutputMatchers();
}
