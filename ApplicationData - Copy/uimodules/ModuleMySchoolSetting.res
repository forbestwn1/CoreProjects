{
	context : {
		criteria : {
			path : "uis.query.criteria"
		},
	},
	constants : {
	
	},
	command : [
		{
			name : "init",
			parm : {
				
			
			}
		
		}
	
	],
	init : [
		{
			name:"presentSchoolQuery",
			type:"presentUI",
			ui : "schoolQueryUI"
		}, 
	],
	uis : [
		{
			name : "schoolQueryUI",
			type : "setting",
			page : "query",
			service : {},
		},
	]
}
