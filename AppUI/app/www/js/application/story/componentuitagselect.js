/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_CONSTANT;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentUITagSelect = function(){

	var loc_getUITagInfoById = function(that, tagId){
		for(var i in that.uitaginfolist){
			if(that.uitaginfolist[i].id==tagId)   return that.uitaginfolist[i]; 
		}
	};
	
	var loc_vueComponent = {
		data : function(){
			return {
				selectedId : undefined,
			};
		},
		props : ['uitaginfolist', 'initselect', 'datainfo'],
		components : {
		},
		methods : {
		},
		computed: {
			currentTagInfo : {
				get : function(){
					return loc_getUITagInfoById(this, this.selectedId);
				},
			}
		},
		watch : {
			initselect : function(){
				this.selectedId = this.initselect;
			},
			selectedId : function(){
				this.$emit("selectChange", this.selectedId);
			},
		},
		template : `
			<div>
				<div>
					<uitag_data v-bind:uitaginfo="currentTagInfo" v-bind:datainfo="datainfo"/>
				</div>
				
				  <div v-for="uitaginfo in uitaginfolist" >
					  <input type="radio" id="uitaginfo.id" v-bind:value="uitaginfo.id" v-model="selectedId">
					  <label for="uitaginfo.id">{{ uitaginfo.name }}</label><br>
				  </div>
			</div>
		`
	};
		
	return loc_vueComponent;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentUITagSelect", node_createComponentUITagSelect); 

})(packageObj);
