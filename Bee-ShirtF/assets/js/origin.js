var app = angular.module('OriginApp', []);
app.service('originService', ['$http', function($http) {
    this.getOrigins = function(page) {
        return $http.get(`http://localhost:8080/api/origins/list?page=${page}`);
    };

    this.getOriginDetail = function(codeOrigin) {
        return $http.get(`http://localhost:8080/api/origins/detail/${codeOrigin}`);
    };

    this.addOrigin = function(origin) {
        return $http.post('http://localhost:8080/api/origins/add', origin);
    };

    this.updateOrigin = function(codeOrigin, origin) {
        return $http.put(`http://localhost:8080/api/origins/update/${codeOrigin}`, origin);
    };

    this.deleteOrigin = function(codeOrigin) {
        return $http.put(`http://localhost:8080/api/origins/delete/${codeOrigin}`);
    };
}]);

app.controller('originController', ['$scope', 'originService', function($scope, originService) {
    $scope.origins = [];
    $scope.currentPage = 0;
    $scope.totalPages = 0;
    $scope.pages = [];
    $scope.origin = {};
    $scope.newOrigin = {};
    $scope.confirmDelete = false;
    $scope.originToDelete = null;

    $scope.getBrands = function(page) {
        brandService.getBrands(page).then(function(response) {
            console.log(response.data.content);  // Kiểm tra dữ liệu trong console
            $scope.brands = response.data.content; // Lưu dữ liệu vào $scope.brands
            $scope.totalPages = response.data.totalPages;
            $scope.pages = new Array($scope.totalPages);
        });
    };
    
    
    // Chuyển đến trang mới
    $scope.goToPage = function(page) {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.currentPage = page;
            $scope.getOrigins($scope.currentPage);
        }
    };

    $scope.viewOriginDetail = function(codeOrigin) {
        originService.getOriginDetail(codeOrigin).then(function(response) {
            $scope.origin = response.data;
            $('#viewOriginDetailModal').modal('show');
        });
    };

    // Đóng modal
    $scope.closeModal = function(modalId) {
        $(`#${modalId}`).modal('hide');
    };

    $scope.openAddOriginModal = function() {
        $scope.newOrigin = {};
    };

    $scope.saveNewOrigin = function() {
        originService.addOrigin($scope.newOrigin).then(function(response) {
            $scope.getOrigins($scope.currentPage);
            $('#addOriginModal').modal('hide');
            location.reload(); 
        });
    };

    $scope.editOrigin = function(origin) {
        $scope.origin = angular.copy(origin);
        $('#editOriginModal').modal('show');
    };

    $scope.saveEditOrigin = function() {
        originService.updateOrigin($scope.origin.codeOrigin, $scope.origin).then(function(response) {
            $scope.getOrigins($scope.currentPage);
            $('#editOriginModal').modal('hide');
        });
    };

    $scope.deleteOrigin = function(codeOrigin) {
        $scope.originToDelete = codeOrigin;
        $('#confirmDeleteModal').modal('show');
    };

    $scope.confirmDeleteOrigin = function() {
        originService.deleteOrigin($scope.originToDelete).then(function(response) {
            $scope.getOrigins($scope.currentPage);
            $('#confirmDeleteModal').modal('hide');
        });
    };

    $scope.getOrigins($scope.currentPage);
}]);
