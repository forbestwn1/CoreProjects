//get/create package
var packageObj = library.getChildPackage("idservice");    


(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_createIdService = function(){
	var loc_id = 0;
	
	var loc_out = {
		generateId : function(){
			loc_id++;
			return loc_id;
		},
	
		reset : function(){
			loc_id = 0;
			return loc_id;
		}
	};
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("createIdService", node_createIdService); 

	var module = {
		start : function(packageObj){
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
