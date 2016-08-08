/*
 * data is a combination of value + dataType
 */
var nosliwCreateData = function(value, dataTypeInfo){
	var loc_out = {
		value : value,
		dataTypeInfo : dataTypeInfo,
	};
	
	loc_out = nosliwTypedObjectUtility.makeTypedObject(loc_out, NOSLIWCONSTANT.TYPEDOBJECT_TYPE_DATA);
	return loc_out;
};


