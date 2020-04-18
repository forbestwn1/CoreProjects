package com.nosliw.data.core.script.expression;

import java.util.List;

public interface HAPExecutableScriptWithSegment extends HAPExecutableScript{

	void addSegment(HAPExecutableScript segment);
	
	void addSegments(List<HAPExecutableScript> segments);
	
	List<HAPExecutableScript> getSegments();
}
