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
    $scope.createUrl = function () {
        if (!$scope.URL.customURL) {
            if(checkLongUrl($scope.URL.longURL)) {
                $http.post('/api/v1/short', $scope.URL.longURL).success(function (data) {
                    $scope.URL.short = data;
                })
            } else {
                Materialize.toast('Url not allowed, check it please.', 5000)
            }

        } else {
            if(isABadWord($scope.URL.customURL)) {
                Materialize.toast('Custom url not allowed for bad words, please check it.', 5000)
            } else {
                $http.post('/api/v1/shortCustom', $scope.URL).success(function (data) {
                    if (data == '"fallito"') {
                        Materialize.toast('Parola non disponibile riprovare', 4000)
                        $scope.URL.short = '';
                    } else {
                        $scope.URL.short = data;
                    }
                 })
            }
        }
    }
});


app.controller('longUrl', function ($scope, $http, $location) {

    $http.post('/api/v1/risultato', $location.absUrl()).success(function (data) {
        data = data.replace(/\"/g, "");
        console.log(data);
        location.href = 'http://' + data;
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