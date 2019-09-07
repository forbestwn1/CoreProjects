//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;

//*******************************************   Start Node Definition  ************************************** 	

var node_componentUtility = {
		
	isActive : function(status){  return status==node_CONSTANT.LIFECYCLE_COMPONENT_STATUS_ACTIVE;    },
	
	buildDecorationInfoArrayByPart : function(decInfoDef, partName){
		var out = [];
		if(decInfoDef.parts!=undefined){
			var partDecs = decInfoDef.parts[partName];
			if(partDecs!=undefined){
				_.each(partDecs, function(dec, i){   out.push(dec);  });
			}
		}
		if(decInfoDef.share!=undefined){
			_.each(decInfoDef.share, function(dec, i){  out.push(dec);   });
		}
	},
};

//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});


//Register Node by Name
packageObj.createChildNode("componentUtility", node_componentUtility); 

})(packageObj);
