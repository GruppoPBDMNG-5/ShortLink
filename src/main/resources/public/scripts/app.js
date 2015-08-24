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
            if(checkLongUrl($scope.URL.longURL)) {
                if (!$scope.URL.customURL) {
                        $http.post('/api/v1/short', $scope.URL.longURL).success(function (data) {
                            data = data.replace(/\"/g, "");
                            data = 'http://' + data;
                            $scope.URL.short = data;
                        })
                } else {
                    if(isABadWord($scope.URL.customURL)) {
                        Materialize.toast('Custom url not allowed due to bad words, please check it.', 5000)
                    } else {
                        $http.post('/api/v1/shortCustom', $scope.URL).success(function (data) {
                            if (data == '"fallito"') {
                                Materialize.toast('Word not available, try again', 5000)
                                $scope.URL.short = '';
                            } else {
                                data = data.replace(/\"/g, "");
                                data = 'http://' + data;
                                $scope.URL.short = data;
                            }
                        })
                }
            }
        } else {
                Materialize.toast('Url not allowed, check it please.', 5000)
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
