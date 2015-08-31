var app = angular.module('shortLink', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'googlechart'
]);

app.config(function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'views/createShort.html',
        controller: 'CreateShort'
    }).when('/:param1/stats', {
        templateUrl: 'views/urlStatistics.html',
        controller: 'UrlStatisticsController'
    }).when('/topSites', {
        templateUrl: 'views/topSites.html',
        controller: 'TopSitesController'
    }).otherwise({
        templateUrl: 'views/long.html',
        controller: 'longUrl'
    })
});

app.controller('CreateShort', function ($scope, $http, $location, $cookieStore) {
    $scope.createUrl = function () {
        if (checkLongUrl($scope.URL.longURL)) {
            if (!$scope.URL.customURL) {
                $http.post('/api/v1/short', $scope.URL.longURL).success(function (data) {
                    var short = data['shortURL'];
                    short = short.replace(/\"/g, "");
                    $scope.URL.short = short;
                    $scope.URL.clicks = data['click'];
                    document.getElementById("buttonStatistics").style.visibility = 'visible';
                    document.getElementById("url-card").style.visibility = 'visible';
                    short = short.replace("http://localhost:8080/#/", "");
                    $scope.requestStatistics = '/#/' + short + '/stats';
                })
            } else {
                if (isABadWord($scope.URL.customURL)) {
                    Materialize.toast('Custom url not allowed due to bad words, please check it.', 5000);
                } else {
                    $http.post('/api/v1/shortCustom', $scope.URL).success(function (data) {
                    console.log(data)
                        if (data==='null') {
                            Materialize.toast('Word not available, try again', 5000);
                        } else {
                            var short = 'http://localhost:8080/#/' + $scope.URL.customURL;
                            $scope.URL.short = short;
                            $scope.URL.clicks = data['click'];
                            document.getElementById("buttonStatistics").style.visibility = 'visible';
                            document.getElementById("url-card").style.visibility = 'visible';
                            short = short.replace("http://localhost:8080/#/", "");
                            $scope.requestStatistics = '/#/' + short + '/stats';
                        }
                    })
                }
            }
        } else {
            Materialize.toast('Url not allowed, check it please.', 5000);
        }
    }

});

app.controller('UrlStatisticsController', function ($scope, $http, $routeParams, $location) {
    var param1 = 'http://localhost:8080/#/' + $routeParams.param1.replace("/stats", "");
    $http.get('/api/v1/url_statistics/', {params: {"param1": param1}}).success(function (data) {
    console.log(data)
        $scope.total_clicks = (data['statistiche'])['num'];
        $scope.shortURL = param1;
        $scope.longURL = data['longURL'];
        $scope.geoChart = geoChart((data['statistiche'])['statistichePaesi']);
        if (data['click'] == 0) {
            $scope.browserChart = barChart(null, 'Browser', '');
            $scope.platformChart = barChart(null, 'Platform', '');
        } else {
            $scope.browserChart = barChart((data['statistiche'])['statisticheBrowser'], 'Browser', 'Clicks');
            $scope.platformChart = barChart((data['statistiche'])['statisticheOS'], 'Platform', 'Clicks');
        }
    });


});

app.controller('TopSitesController', function ($scope, $http) {

    $http.get('/api/v1/top_sites').success(function(data) {
        console.log(data)
        var top = data['topTen'];
        var array = [];
        for(var index = 0; index < top.length; index ++) {
            array.push({longURL: top[index]['longURL'], click: top[index]['click']});
        }
        $scope.sites = array;
        $scope.geoChart = geoChart(data['statistichePaesi']);
        if(data['num'] == 0) {
            $scope.browserChart = barChart(null, 'Browser', '');
            $scope.platformChart = barChart(null, 'Platform', '');
        } else {
            $scope.browserChart = barChart(data['statisticheBrowser'], 'Browser', 'Clicks');
            $scope.platformChart = barChart(data['statisticheOS'], 'Platform', 'Clicks');
        }
    });

});


app.controller('longUrl', function ($scope, $http, $location) {

    $http.post('/api/v1/risultato', $location.absUrl()).success(function (data) {
        data = data.replace(/\"/g, "");
        location.href = data;
    })

});
