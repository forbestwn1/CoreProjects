//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var createIdService = function(){
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
}

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("createIdService", createIdService); 

})(packageObj);


