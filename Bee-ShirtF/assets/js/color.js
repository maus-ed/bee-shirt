var app = angular.module('ColorApp', []);
app.service('colorService', ['$http', function($http) {
    this.getColors = function(page) {
        return $http.get(`http://localhost:8080/api/colors/list?page=${page}`);
    };

    this.getColorDetail = function(codeColor) {
        return $http.get(`http://localhost:8080/api/colors/detail/${codeColor}`);
    };

    this.addColor = function(color) {
        return $http.post('http://localhost:8080/api/colors/add', color);
    };

    this.updateColor = function(codeColor, color) {
        return $http.put(`http://localhost:8080/api/colors/update/${codeColor}`, color);
    };

    this.deleteColor = function(codeColor) {
        return $http.put(`http://localhost:8080/api/colors/delete/${codeColor}`);
    };
}]);

app.controller('colorController', ['$scope', 'colorService', function($scope, colorService) {
    $scope.colors = [];
    $scope.currentPage = 0;
    $scope.totalPages = 0;
    $scope.pages = [];
    $scope.color = {};
    $scope.newColor = {};
    $scope.confirmDelete = false;
    $scope.colorToDelete = null;

    $scope.getColors = function(page) {
        colorService.getColors(page).then(function(response) {
            $scope.colors = response.data.content.map(function(item) {
                return {
                    codeColor: item[0],
                    nameColor: item[1],
                    statusColor: item[2]
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
            $scope.getColors($scope.currentPage);
        }
    };

    $scope.viewColorDetail = function(codeColor) {
        colorService.getColorDetail(codeColor).then(function(response) {
            $scope.color = response.data;
            $('#viewColorDetailModal').modal('show');
        });
    };
  // Close modal function
  $scope.closeModal = function(modalId) {
    $(`#${modalId}`).modal('hide');
};
    $scope.openAddColorModal = function() {
        $scope.newColor = {};
    };

    $scope.saveNewColor = function() {
        colorService.addColor($scope.newColor).then(function(response) {
            $scope.getColors($scope.currentPage);
            $('#addColorModal').modal('hide');
            location.reload(); 

        });
    };

    $scope.editColor = function(color) {
        $scope.color = angular.copy(color);
        $('#editColorModal').modal('show');

    };

    $scope.saveEditColor = function() {
        colorService.updateColor($scope.color.codeColor, $scope.color).then(function(response) {
            $scope.getColors($scope.currentPage);
            $('#editColorModal').modal('hide');
        });
    };

    $scope.deleteColor = function(codeColor) {
        $scope.colorToDelete = codeColor;
        $('#confirmDeleteModal').modal('show');
    };

    $scope.confirmDeleteColor = function() {
        colorService.deleteColor($scope.colorToDelete).then(function(response) {
            $scope.getColors($scope.currentPage);
            $('#confirmDeleteModal').modal('hide');
        });
    };

    $scope.getColors($scope.currentPage);
}]);
