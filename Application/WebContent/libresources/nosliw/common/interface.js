//get/create package
var packageObj = library.getChildPackage("interface");    

(function(packageObj){
	//get used node
	var node_basicUtility;
//*******************************************   Start Node Definition  ************************************** 	

var interfaceAttributeName = "____Interface";

/**
 * Append interface object to base object
 */
var loc_getInterfaceObject = function(baseObject, createIfNotExist){
	if(baseObject==undefined)  return undefined;
	
	var interfaceObj = baseObject[interfaceAttributeName];
	if(interfaceObj==undefined && createIfNotExist===true){
		interfaceObj = {};
		baseObject[interfaceAttributeName] = interfaceObj;

		//add "getInterfaceObject" method to baseObject
		baseObject.getInterfaceObject = function(name){
			if(name==undefined)   return interfaceObj;
			else   return interfaceObj[name];
		}
	}
	return interfaceObj;
};

var node_buildInterface = function(baseObject, name, newInterfaceObj){
	var interfaceBase = loc_getInterfaceObject(baseObject, true);
	
	//add "getBaseObject" method to interfaceObject
	newInterfaceObj.getBaseObject = function(){
		return baseObject;
	};

	//store interface object as attribute of baseObject
	baseObject["interfaceObject"+node_basicUtility.capitalizeFirstLetter(name)] = newInterfaceObj;
	
	interfaceBase[name] = newInterfaceObj;
	
	//execute bind callback (bindBaseObject)
	if(newInterfaceObj.bindBaseObject!=undefined){
		newInterfaceObj.bindBaseObject(baseObject);
	}
	
	return baseObject;
};

var node_getInterface = function(baseObject, name){
	var interfaceObj = loc_getInterfaceObject(baseObject, false);
	if(interfaceObj===undefined)  return undefined;
	else return interfaceObj[name];
};

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createChildNode("buildInterface", node_buildInterface); 
packageObj.createChildNode("getInterface", node_getInterface); 

var module = {
		start : function(packageObj){
			node_basicUtility = packageObj.getNodeData("common.utility.basicUtility");
		}
};
nosliw.registerModule(module, packageObj);

})(packageObj);
