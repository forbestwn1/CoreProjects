/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage("module.userapps");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentUserInfo = function(){
	var loc_vueComponent = {
		data : function(){
			return {};
		},
		props : {
			user : {
				default : function(){  return {};  }
			}
		},
		components : {
		},
		methods : {
		},
		template : `
			<div>
				{{user.name}}
			</div>
		`
	};
	return loc_vueComponent;
};	
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentUserInfo", node_createComponentUserInfo); 

})(packageObj);
