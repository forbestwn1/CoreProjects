{
	page : [
		{
			name : "listPage",
			uiResource : "Resource_MySchool_SchoolList"
		},
		{
			name : "infoPage",
			uiResource : "Resource_MySchool_SchoolData"
		}
	],
	
	context : {

			schoolList : {
				definition : "test.array;1.0.0%%||element:test.map;1.0.0%%||geo:test.geo;1.0.0,schoolName:test.string;1.0.0,schoolRating:test.float;1.0.0||%%||%%",
				default : {
					dataTypeId: "test.array;1.0.0",
					value: [
						{
							dataTypeId: "test.map;1.0.0",
							value: {
								schoolName : {
									dataTypeId: "test.string;1.0.0",
									value: "School1"
								},
								schoolRating : {
									dataTypeId: "test.float;1.0.0",
									value: 6.0
								},
								geo : {
									dataTypeId: "test.geo;1.0.0",
									value: {
										"latitude" :  43.651299,
										"longitude" : -79.579473
									}
								}
							}
						},
						{
							dataTypeId: "test.map;1.0.0",
							value: {
								schoolName : {
									dataTypeId: "test.string;1.0.0",
									value: "School2"
								},
								schoolRating : {
									dataTypeId: "test.float;1.0.0",
									value: 8.5
								},
								geo : {
									dataTypeId: "test.geo;1.0.0",
									value: {
										"latitude" :  43.649016, 
										"longitude" : -79.485059
									}
								}
							}
						}					
					]
				}
			},
	},
	init : [
		{
			name:"presentSchoolList",
			type:"presentUI",
			ui : "schoolListUI"
		}, 
	],
	uis : [
		{
			name : "schoolListUI",
			type : "list",
			page : "schoolList",
			service : {
			
			},
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
								schoolList : "event.parm.schoolData"
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
