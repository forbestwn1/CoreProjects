//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataType = Nosliw.getDataType("core.url", "1.1.0");

dataType.requires = {
	"operation" : "op|dataype1|operation1",
	"datatype" : "",
	"library" : ""
};

dataType.operations['host1'].requires = {
		"operation" : "dataype1|operation1|op",
		"datatype" : "",
		"library" : ""
};

dataType.operations['host1'].operation = function(parms, context){
	
	context.operate("op", parms, context);
	
	context.operate("dataType1", "operation1", parms, context);
	
	context.operate("dataType2", "operation2", parms, context);
};		 

operations['host2'].operation = function(parms, context){
	
	
};		 

