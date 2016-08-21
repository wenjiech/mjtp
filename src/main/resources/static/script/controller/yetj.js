
//'use strict';
//
//(function(myApp){

var yetjModule = angular.module('yetjModule', []);
    //应用渠道管理
<<<<<<< HEAD
yetjModule.controller('YetjListCtrl',['$scope', '$state',function($scope, $state){
		$scope.titles=["满记甜品","余额调节"];
		$scope.years=[2015,2016,2017,2018,2019];
		$scope.months=[{"show":"一月", "value":1},
		               {"show":"二月", "value":2},
		               {"show":"三月", "value":3},
		               {"show":"四月", "value":4},
		               {"show":"五月", "value":5},
		               {"show":"六月", "value":6},
		               {"show":"七月", "value":7},
		               {"show":"八月", "value":8},
		               {"show":"九月", "value":9},
		               {"show":"十月", "value":10},
		               {"show":"十一月", "value":11},
		               {"show":"十二月", "value":12}];
=======
	myApp.controller('yetjListCtrl',['$scope', '$state',function($scope, $state){
		console.log("1111111111yetjListCtrl");
		$scope.title1="mjtp1111";
		$scope.querytitle="querytitle11111";
>>>>>>> branch 'master' of https://github.com/wenjiech/mjtp.git
		
<<<<<<< HEAD
		
		$scope.queryData = function(){
			alert($scope.selectedYear);
			alert($scope.selectedMonth);
		}
		
		$scope.getData = function(){
			alert($scope.selectedYear);
			alert($scope.selectedMonth);
		}
		
		$scope.goToDetail = function()
		{
			alert(11111);
			$state.go("main.module.page", {"module": "yetj", "page" : "detail"});
		}
		
		$scope.myData = [
		                 { "no" : "012", "accountNo": "31001505400050030190", "T3Name":"天津建行付款户", "T3Code":"10026072", "T3Balance":849414.02, "bankBalance":1520761.88, "diffAmount":671347.86,"dataId":1},
		                 { "no" : "012", "accountNo": "31001505400050030190", "T3Name":"天津建行付款户", "T3Code":"10026072", "T3Balance":849414.02, "bankBalance":1520761.88, "diffAmount":671347.86,"dataId":2},
		                 { "no" : "012", "accountNo": "31001505400050030190", "T3Name":"天津建行付款户", "T3Code":"10026072", "T3Balance":849414.02, "bankBalance":1520761.88, "diffAmount":671347.86,"dataId":3},
		                 { "no" : "012", "accountNo": "31001505400050030190", "T3Name":"天津建行付款户", "T3Code":"10026072", "T3Balance":849414.02, "bankBalance":1520761.88, "diffAmount":671347.86,"dataId":4},
		                 { "no" : "012", "accountNo": "31001505400050030190", "T3Name":"天津建行付款户", "T3Code":"10026072", "T3Balance":849414.02, "bankBalance":1520761.88, "diffAmount":671347.86,"dataId":5}
		                ];
		
//	    $scope.gridOptions = { 
//	            data: 'myData',
//	            jqueryUITheme: true,
//	            columnDefs: [{field:'no', displayName:'帐套名称'}, 
//	                         {field:'accountNo', displayName:'账号'},
//	                         {field:'T3Name', displayName:'T3科目名称'},
//	                         {field:'T3Code', displayName:'T3科目编码'},
//	                         {field:'T3Balance', displayName:'T3账面余额'},
//	                         {field:'bankBalance', displayName:'银行账面余额'},
//	                         {field:'diffAmount', displayName:'差异金额'},
//	                         {field:'dataId', displayName:'明细',
////	                        	 cellTemplate: '<div><a ui-sref="main.module.page({module:yetj,page:detail})" id="{{row.getProperty(col.field)}}">明细</a></div>'}
//	                        	 cellTemplate: '<div class="ngCellText" ng-class="col.colIndex()"><span ng-cell-text ui-sref="main.module.page({module:yetj,page:detail})">明细</span></div>'}
//	                         ]
//	        };
		
		
		
	}])
	.controller('YetjDetailCtrl',['$scope', '$state',function($scope, $state, $routeParams){
		console.log("welecome YetjDetailCtrl");
		$scope.titles=["满记甜品","余额调节","详情"];
=======
		$scope.option_years=[2015,2016,2017,2018,2019];
		$scope.option_months=[{"show":"一月","value":"1"},
		                      {"show":"二月","value":"2"},
		                      {"show":"三月","value":"3"},
		                      {"show":"四月","value":"4"},
		                      {"show":"五月","value":"5"},
		                      {"show":"六月","value":"6"},
		                      {"show":"七月","value":"7"},
		                      {"show":"八月","value":"8"},
		                      {"show":"九月","value":"9"},
		                      {"show":"十月","value":"10"},
		                      {"show":"十一月","value":"11"},
		                      {"show":"十二月","value":"12"}];
>>>>>>> branch 'master' of https://github.com/wenjiech/mjtp.git
	}]);

//}(myApp));
