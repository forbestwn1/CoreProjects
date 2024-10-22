	
	let parentComponent = {
		data : function(){
			return {
				parentValue : "parent",
				parentObj : {
					aaa : "aaa",
					bbb : "bbb"
				}		
			};
		},
		props : ['pro1', 'pro2'],
		components : {
		},
		methods : {
		},
		created : function(){
		},
		computed: {
			proComputed : {
				get : function(){
					return this.parentValue;
				},
				set : function(value){
					this.parentValue = value;
				}
			}
		},
		watch : {
		},

		template :
		 `
			<div>
				<br/>-----inparent----</br>
				Parent pro -- {{pro1}}
				</br>
				Parent value -- {{parentValue}}
				</br>
				Parent obj -- {{parentObj.aaa}}
				</br>
				<slot v-bind:slotPro1="parentObj"></slot>
				<br/>-----inparent----</br>
			</div>
		`
	};
	
	