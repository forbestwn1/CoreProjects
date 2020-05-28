//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_createApplicationConfigure = function(modulesConfigure, layout, initFun, data){
	var loc_modulesConfigure = modulesConfigure;
	var loc_layout = layout;
	var loc_data = data==undefined?{}:data;
	var loc_initFun = initFun;
	
	var loc_out = {
		getModulesConfigure : function(){   return loc_modulesConfigure;   },
		getLayout : function(){  return loc_layout;    },
		getData : function(){   return loc_data;   },
		getInitFun : function(){  return loc_initFun;   },
		addData : function(name, value){   loc_data[name] = value;    },
		addDataSet : function(dataSet){    loc_data = _.extend(loc_data, dataSet);     }
	};
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createApplicationConfigure", node_createApplicationConfigure); 

})(packageObj);
