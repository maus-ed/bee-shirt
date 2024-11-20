var app = angular.module('GenderApp', []);
app.service('genderService', ['$http', function($http) {
    this.getGenders = function(page) {
        return $http.get(`http://localhost:8080/api/genders/list?page=${page}`);
    };

    this.getGenderDetail = function(codeGender) {
        return $http.get(`http://localhost:8080/api/genders/detail/${codeGender}`);
    };

    this.addGender = function(gender) {
        return $http.post('http://localhost:8080/api/genders/add', gender);
    };

    this.updateGender = function(codeGender, gender) {
        return $http.put(`http://localhost:8080/api/genders/update/${codeGender}`, gender);
    };

    this.deleteGender = function(codeGender) {
        return $http.put(`http://localhost:8080/api/genders/delete/${codeGender}`);
    };
}]);

app.controller('genderController', ['$scope', 'genderService', function($scope, genderService) {
    $scope.genders = [];
    $scope.currentPage = 0;
    $scope.totalPages = 0;
    $scope.pages = [];
    $scope.gender = {};
    $scope.newGender = {};
    $scope.confirmDelete = false;
    $scope.genderToDelete = null;

    $scope.getGenders = function(page) {
        genderService.getGenders(page).then(function(response) {
            $scope.genders = response.data.content.map(function(item) {
                return {
                    codeGender: item[0],
                    nameGender: item[1],
                    statusGender: item[2]
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
            $scope.getGenders($scope.currentPage);
        }
    };

    $scope.viewGenderDetail = function(codeGender) {
        genderService.getGenderDetail(codeGender).then(function(response) {
            $scope.gender = response.data;
            $('#viewGenderDetailModal').modal('show');
        });
    };

    // Đóng modal
    $scope.closeModal = function(modalId) {
        $(`#${modalId}`).modal('hide');
    };

    $scope.openAddGenderModal = function() {
        $scope.newGender = {};
    };

    $scope.saveNewGender = function() {
        genderService.addGender($scope.newGender).then(function(response) {
            $scope.getGenders($scope.currentPage);
            $('#addGenderModal').modal('hide');
            location.reload(); 
        });
    };

    $scope.editGender = function(gender) {
        $scope.gender = angular.copy(gender);
        $('#editGenderModal').modal('show');
    };

    $scope.saveEditGender = function() {
        genderService.updateGender($scope.gender.codeGender, $scope.gender).then(function(response) {
            $scope.getGenders($scope.currentPage);
            $('#editGenderModal').modal('hide');
        });
    };

    $scope.deleteGender = function(codeGender) {
        $scope.genderToDelete = codeGender;
        $('#confirmDeleteModal').modal('show');
    };

    $scope.confirmDeleteGender = function() {
        genderService.deleteGender($scope.genderToDelete).then(function(response) {
            $scope.getGenders($scope.currentPage);
            $('#confirmDeleteModal').modal('hide');
        });
    };

    $scope.getGenders($scope.currentPage);
}]);
