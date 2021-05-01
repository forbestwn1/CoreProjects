package com.nosliw.data.core.structure;

public interface HAPStructure {

	HAPRoot getRoot(String localId);

	HAPStructure cloneStructure();

}
