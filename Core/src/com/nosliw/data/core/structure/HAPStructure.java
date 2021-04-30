package com.nosliw.data.core.structure;

public interface HAPStructure {

	HAPRoot getRoot(HAPReferenceRoot rootReference);

	HAPStructure cloneStructure();

}
