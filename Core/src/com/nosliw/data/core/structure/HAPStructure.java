package com.nosliw.data.core.structure;

import java.util.Set;

import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;

public interface HAPStructure {

	HAPRootStructure getRoot(String rootName, boolean createIfNotExist);

	Set<HAPRootStructure> getAllRoots();

}
