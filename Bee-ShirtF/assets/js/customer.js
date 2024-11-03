angular.module("customerApp", []).controller("CustomerController", [
  "$scope",
  "$http",
  function ($scope, $http) {
    // Khởi tạo biến để kiểm soát hiển thị form tạo tài khoản
    $scope.showCreateAccountForm = false; // Biến này điều khiển hiển thị biểu mẫu

    // Dữ liệu
    $scope.customerList = [];
    $scope.filteredCustomerList = []; // Danh sách nhân viên đã lọc
    $scope.errorMessage = ""; // Để lưu thông báo lỗi
    $scope.successMessage = ""; // Để lưu thông báo thành công

    $scope.availableRoles = ["ADMIN", "STAFF", "USER"]; // Danh sách role có sẵn

    // Hàm kiểm tra quyền
    function checkPermission() {
      const token = sessionStorage.getItem("jwtToken");
      if (!token) {
        alert("Bạn chưa đăng nhập!");
        window.location.href = "/assets/account/login.html";
        return false;
      }

      const payload = JSON.parse(atob(token.split(".")[1]));
      const highestRole = getHighestRole(payload.scope);

      if (highestRole !== "ROLE_ADMIN") {
        alert("Bạn không có quyền truy cập vào trang này!");
        window.location.href = "/assets/BanHang.html";
        return false;
      }

      return true;
    }

    if (!checkPermission()) return;

    function getHighestRole(scopes) {
      const roles = scopes ? scopes.split(" ") : [];
      const rolePriority = {
        ROLE_ADMIN: 1,
        ROLE_STAFF: 2,
        ROLE_USER: 3,
      };

      const validRoles = roles.filter((role) => rolePriority[role]);
      validRoles.sort((a, b) => rolePriority[a] - rolePriority[b]);

      return validRoles[0] || null;
    }

    // Hàm lấy danh sách tài khoản
    $scope.getCustomers = function () {
      const token = sessionStorage.getItem("jwtToken");

      $http({
        method: "GET",
        url: "http://localhost:8080/admin/clients",
        headers: {
          Authorization: "Bearer " + token,
        },
      })
        .then(function (response) {
          if (Array.isArray(response.data.result)) {
            $scope.customerList = response.data.result.map((customer) => ({
              code: customer.code,
              fullName: `${customer.firstName} ${customer.lastName}`,
              username: customer.username,
              email: customer.email,
              phone: customer.phone,
              avatar: customer.avatar,
              address: customer.address,
              status: customer.status,
            }));

            $scope.filteredCustomerList = $scope.customerList;
          } else {
            console.error(
              "Dữ liệu không phải là một mảng:",
              response.data.result
            );
          }
        })
        .catch(function (error) {
          console.error("Lỗi khi lấy danh sách tài khoản:", error);
          $scope.errorMessage = "Không thể lấy danh sách tài khoản.";
        });
    };

    // Hàm xác thực dữ liệu
    function validateForm() {
      const phoneRegex = /^(0[3|5|7|8|9])[0-9]{8}$/;
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

      if (!$scope.user.firstName || !$scope.user.lastName) {
        $scope.errorMessage = "Tên và họ không được để trống.";
        return false;
      }
      if (!$scope.user.phone || !$scope.user.phone.match(phoneRegex)) {
        $scope.errorMessage = "Số điện thoại không hợp lệ.";
        return false;
      }
      if (!$scope.user.email || !$scope.user.email.match(emailRegex)) {
        $scope.errorMessage = "Email không hợp lệ.";
        return false;
      }
      if ($scope.user.username.length < 3) {
        $scope.errorMessage = "Tên đăng nhập phải lớn hơn 3 ký tự.";
        return false;
      }
      if ($scope.user.pass.length < 5) {
        $scope.errorMessage = "Mật khẩu phải lớn hơn 5 ký tự.";
        return false;
      }

      $scope.errorMessage = "";
      return true;
    }

    $scope.deleteAccount = function (code) {
      if (confirm("Are you sure you want to delete this account?")) {
        const token = sessionStorage.getItem("jwtToken");

        // Khởi tạo biến trạng thái để tắt nút khi đang xóa
        $scope.isDeleting = true;

        $http({
          method: "DELETE",
          url: `http://localhost:8080/admin/delete/${code}`, // URL API cho chức năng xóa
          headers: {
            Authorization: "Bearer " + token,
          },
        })
          .then(function (response) {
            $scope.successMessage = "Account deleted successfully!";
            $scope.errorMessage = ""; // Xóa thông báo lỗi (nếu có)
            $scope.getCustomers(); // Cập nhật lại danh sách nhân viên sau khi xóa
            $scope.isDeleting = false; // Kích hoạt lại nút sau khi hoàn tất

            // Tự động ẩn thông báo sau 3 giây
            setTimeout(function () {
              $scope.successMessage = "";
              $scope.$apply();
            }, 3000);
          })
          .catch(function (error) {
            console.error("Error deleting account:", error);
            $scope.errorMessage = "Failed to delete account.";
            $scope.successMessage = ""; // Xóa thông báo thành công (nếu có)
            $scope.isDeleting = false; // Kích hoạt lại nút sau khi gặp lỗi
          });
      }
    };

    // Hàm tìm kiếm
    $scope.searchCustomer = function () {
      if (!$scope.searchQuery) {
        $scope.filteredCustomerList = $scope.customerList;
      } else {
        $scope.filteredCustomerList = $scope.customerList.filter(function (
          customer
        ) {
          return (
            customer.code
              .toLowerCase()
              .includes($scope.searchQuery.toLowerCase()) ||
            customer.username
              .toLowerCase()
              .includes($scope.searchQuery.toLowerCase()) ||
            customer.email
              .toLowerCase()
              .includes($scope.searchQuery.toLowerCase()) ||
            customer.phone.includes($scope.searchQuery) ||
            customer.address
              .toLowerCase()
              .includes($scope.searchQuery.toLowerCase())
          );
        });
      }
    };

    // Gọi các hàm khởi tạo
    $scope.getCustomers();
  },
]);
