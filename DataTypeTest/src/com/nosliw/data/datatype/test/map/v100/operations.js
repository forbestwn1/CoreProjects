//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefition = nosliw.getDataTypeDefinition("test.map");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefition.requires = {
};

//define what operations in this page requires (operation, datatype, library)

//define operation
//define operation
dataTypeDefition.operations['put'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			context.logging.info("Operand Calcualting [New]  ----------------");
			var name = parms.getParm("name").value;
			var data = parms.getParm("value");
			this.value[name] = data;
			return this;
		},
};


dataTypeDefition.operations['new'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			context.logging.info("Operand Calcualting [New]  ----------------");
			return {
				dataTypeId : "test.map;1.0.0",
				value : {},
			};
		},
};

nosliw.addDataTypeDefinition(dataTypeDefition);
