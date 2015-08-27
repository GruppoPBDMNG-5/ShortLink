var app = angular.module('shortLink', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'googlechart'
]);

app.service('urlData', function() {
    var _dataObject = {};
    return {
        dataObject: _dataObject
    };
});

app.config(function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'views/createShort.html',
        controller: 'CreateShort'
    }).when('/urlStatistics', {
        templateUrl: 'views/urlStatistics.html',
        controller: 'UrlStatisticsController'
    }).otherwise({
        templateUrl: 'views/long.html',
        controller: 'longUrl'
    })
});

app.controller('CreateShort', function ($scope, $http, $location) {
    $scope.createUrl = function () {
            if(checkLongUrl($scope.URL.longURL)) {
                if (!$scope.URL.customURL) {
                        $http.post('/api/v1/short', $scope.URL.longURL).success(function (data) {
                            var short = data['shortURL'];
                            short = short.replace(/\"/g, "");
                            short = 'http://' + short;
                            $scope.URL.short = short;
                            $scope.URL.clicks = data['click'];
                            document.getElementById("buttonStatistics").style.visibility='visible';
                            document.getElementById("url-card").style.visibility='visible';
                            dataObject = data;
                        })
                } else {
                    if(isABadWord($scope.URL.customURL)) {
                        Materialize.toast('Custom url not allowed due to bad words, please check it.', 5000);
                    } else {
                        $http.post('/api/v1/shortCustom', $scope.URL).success(function (data) {
                            if (data == null) {
                                Materialize.toast('Word not available, try again', 5000);
                                dataObject = data;
                            } else {
                                var short = $scope.URL.customURL;
                                short = 'http://localhost/#/' + short;
                                $scope.URL.short = short;
                                $scope.URL.clicks = data['click'];
                                document.getElementById("buttonStatistics").style.visibility='visible';
                                document.getElementById("url-card").style.visibility='visible';
                                dataObject = data;
                            }
                        })
                }
            }
        } else {
                Materialize.toast('Url not allowed, check it please.', 5000);
        }
    }

    $scope.go = function(path) {
        $location.path(path);
    }

});

app.controller('UrlStatisticsController', function ($scope) {

    var countryStatistics = dataObject['statistichePaesi'];
    var chart1 = {};
    chart1.type = "GeoChart";
    chart1.data = [
        ['Country', 'Clicks']
    ];
    var item;
    for (var type in countryStatistics) {
        item = [];
        item = type;
        item = [item, countryStatistics[type]];
        chart1.data.push(item);
    }
    chart1.options = {};
    $scope.geoChart = chart1;
    $scope.total_clicks = dataObject['click'];

    var colors = ['red', 'blue', 'grey' , 'orange', 'green', 'purple', 'yellow', 'brown'];
    var browserStatistics = dataObject['statisticheBrowser'];
    var chart1 = {};
    chart1.type = "BarChart";
    chart1.data = [
        ['Browser','Clicks',{ role: 'style'}]
    ];
    var item;
    var index = 0;
    for(var type in browserStatistics) {
        item = [];
        item = type;
        if(index == 7)
            index = 0;
        item = [item, browserStatistics[type], colors[index++]];
        chart1.data.push(item);
    }
    chart1.options = {};
    $scope.browserChart = chart1;

    var platformStatistics = dataObject['statisticheOS'];
    var chart1 = {};
    chart1.type = "BarChart";
    chart1.data = [
        ['Platform','Clicks',{ role: 'style'}]
    ];
    var item;
    var index = 0;
    for(var type in platformStatistics) {
        item = [];
        item = type;
        if(index == 7)
            index = 0;
        item = [item, platformStatistics[type], colors[index++]];
        chart1.data.push(item);
    }
    chart1.options = {};
    $scope.platformChart = chart1;

});


app.controller('longUrl', function ($scope, $http, $location) {

    $http.post('/api/v1/risultato', $location.absUrl()).success(function (data) {
        data = data.replace(/\"/g, "");
        console.log(data);
        location.href = 'http://' + data;
    })


});
