
var app = angular.module('beeShirtDetail', []);
// Lấy giá trị tham số 'codeShirt' từ URL
const urlParams = new URLSearchParams(window.location.search);
const codeShirt = urlParams.get('codeShirt');

// In ra giá trị codeShirt
console.log(codeShirt);  // Kết quả: "S387821"

app.service('shirtDetailService', ['$http', function($http) {
    const baseUrl = 'http://localhost:8080/shirt-details';

    
// Hàm gọi API lấy chi tiết áo thun
this.getShirtDetails = function() {
    return $http.get(baseUrl + '/api/hienthi');
};

 
// Hàm gọi API lấy chi tiết áo thun
this.getShirtDetailsByCode = function() {
    return $http.get(baseUrl + '/byCode/' + codeShirt);
};


  // Hàm kiểm tra và gọi API tương ứng
  this.getShirtDetailsBasedOnCode = function() {
    // Kiểm tra nếu codeShirt có giá trị, gọi API theo mã
    if (codeShirt) {
        return this.getShirtDetailsByCode(codeShirt);
    } else {
        // Nếu không có codeShirt, gọi API lấy tất cả chi tiết áo thun
        return this.getShirtDetails();
    }
};

    this.saveVariants = function(savedVariants) {
        return $http.post(baseUrl + '/add', savedVariants); 
    };

    this.updateShirtDetail = function(codeShirtDetail, shirtdetail) {
        return $http.put(baseUrl + '/update/' + codeShirtDetail, shirtdetail);
    };

    this.deleteShirtDetail = function(codeshirtdetail) {
        return $http.delete(baseUrl + '/delete/' + codeshirtdetail);
    };

    this.getColors = function() {
        return $http.get(baseUrl + '/api/colors');
    };

    this.getGenders = function() {
        return $http.get(baseUrl + '/api/genders');
    };

    this.getMaterials = function() {
        return $http.get(baseUrl + '/api/materials');
    };

    this.getOrigins = function() {
        return $http.get(baseUrl + '/api/origins');
    };

    this.getPatterns = function() {
        return $http.get(baseUrl + '/api/patterns');
    };

    this.getSeasons = function() {
        return $http.get(baseUrl + '/api/seasons');
    };

    this.getSizes = function() {
        return $http.get(baseUrl + '/api/sizes');
    };
    this.getShirts = function() {
        return $http.get(baseUrl + '/api/shirts');
    };
}]);

app.controller('ShirtDetailController', ['$scope', 'shirtDetailService', function($scope, shirtDetailService) {
    $scope.shirtDetails = [];
    $scope.colors = [];
    $scope.genders = [];
    $scope.materials = [];
    $scope.origins = [];
    $scope.patterns = [];
    $scope.seasons = [];
    $scope.sizes = [];
    $scope.shirts = [];
    $scope.selectedColors = [];
    $scope.selectedSizes = [];
    $scope.variants = [];
    $scope.savedVariants = [];

    $scope.editingShirtDetail = null; // Để lưu thông tin áo thun đang được chỉnh sửa
    $scope.newShirtDetail = {};
    $scope.currentPage = 1;
    $scope.itemsPerPage = 10;

    // Số mục trên mỗi trang
    $scope.itemsPerPage = 10;
    // Biến lưu trữ số trang hiện tại
    $scope.currentPage = 1;
    
    // Hàm gọi API lấy danh sách chi tiết áo thun (tùy theo có codeShirt hay không)
    $scope.getShirtDetails = function() {
        shirtDetailService.getShirtDetailsBasedOnCode().then(function(response) {
            $scope.shirtDetails = response.data;
            $scope.currentPage = 1;  // Đặt lại trang về trang đầu tiên mỗi khi gọi API
        }).catch(function(error) {
            console.error('Có lỗi xảy ra:', error);
        });
    };

    // Phương thức phân trang: Lấy danh sách chi tiết áo thun cho trang hiện tại
    $scope.getShirtDetailsForCurrentPage = function() {
        const startIndex = ($scope.currentPage - 1) * $scope.itemsPerPage;
        const endIndex = startIndex + $scope.itemsPerPage;
        return $scope.shirtDetails.slice(startIndex, endIndex);
    };

    // Hàm để tính số thứ tự (STT) cho mỗi mục trên trang hiện tại
    $scope.getShirtIndex = function(index) {
        return ($scope.currentPage - 1) * $scope.itemsPerPage + (index + 1);
    };
   
    $scope.toggleColorSelection = function(colorId) {
        var index = $scope.selectedColors.indexOf(colorId);
        if (index > -1) {
            $scope.selectedColors.splice(index, 1);
        } else {
            $scope.selectedColors.push(colorId);
        }
    };

    $scope.toggleSizeSelection = function(sizeId) {
        var index = $scope.selectedSizes.indexOf(sizeId);
        if (index > -1) {
            $scope.selectedSizes.splice(index, 1);
        } else {
            $scope.selectedSizes.push(sizeId);
        }
    };

    $scope.generateVariants = function() {
        $scope.variants = [];
        angular.forEach($scope.selectedColors, function(colorId) {
            angular.forEach($scope.selectedSizes, function(sizeId) {
                $scope.variants.push({ 
                    colorId: colorId, 
                    sizeId: sizeId,
                    quantity: 0 ,
                    price: 0
                });
            });
        });
        
    };
        $scope.saveAllVariants = function() {
        angular.forEach($scope.variants, function(variant) {
            if (variant.quantity > 0) {
                var existingVariant = $scope.savedVariants.find(v => v.colorId === variant.colorId && v.sizeId === variant.sizeId);
                if (existingVariant) {
                    existingVariant.quantity = variant.quantity;
                } else {
                    // Đảm bảo materialId được lưu đúng
                    $scope.savedVariants.push({
                        shirtId: $scope.newShirtDetail.shirt.id,  // Lưu tên sản phẩm
                        colorId: variant.colorId,
                        sizeId: variant.sizeId,
                        quantity: variant.quantity,
                        materialId: $scope.newShirtDetail.material.id, // Lưu materialId đã chọn
                        genderId: $scope.newShirtDetail.gender.id,
                        originId: $scope.newShirtDetail.origin.id,
                        patternId: $scope.newShirtDetail.pattern.id,
                        seasonId: $scope.newShirtDetail.season.id,
                        price: variant.price,

                    });
                }
            }
        });

        // Mở modal "saveVariantsModal" (Modal Lưu Biến Thể)
        var saveModal = new bootstrap.Modal(document.getElementById('saveVariantsModal'));
        saveModal.show();
        
    };
        $scope.submitVariants = function() {
    console.log("Dữ liệu sẽ được gửi lên backend:", $scope.savedVariants); // Kiểm tra dữ liệu trước khi gửi

    shirtDetailService.saveVariants($scope.savedVariants).then(function(response) {
        // Xử lý sau khi gửi thành công
        alert("Biến thể đã được lưu vào danh sách thành công.");

        console.log("Biến thể đã được lưu thành công:", response.data);
        $('#addVariantModal').modal('hide');  // Đóng modal form nhập liệu
        $('#saveVariantsModal').modal('hide');
        location.reload(); // Tải lại toàn bộ trang

    }, function(error) {
        // Xử lý lỗi khi gửi
        console.error("Có lỗi khi lưu biến thể:", error);
    });
};


    $scope.editShirtDetail = function(shirtDetail) {
        $scope.newShirtDetail = angular.copy(shirtDetail);
    };

    $scope.deleteShirtDetail = function(codeShirtDetail) {
        shirtDetailService.deleteShirtDetail(codeShirtDetail).then(function() {
            $scope.getShirtDetails();
        });
    };

    $scope.getColors = function() {
        shirtDetailService.getColors().then(function(response) {
            $scope.colors = response.data;
        });
    };

    $scope.getGenders = function() {
        shirtDetailService.getGenders().then(function(response) {
            $scope.genders = response.data;
        });
    };

    $scope.getMaterials = function() {
        shirtDetailService.getMaterials().then(function(response) {
            $scope.materials = response.data;
        });
    };

    $scope.getOrigins = function() {
        shirtDetailService.getOrigins().then(function(response) {
            $scope.origins = response.data;
        });
    };

    $scope.getPatterns = function() {
        shirtDetailService.getPatterns().then(function(response) {
            $scope.patterns = response.data;
        });
    };

    $scope.getSeasons = function() {
        shirtDetailService.getSeasons().then(function(response) {
            $scope.seasons = response.data;
        });
    };

    $scope.getSizes = function() {
        shirtDetailService.getSizes().then(function(response) {
            $scope.sizes = response.data;
        });
    };
    $scope.getShirts = function() {
        shirtDetailService.getShirts().then(function(response) {
            $scope.shirts = response.data;
        });
    };
        $scope.editShirtDetail = function(shirtdetail) {
    // Sao chép đối tượng chi tiết áo thun để tránh thay đổi trực tiếp
        $scope.editingShirtDetail = angular.copy(shirtdetail);

        //chuyển dữ liệu các dropdown thuộc tính
        $scope.editingShirtDetail.colorId=$scope.editingShirtDetail.color.id;
        $scope.editingShirtDetail.genderId=$scope.editingShirtDetail.gender.id;
        $scope.editingShirtDetail.materialId=$scope.editingShirtDetail.material.id;
        $scope.editingShirtDetail.originId=$scope.editingShirtDetail.origin.id;
        $scope.editingShirtDetail.shirtId=$scope.editingShirtDetail.shirt.id;
        $scope.editingShirtDetail.patternId=$scope.editingShirtDetail.pattern.id;
        $scope.editingShirtDetail.seasonId=$scope.editingShirtDetail.season.id;
        $scope.editingShirtDetail.sizeId=$scope.editingShirtDetail.size.id;

        //các trạng thái status, deleted
        $scope.editingShirtDetail.statusshirtdetail=$scope.editingShirtDetail.statusshirtdetail;
        $scope.editingShirtDetail.deleted=$scope.editingShirtDetail.deleted;
    };


    $scope.updateShirtDetail = function() {
    // Sao chép dữ liệu từ đối tượng editShirtDetail
        let updateShirtDetailed = angular.copy($scope.editingShirtDetail);

        // Chuyển các ID thuộc tính thành đối tượng
        updateShirtDetailed.color = { id: updateShirtDetailed.colorId };
        updateShirtDetailed.gender = { id: updateShirtDetailed.genderId };
        updateShirtDetailed.material = { id: updateShirtDetailed.materialId };
        updateShirtDetailed.origin = { id: updateShirtDetailed.originId };
        updateShirtDetailed.shirt = { id: updateShirtDetailed.shirtId };
        updateShirtDetailed.pattern = { id: updateShirtDetailed.patternId };
        updateShirtDetailed.season = { id: updateShirtDetailed.seasonId };
        updateShirtDetailed.size = { id: updateShirtDetailed.sizeId };

        // Cập nhật trạng thái và đã xóa

        // Gửi yêu cầu cập nhật chi tiết áo thun
        shirtDetailService.updateShirtDetail(updateShirtDetailed.codeShirtDetail, updateShirtDetailed).then(function() {

        $scope.editingShirtDetail = null;
        $scope.getShirtDetails();

        }, function(error) {
        console.error("Error updating shirt", error);
    });
    };


    $scope.deleteShirtDetail = function(codeShirtDetail) {
        console.log("Deleting shirt detail with code:", codeShirtDetail); // Debugging
        if (confirm('Bạn có chắc chắn muốn xóa áo này không?')) {
            shirtDetailService.deleteShirtDetail(codeShirtDetail).then(function() {
                $scope.getShirtDetails();
            }).catch(function(error) {
                alert('Có lỗi xảy ra khi xóa áo thun: ' + error.message);
            });
        }
    };
     // Hàm để lấy tên màu sắc theo ID
     $scope.getColorName = function(colorId) {
        var color = $scope.colors.find(c => c.id === colorId);
        return color ? color.nameColor : '';
    };

    // Hàm để lấy tên kích thước theo ID
    $scope.getSizeName = function(sizeId) {
        var size = $scope.sizes.find(s => s.id === sizeId);
        return size ? size.namesize : '';
    };
    // Hàm để lấy tên màu sắc theo ID
    $scope.getMaterialName = function(materialId) {
        var material = $scope.materials.find(m => m.id === materialId);
        return material ? material.nameMaterial : '';
    };

    // Initial data fetch
    $scope.getShirtDetails();
    $scope.getColors();
    $scope.getGenders();
    $scope.getMaterials();
    $scope.getPatterns();
    $scope.getSeasons();
    $scope.getOrigins();

    $scope.getSizes();
    $scope.getShirts();

}]);