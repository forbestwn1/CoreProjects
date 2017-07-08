//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefition = nosliw.getDataTypeDefinition("test.integer");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefition.requires = {
};

//define operation
dataTypeDefition.operations['add'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			context.logging.info("Operand Calcualting integer.add ----------------");
			var parm1 = parms.getParm("parm1").value;
			var parm2 = parms.getParm("parm2").value;
			var out = parm1 +  parm2;
			return {
				dataTypeId : "test.integer;1.0.0",
				value : out,
			}
		},
};

nosliw.addDataTypeDefinition(dataTypeDefition);
