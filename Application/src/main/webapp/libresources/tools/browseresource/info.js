/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentInfo = function(){

	var loc_vueComponent = {
		data : function(){
			return {};
		},
		methods : {
		},
		props : ['data'],
		template : `
			<div>
				<p>{{data.name}}</p>
				<a class="link external" v-bind:href="data.url" target="_blank">{{data.url}}</a>
				<p>{{data.resourceId}}</p>
		  	</div>
		`
	};
	return loc_vueComponent;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentInfo", node_createComponentInfo); 

})(packageObj);
