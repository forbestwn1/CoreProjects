/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentChildren = function(){

	var loc_vueComponent = {
		data : function(){
			return {};
		},
		methods : {
			onChildSelectResource : function(resourceInfo) {
				this.$emit("selectResource", resourceInfo);
			},
		},
		props : ['data'],
		template : `
			<div>
			  	<resource-group 
					v-for="group in data"
					v-bind:key="group.type"
			  		v-bind:data="group"
					v-on:selectResource="onChildSelectResource"
			  	></resource-group>
		  	</div>
		`
	};
	return loc_vueComponent;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentChildren", node_createComponentChildren); 

})(packageObj);
