var app = angular.module('SeasonApp', []);
app.service('seasonService', ['$http', function($http) {
    this.getSeasons = function(page) {
        return $http.get(`http://localhost:8080/api/seasons/list?page=${page}`);
    };

    this.getSeasonDetail = function(codeSeason) {
        return $http.get(`http://localhost:8080/api/seasons/detail/${codeSeason}`);
    };

    this.addSeason = function(season) {
        return $http.post('http://localhost:8080/api/seasons/add', season);
    };

    this.updateSeason = function(codeSeason, season) {
        return $http.put(`http://localhost:8080/api/seasons/update/${codeSeason}`, season);
    };

    this.deleteSeason = function(codeSeason) {
        return $http.put(`http://localhost:8080/api/seasons/delete/${codeSeason}`);
    };
}]);
app.controller('seasonController', ['$scope', 'seasonService', function($scope, seasonService) {
    $scope.seasons = [];
    $scope.currentPage = 0;
    $scope.totalPages = 0;
    $scope.pages = [];
    $scope.season = {};
    $scope.newSeason = {};
    $scope.confirmDelete = false;
    $scope.seasonToDelete = null;

    $scope.getSeasons = function(page) {
        seasonService.getSeasons(page).then(function(response) {
            $scope.seasons = response.data.content.map(function(item) {
                return {
                    codeSeason: item[0],
                    nameSeason: item[1],
                    statusSeason: item[2]
                };
            });
            $scope.totalPages = response.data.totalPages;
            $scope.pages = new Array($scope.totalPages);
        });
    };

    // Chuyển đến trang mới
    $scope.goToPage = function(page) {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.currentPage = page;
            $scope.getSeasons($scope.currentPage);
        }
    };

    $scope.viewSeasonDetail = function(codeSeason) {
        seasonService.getSeasonDetail(codeSeason).then(function(response) {
            $scope.season = response.data;
            $('#viewSeasonDetailModal').modal('show');
        });
    };

    // Đóng modal
    $scope.closeModal = function(modalId) {
        $(`#${modalId}`).modal('hide');
    };

    $scope.openAddSeasonModal = function() {
        $scope.newSeason = {};
    };

    $scope.saveNewSeason = function() {
        seasonService.addSeason($scope.newSeason).then(function(response) {
            $scope.getSeasons($scope.currentPage);
            $('#addSeasonModal').modal('hide');
            location.reload(); 
        });
    };

    $scope.editSeason = function(season) {
        $scope.season = angular.copy(season);
        $('#editSeasonModal').modal('show');
    };

    $scope.saveEditSeason = function() {
        seasonService.updateSeason($scope.season.codeSeason, $scope.season).then(function(response) {
            $scope.getSeasons($scope.currentPage);
            $('#editSeasonModal').modal('hide');
        });
    };

    $scope.deleteSeason = function(codeSeason) {
        $scope.seasonToDelete = codeSeason;
        $('#confirmDeleteModal').modal('show');
    };

    $scope.confirmDeleteSeason = function() {
        seasonService.deleteSeason($scope.seasonToDelete).then(function(response) {
            $scope.getSeasons($scope.currentPage);
            $('#confirmDeleteModal').modal('hide');
        });
    };

    $scope.getSeasons($scope.currentPage);
}]);
