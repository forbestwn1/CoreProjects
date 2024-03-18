package com.nosliw.core.application.common.structure;

import java.util.Set;

import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.scriptexpression.HAPWithConstantScriptExpression;

public interface HAPStructure extends HAPWithConstantScriptExpression{

	HAPRootStructure getRoot(String rootName, boolean createIfNotExist);

	Set<HAPRootStructure> getAllRoots();

}
