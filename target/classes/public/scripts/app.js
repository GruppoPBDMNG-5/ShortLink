/**
 * Created by shekhargulati on 10/06/14.
 */

var app = angular.module('shortLink', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute'
]);

app.config(function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'views/createShort.html',
        controller: 'CreateShort'
    }).when('/create', {
        templateUrl: 'views/create.html',
        controller: 'CreateCtrl'
    }).otherwise({
       templateUrl: 'views/long.html',
       controller: 'longUrl'
    })
});

app.controller('CreateShort', function ($scope, $http) {
    $scope.createUrl=function(){
	$http.post('/api/v1/short',$scope.URL.longURL).success(function(data){
	$scope.URL.short=data;
	})

	}


});
app.controller('longUrl',function($scope,$http,$location){

$http.post('/api/v1/risultato',$location.absUrl()).success(function(data){
data=data.replace(/\"/g,"");
console.log(data);
location.href='http://'+data;
})






});
app.controller('CreateCtrl', function ($scope, $http, $location) {

    $scope.createUrl = function () {
        $http.post('/api/v1/personas', $scope.URL).success(function (data) {
            $location.path('/');
        }).error(function (data, status) {
            console.log('Error ' + data)
        })
    }
});