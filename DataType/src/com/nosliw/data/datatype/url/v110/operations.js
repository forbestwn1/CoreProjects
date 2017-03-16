//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var operations = initForOperation("core.url", "1.1.0");

operations['host1'] = function(parms, context){
	
	context.operate("dataType1", "operation1", parms, context);
	
	context.operate("dataType2", "operation2", parms, context);
};		 

operations['host2'] = function(parms, context){
	
	
};		 

