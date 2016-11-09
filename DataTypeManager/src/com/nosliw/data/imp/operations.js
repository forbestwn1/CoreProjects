function initForOperation(dataType, version){
	return {
		'dataType' : {
			'name' : dataType,
			'version' : version,
		},
	};
};

var operationsInfo = initForOperation("url", "1.0.0");

operationsInfo["name1"] = function(parms, context, operationManager){
	operationManager.operate("dataType", "operation", parms, context);
	
};		 

operationsInfo["name2"] = function(parms, context, operationManager){
	
	
};		 

operationsInfo;
