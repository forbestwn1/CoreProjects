var operations = initForOperation("core.url", "1.1.0");

operations['host'] = function(parms, context){
	
	context.operate("dataType1", "operation1", parms, context);
	
	context.operate("dataType2", "operation2", parms, context);
};		 

operations['host1'] = function(parms, context){
	
	
};		 

operations;
