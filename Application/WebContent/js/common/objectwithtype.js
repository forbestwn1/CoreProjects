//get/create package
var packageObj = library.getChildPackage("objectwithtype");    

(function(packageObj){
	//get used node
	var basicUtilityNode = packageObj.require("common.utility.BasicUtility");
//*******************************************   Start Node Definition  ************************************** 	

var TYPEATTRNAME = "__ObjectType";
	
/*
 * utility functions to build object with particular type
 */
var loc_createTypedObject = function(type){
	var loc_type = type;
	var loc_out = {};
	loc_out[typeAttrName] = function(){  return loc_type; };
	return loc_out;
};
	
	
/*
 * build an object to typed object
 */
var makeObjectWithType = function(obj, type){
	var out = _.extendOwn(obj, loc_createTypedObject(type));
	return out;
};

/*
 * get object's type info
 * if no type info, the use VALUE as type  
 */
var getObjectType = function(object){
	if(object==undefined)   return NOSLIWCONSTANT.TYPEDOBJECT_TYPE_VALUE;
	var type = undefined;
	if(typeof object[TYPEATTRNAME] === 'function'){
		type = object[TYPEATTRNAME]();
	}
	
	if(type!=undefined)    return type;
	else return NOSLIWCONSTANT.TYPEDOBJECT_TYPE_VALUE;
};
	

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("makeObjectWithType", makeObjectWithType); 
packageObj.createNode("getObjectType", getObjectType); 

})(packageObj);

