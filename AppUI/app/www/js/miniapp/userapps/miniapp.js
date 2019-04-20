/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage("module.userapps");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentMiniApp = function(){

	var loc_miniAppModel = {
//		minApp : {}
	};
	
	var loc_vueComponent = {
		data : function(){
			return loc_miniAppModel;
		},
		props : ['miniapp'],
		methods : {
			chooseMiniApp : function(event) {
				this.$emit("selectMiniApp", this.miniapp.id);
			}
		},
		template : `
			<div>
				<p>
					<a v-on:click.prevent="chooseMiniApp">{{miniapp.name}}</a>
				</p>
			
			</div>
		`
	};
	return loc_vueComponent;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentMiniApp", node_createComponentMiniApp); 

})(packageObj);
