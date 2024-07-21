//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
var node_basicUtility;
var node_namingConvensionUtility;
var node_CONSTANT;
var node_COMMONCONSTANT;
var node_COMMONATRIBUTECONSTANT;
var node_makeObjectWithType;
var node_getObjectType;
var node_createValueStructureVariableInfo;
//*******************************************   Start Node Definition  ************************************** 	

var node_VariableIdValuePair = function(varId, value){
	this.variableId = varId;
	this.value = value;
};

var node_ElementIdValuePair = function(elementId, vlaue){
	this.elementId = elementId;
	this.value = value;
};

//locally reference to element in value port
var node_createValuePortElementInfo = function(elementId, n, p){

	if(node_getObjectType(elementId)==node_CONSTANT.TYPEDOBJECT_TYPE_VALUEPORTELEMENTID)  return elementId;

	var loc_valueStructureId;

	var loc_valueStructureVariableInfo;

	var loc_key;

	var loc_init = function(elementId, n, p){
		if(n==undefined&&p==undefined){
			if(node_basicUtility.isStringValue(elementId)){
				var index = elementId.indexOf(node_COMMONCONSTANT.SEPERATOR_PATH);
				if(index!=-1){
					loc_valueStructureId = elementId.substring(0, index);
					loc_valueStructureVariableInfo = node_createValueStructureVariableInfo(elementId.substring(index+1));
				}
			}
			else{
				//variable id object
				var rootEleId = elementId[node_COMMONATRIBUTECONSTANT.IDELEMENT_ROOTELEMENTID];
				loc_valueStructureId = rootEleId[node_COMMONATRIBUTECONSTANT.IDROOTELEMENT_VALUESTRUCTUREID];
				loc_valueStructureVariableInfo =  node_createValueStructureVariableInfo(rootEleId[node_COMMONATRIBUTECONSTANT.IDROOTELEMENT_ROOTNAME], elementId[node_COMMONATRIBUTECONSTANT.IDELEMENT_ELEMENTPATH]);
			}
		}
		else{
			loc_valueStructureId = elementId;
			loc_valueStructureVariableInfo =  node_createValueStructureVariableInfo(n, p);
		}
		loc_key = node_namingConvensionUtility.cascadePath(loc_valueStructureId, loc_valueStructureVariableInfo.key);
	};

	var loc_out = {

		getValueStructureId : function(){   return  loc_valueStructureId;    },
		
		getValueStructureVariableInfo : function(){    return loc_valueStructureVariableInfo;    },

		getRootName : function(){   return loc_valueStructureVariableInfo.name;   },
		
		getElementPath : function(){    return  loc_valueStructureVariableInfo.path;   },

		getKey : function(){  return loc_key;    },

		getFullPath : function(){	return this.getKey();	},
		
		//key
		key : loc_key,
		
	};
	
	loc_init(elementId, n, p);
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_VALUEPORTELEMENTID);
	
	loc_out.elementId = elementId;
	loc_out.valueStructureVariableInfo = loc_valueStructureVariableInfo;
	
	return loc_out;
	
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valuestructure.createValueStructureVariableInfo", function(){node_createValueStructureVariableInfo = this.getData();});

//Register Node by Name
packageObj.createChildNode("createValuePortElementInfo", node_createValuePortElementInfo); 
packageObj.createChildNode("VariableIdValuePair", node_VariableIdValuePair); 
packageObj.createChildNode("ElementIdValuePair", node_ElementIdValuePair); 

})(packageObj);
