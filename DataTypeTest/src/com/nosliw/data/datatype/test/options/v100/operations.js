//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefition = nosliw.getDataTypeDefinition("test.options");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefition.requires = {
};

//define what operations in this page requires (operation, datatype, library)

//define operation
dataTypeDefition.operations['all'] = {
		operation : function(parms, context){
			var valueOut = [];
			var gatewayParms = {
				"id" : parms.getParm("optionsId").value,
			};
			var optionsValues =  context.getResourceDataByName("myGateWay").command("getValues", gatewayParms);
			_.each(optionsValues, function(value, i){
				valueOut.push({
					dataTypeId : "test.string;1.0.0",
					value : value,
				});
			});

			return {
				dataTypeId : "test.array;1.0.0",
				value : valueOut,
			};
		},

		requires:{
			"jsGateway" : { 
				myGateWay: "options",
			}
		},
};

nosliw.addDataTypeDefinition(dataTypeDefition);
