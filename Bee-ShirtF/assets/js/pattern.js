var app = angular.module('PatternApp', []);
app.service('patternService', ['$http', function($http) {
    this.getPatterns = function(page) {
        return $http.get(`http://localhost:8080/api/patterns/list?page=${page}`);
    };

    this.getPatternDetail = function(codePattern) {
        return $http.get(`http://localhost:8080/api/patterns/detail/${codePattern}`);
    };

    this.addPattern = function(pattern) {
        return $http.post('http://localhost:8080/api/patterns/add', pattern);
    };

    this.updatePattern = function(codePattern, pattern) {
        return $http.put(`http://localhost:8080/api/patterns/update/${codePattern}`, pattern);
    };

    this.deletePattern = function(codePattern) {
        return $http.put(`http://localhost:8080/api/patterns/delete/${codePattern}`);
    };
}]);

app.controller('patternController', ['$scope', 'patternService', function($scope, patternService) {
    $scope.patterns = [];
    $scope.currentPage = 0;
    $scope.totalPages = 0;
    $scope.pages = [];
    $scope.pattern = {};
    $scope.newPattern = {};
    $scope.confirmDelete = false;
    $scope.patternToDelete = null;

    $scope.getPatterns = function(page) {
        patternService.getPatterns(page).then(function(response) {
            $scope.patterns = response.data.content.map(function(item) {
                return {
                    codePattern: item[0],
                    namePattern: item[1],
                    statusPattern: item[2]
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
            $scope.getPatterns($scope.currentPage);
        }
    };

    $scope.viewPatternDetail = function(codePattern) {
        patternService.getPatternDetail(codePattern).then(function(response) {
            $scope.pattern = response.data;
            $('#viewPatternDetailModal').modal('show');
        });
    };

    // Đóng modal
    $scope.closeModal = function(modalId) {
        $(`#${modalId}`).modal('hide');
    };

    $scope.openAddPatternModal = function() {
        $scope.newPattern = {};
    };

    $scope.saveNewPattern = function() {
        patternService.addPattern($scope.newPattern).then(function(response) {
            $scope.getPatterns($scope.currentPage);
            $('#addPatternModal').modal('hide');
            location.reload(); 
        });
    };

    $scope.editPattern = function(pattern) {
        $scope.pattern = angular.copy(pattern);
        $('#editPatternModal').modal('show');
    };

    $scope.saveEditPattern = function() {
        patternService.updatePattern($scope.pattern.codePattern, $scope.pattern).then(function(response) {
            $scope.getPatterns($scope.currentPage);
            $('#editPatternModal').modal('hide');
        });
    };

    $scope.deletePattern = function(codePattern) {
        $scope.patternToDelete = codePattern;
        $('#confirmDeleteModal').modal('show');
    };

    $scope.confirmDeletePattern = function() {
        patternService.deletePattern($scope.patternToDelete).then(function(response) {
            $scope.getPatterns($scope.currentPage);
            $('#confirmDeleteModal').modal('hide');
        });
    };

    $scope.getPatterns($scope.currentPage);
}]);
