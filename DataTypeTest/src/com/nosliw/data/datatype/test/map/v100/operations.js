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
			var name = parms.getParm("name").value;
			var data = parms.getParm("value");
			this.value[name] = data;
			return this;
		},
};


dataTypeDefition.operations['getChildrenNames'] = {
		operation : function(parms, context){
			var names = [];
			_.each(this.value, function(mapValue, name){
				names.push({
					dataTypeId : "test.string;1.0.0",
					value : name
				});
			});
			
			return {
				dataTypeId : "test.array;1.0.0",
				value : names
			};
		},
};

dataTypeDefition.operations['isAccessChildById'] = {
		operation : function(parms, context){
			return {
				dataTypeId : "test.boolean;1.0.0",
				value : true
			};
		},
};

dataTypeDefition.operations['length'] = {
		operation : function(parms, context){
			var obj = parms.getParm("base").value;
			var count = 0;
			for (var k in obj) {
			    if (obj.hasOwnProperty(k)) {
			       ++count;
			    }
			}			
			
			return {
				dataTypeId : "test.integer;1.0.0",
				value : count
			};
		},
};

dataTypeDefition.operations['getChildData'] = {
		operation : function(parms, context){
			var name = parms.getParm("name").value;
			return this.value[name];
		}
};

dataTypeDefition.operations['setChildData'] = {
		operation : function(parms, context){
			var name = parms.getParm("name").value;
			var value = parms.getParm("value");
			this.value[name] = value;
			return this;
		}
};


dataTypeDefition.operations['new'] = {
		//defined operation
		//in operation can access all the required resources by name through context
		operation : function(parms, context){
			return {
				dataTypeId : "test.map;1.0.0",
				value : {},
			};
		},
};

nosliw.addDataTypeDefinition(dataTypeDefition);
