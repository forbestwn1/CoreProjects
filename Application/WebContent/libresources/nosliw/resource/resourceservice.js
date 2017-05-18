//get/create package
var packageObj = library.getChildPackage("resourceservice");    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

/**
 * Create Resource Service
 * Resrouce service manage all the resources
 */
var createResourceService = function(){
	
	var loc_out = {
			
		/**
		 * 
		 */
		useResource : function(moduleId, resourceId){
			
		},
		
		dismissResource : function(moduleId, resourceId){
			
		},
	};
	
	return loc_out;
};	

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("createResourceService", createResourceService); 

})(packageObj);
