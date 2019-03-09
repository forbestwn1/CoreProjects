//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_CONSTANT;
	var node_makeObjectWithType;
	var node_getObjectType;
//*******************************************   Start Node Definition  ************************************** 	

//task result 
//  resultName : name of the result
//  result: result value map (value name / value)
var node_IOTaskResult = function(resultName, resultValue){
	this.resultName = resultName;
	this.resultValue = resultValue; 
};

var node_createIODataSet = function(value){
	
	var loc_dataSet = {};
	
	if(value!=undefined){
		var valueType = node_getObjectType(value);
		if(valueType==node_CONSTANT.TYPEDOBJECT_TYPE_IOINPUT){
			return value;
		}
		else{
			//value is default value
			loc_dataSet[node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_DEFAULT] = value;
		}
	}
	
	var loc_out = {
		
		setData : function(name, data){  loc_dataSet[name] = data;   },
		
		getData : function(name){
			if(name==undefined)  name = node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_DEFAULT;
			return loc_dataSet[name];
		},
		
		getDataSet : function(){   return loc_dataSet;   },
		
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_IOINPUT);
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});

//Register Node by Name
packageObj.createChildNode("IOTaskResult", node_IOTaskResult); 
packageObj.createChildNode("createIODataSet", node_createIODataSet); 

})(packageObj);
