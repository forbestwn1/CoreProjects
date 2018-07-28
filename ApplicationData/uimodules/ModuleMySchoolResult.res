{
	resource : {
		schoolList : "Page_MySchool_Result_List",
		schoolInfo : "Page_MySchool_Result_Info",
	},
	context : {
		schools : {
			definition : "test.array;1.0.0%%||element:test.map;1.0.0%%||geo:test.geo;1.0.0,schoolName:test.string;1.0.0,schoolRating:test.float;1.0.0||%%||%%",
		}
	},
	constants : {
	
	},
	
	init : [
		{
			name:"presentUI"
			type:"presentUI",
			ui : "schoolListUI"
		}, 
		
		{
			name:"refreshSchoolList"
			type: "uiCommand",
			ui : "schoolListUI"
			command : "refresh",
			parms:{
				schoolList : {
					path : "schools"
				}
			}
		}
	],
	uis : [
		{
			name : "schoolListUI",
			type : "list",
			page : "schoolList",
			event : {
				"selectSchool : {
					task : [
						{
							name:"presentUI",
							type:"presentUI",
							action : "present.information",
							ui : "schoolInfoUI"
						}, 
						{
							name: "refreshSchoolInfo",
							type: "uiCommand",
							ui : "schoolInfoUI"
							command : "refresh",
							parms:{
								schoolData : {
									path : "schoolData"
								}
							}
						},
					]
				}
			}
		},
		{
			name : "schoolInfoUI",
			type : "info",
			page : "schoolInfo",
		}
	]
}
