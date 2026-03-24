package com.nosliw.core.application.entity.brickcriteria;

import java.util.List;
import java.util.stream.Collectors;

public class HAPUtilityBrickCriteria {

	public static List<HAPRestrainBrick> getCriteriaRestrain(HAPCriteriaBrick brickCriteria, String restrainType) {
		return brickCriteria.getRestains().stream().filter(restrain->restrain.getType().equals(restrainType)).collect(Collectors.toList());
	}

}
