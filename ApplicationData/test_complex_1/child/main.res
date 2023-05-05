{
	"extra": {
		"name": "child complex entity",
		"description": "childcomplex"
	},
	"entity": {
		"localresource-none-testcomplex1": {
			"resourceId": "testcomplex1|#localchild",
			"extra": {
				"status": "disabled1"
			}
		},
		"attachment": {
			"extra": {
				"status": "disabled1",
				"name": "child complex entity" 
			},
			"entity": {
				"valuestructure": [
				],
				"testsimple1": [
					{
						"name": "samename",
						"entity": {
							"child_none_testsimple1": {
								"entity": {
								}
							}
						}
					},
					{
						"name": "inchildonly",
						"entity": {
							"child_none_testsimple1": {
								"entity": {
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
