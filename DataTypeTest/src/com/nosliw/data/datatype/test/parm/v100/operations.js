//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefition = nosliw.getDataTypeDefinition("test.parm");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefition.requires = {
};

//define what operations in this page requires (operation, datatype, library)

//define operation
dataTypeDefition.operations['getValue'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			return this.value.data;
		},

		requires:{
		},
};

dataTypeDefition.operations['getCriteria'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			context.logging.info("Operand Calcualting [test.parm.getCriteria]  ----------------");
			context.logging.info("Input : " + JSON.stringify(parms));
			var out = this.value.outputCriteria;
			context.logging.info("Output : " + JSON.stringify(out));
			context.logging.info("-----------------------------------------------");
			return out;
		},

		requires:{
		},
};

nosliw.addDataTypeDefinition(dataTypeDefition);
