package com.nosliw.data.core.script.context.dataassociation;

import java.util.Map;

import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.script.context.HAPParentContext;

public interface HAPExecutableTask extends HAPExecutable{

	HAPParentContext getInContext();

	Map<String, HAPParentContext> getOutResultContext();

}
