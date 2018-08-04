{
	context : {
		criteria : {
			path : "uis.query.criteria"
		},
	},
	constants : {
	
	},
	
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
