//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

var node_newOrderedContainer = function(){

	var loc_keys = [];
	var loc_elements = {};
	
	var loc_out = {
			
		getAllKeys : function(){  return loc_keys;   },
		
		getElement : function(key){   return loc_elements[key];  	},
		
		addElement : function(key, ele){
			loc_keys.push(key);
			loc_elements[key] = ele;
		}
	};
	
	return loc_out;

};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data

//Register Node by Name
packageObj.createChildNode("newOrderedContainer", node_newOrderedContainer); 

})(packageObj);
