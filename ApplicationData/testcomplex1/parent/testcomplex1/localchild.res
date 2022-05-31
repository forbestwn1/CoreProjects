{
	"extra": {
		"name": "localchild complex entity",
		"description": "localchild complex entity"
	},
	"entity": {
		"attachment": {
			"extra": {
				"status": "localchild complex entity",
				"name": "localchild complex entity" 
			},
			"entity": {
				"valuestructure" : [
				],
				"testsimple1": [
					{
						"name": "samename",
						"entity": {
							"localchild_none_testsimple1": {
								"entity":{
								}
							}
						}
					},
					{
						"name": "inlocalchildonly",
						"entity": {
							"localchild_none_testsimple1": {
								"entity":{
								}
							}
						}
					}
				],
				"testcomplex1": [
				
				]
			}
		},
		"valueStructure": {
			"entity": [
				{
					"groupType" : "public",
					"valueStructure" : {
						"parent1": {
							"definition": {
								"path" : "parent1",
								"definition" : {
									"criteria" : "test.string;1.0.0"
								}
							}
						}
					}
				}
			]
		}
	}
}
