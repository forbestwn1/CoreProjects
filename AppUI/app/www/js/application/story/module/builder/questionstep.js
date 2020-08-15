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
		props : ['data', 'story', 'stages', 'currentStage', 'errorMessages'],
		components : {
		},
		computed: {
			stageIndex : {
				get : function(){
					var that = this;
					var index;
					_.each(this.stages, function(stage, i){
						if(stage.name==that.currentStage)  index=i;
					});
					return index;
				},
			}
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
		    	<div>
				  <span v-for="errorMessage in errorMessages" style="color:red;"><br>{{errorMessage}}
				  </span>
		    	</div>

		    	<div>
				  <span v-for="stage in stages">
    					<span v-if="stage.name==currentStage" style="color:red;"><br>{{stage.name}}</span>
    					<span v-if="stage.name!=currentStage"><br>{{stage.name}}</span>
				  </span>
		    	</div>
				    
				<br>
		    	QuestionStep
				<br>
				<question-group v-bind:data="data" v-bind:story="story">
				</question-group>

				<br>
				<a v-on:click.prevent="onPrevious" v-if="stageIndex>=1">Previous</a>
				<br>

				<br>
				<a v-on:click.prevent="onNext" v-if="stageIndex<stages.length-2">Next</a>
				<br>

				<br>
				<a v-on:click.prevent="onFinish" v-if="stageIndex>=stages.length-2">Finish</a>
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
