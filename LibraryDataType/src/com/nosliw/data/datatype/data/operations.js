//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefition = nosliw.getDataTypeDefinition("base.data");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefition.requires = {
};

//define what operations in this page requires (operation, datatype, library)
dataTypeDefition.localRequires = {
	"operation" : { 
	},
	"datatype" : {
	},
	"library" : {
	},
	"helper" : {
	}
};

nosliw.addDataTypeDefinition(dataTypeDefition);
