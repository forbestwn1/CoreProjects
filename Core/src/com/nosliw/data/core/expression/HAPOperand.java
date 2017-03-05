package com.nosliw.data.core.expression;

import java.util.List;

public interface HAPOperand {

	String getType();

	List<HAPOperand> getChildren();
}
