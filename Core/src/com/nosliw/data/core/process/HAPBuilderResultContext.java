package com.nosliw.data.core.process;

import com.nosliw.data.core.script.context.dataassociation.HAPDataAssociationIO;

public interface HAPBuilderResultContext {

	//it is for activity plugin to build context for particular result defined in activity
	HAPDataAssociationIO buildResultContext(String result, HAPExecutableActivityNormal activity);

}
