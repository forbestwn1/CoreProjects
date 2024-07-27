package com.nosliw.core.application.common.structure;

import java.util.Set;

import com.nosliw.core.application.division.manual.common.scriptexpression.HAPWithConstantScriptExpression;
import com.nosliw.core.application.valuestructure.HAPRootStructure;

public interface HAPStructure extends HAPWithConstantScriptExpression{

	HAPRootStructure getRoot(String rootName, boolean createIfNotExist);

	Set<HAPRootStructure> getAllRoots();

}
