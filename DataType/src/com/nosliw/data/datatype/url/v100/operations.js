//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefinition = nosliw.getDataTypeDefinition("core.url;1.0.0");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefinition.requires = {
};

dataTypeDefinition.convertTo = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(data, toDataType, context){
			context.logging.info("Operand Calcualting [convert]  ----------------");
			return {
				dataTypeId : "core.url;1.0.0",
				value : 'http:http:'+data.value
			};
		} 
};

nosliw.addDataTypeDefinition(dataTypeDefinition);
