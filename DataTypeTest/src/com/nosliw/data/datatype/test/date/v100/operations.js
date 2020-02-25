//run seperately
//		which data type (name + version) belong to
//      dependency
//		each operation (operation name, script, dependency)

var dataTypeDefition = nosliw.getDataTypeDefinition("test.date");

//define what this data type globlely requires (operation, datatype, library)
dataTypeDefition.requires = {
};

nosliw.addDataTypeDefinition(dataTypeDefition);
