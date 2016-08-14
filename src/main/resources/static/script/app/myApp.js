var myApp = angular.module('myApp', ['ui.router']);

myApp.config(function ($stateProvider, $urlRouterProvider) {
	
	// 默认访问路径
	$urlRouterProvider.when("","/main");
	
	//路由配置
	$stateProvider
    .state("main", {
        url: "/main",
        templateUrl: "views/main.html"
    })
    .state('main.module', {
        url: '/{module}',
        views:{
            "main": {
                templateUrl: function ($stateParams) {
                    return "views/" + $stateParams.module + "/index.html";
                }
            }
        }
    })
    .state('main.module.page', {
        url: '/{page}',
        templateUrl: function ($stateParams) {
            return "views/"+$stateParams.module+"/" + $stateParams.page + ".html";
        }
    })
    
});

//暂时为空，之后可以处理一些公共的逻辑
myApp.controller('DispatcherCtrl',[]);


//myApp.config(function($routeProvider){
//	
//	$routeProvider
//    .when('/',{
//        templateUrl : 'views/checkaccount_list.html'
//    }).otherwise({ redirectTo: '/' });
//
//});
