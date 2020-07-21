/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentQuestionStep = function(){

	var loc_vueComponent = {
		data : function(){
			return {};
		},
		props : ['data'],
		components : {
		},
		methods : {
			onPrevious : function(event) {
				this.$emit("previousStep");
			},
			onNext : function(event) {
				this.$emit("nextStep");
			},
			onFinish : function(event) {
				this.$emit("finishStep");
			},
		},
		template : `
			<div>
				<br>
		    	QuestionStep
				<br>
				<question-group v-bind:data="data">
				</question-group>

				<br>
				<a v-on:click.prevent="onPrevious">Previous</a>
				<br>

				<br>
				<a v-on:click.prevent="onNext">Next</a>
				<br>

				<br>
				<a v-on:click.prevent="onFinish">Finish</a>
				<br>
				
			</div>
		`
	};
	return loc_vueComponent;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentQuestionStep", node_createComponentQuestionStep); 

})(packageObj);
