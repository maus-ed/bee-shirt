angular.module("adminApp", []).controller("AdminController", [
  "$scope",
  "$http",
  function ($scope, $http) {
    // Dữ liệu
    $scope.roles = [];
    $scope.accounts = [];
    $scope.newAccount = {};
    $scope.errorMessage = ""; // Để lưu thông báo lỗi
    $scope.successMessage = ""; // Để lưu thông báo thành công

    // Hàm kiểm tra quyền
    function checkPermission() {
      const token = sessionStorage.getItem("jwtToken");
      if (!token) {
        alert("Bạn chưa đăng nhập!"); // Thông báo nếu chưa có token
        return false; // Không có quyền truy cập
      }

      // Giải mã token để lấy quyền
      const payload = JSON.parse(atob(token.split(".")[1]));
      const highestRole = getHighestRole(payload.scope);

      // Kiểm tra xem người dùng có phải là admin không
      if (highestRole !== "ROLE_ADMIN") {
        alert("Bạn không có quyền truy cập vào trang này!"); // Thông báo không đủ quyền
        window.location.href = "/page/staff/staff.html";
        return false; // Không có quyền truy cập
      }

      return true; // Có quyền truy cập
    }

    // Kiểm tra quyền truy cập khi khởi tạo controller
    if (!checkPermission()) return;

    // Hàm lấy quyền cao nhất
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

    // Hàm lấy danh sách vai trò
    $scope.getRoles = function () {
      const token = sessionStorage.getItem("jwtToken"); // Lấy token từ sessionStorage
      console.log("Token trước khi gửi yêu cầu:", token); // In token ra console

      $http({
        method: "GET",
        url: "http://localhost:8080/admin/roles",
        headers: {
          Authorization: "Bearer " + token, // Lấy token từ sessionStorage
        },
      })
        .then(function (response) {
          $scope.roles = response.data.result; // Lưu kết quả vào $scope.roles
        })
        .catch(function (error) {
          console.error("Lỗi khi lấy danh sách vai trò:", error); // In lỗi ra console
          $scope.errorMessage = "Không thể lấy danh sách vai trò."; // Thông báo lỗi
        });
    };

    // Hàm lấy danh sách tài khoản
    $scope.getAccounts = function () {
      const token = sessionStorage.getItem("jwtToken");
      console.log("Token trước khi gửi yêu cầu:", token);

      $http({
        method: "GET",
        url: "http://localhost:8080/admin/accounts",
        headers: {
          Authorization: "Bearer " + token,
        },
      })
        .then(function (response) {
          $scope.accounts = response.data.result;
        })
        .catch(function (error) {
          console.error("Lỗi khi lấy danh sách tài khoản:", error);
          $scope.errorMessage = "Không thể lấy danh sách tài khoản."; // Thông báo lỗi
        });
    };

    // Hàm tạo tài khoản mới
    $scope.createAccount = function () {
      const token = sessionStorage.getItem("jwtToken");
      console.log("Token trước khi gửi yêu cầu tạo tài khoản:", token);

      $http({
        method: "POST",
        url: "http://localhost:8080/admin/create",
        headers: {
          Authorization: "Bearer " + token,
          "Content-Type": "application/json",
        },
        data: $scope.newAccount,
      })
        .then(function (response) {
          $scope.successMessage = "Tạo tài khoản thành công!"; // Thông báo thành công
          $scope.getAccounts(); // Cập nhật lại danh sách tài khoản
          $scope.newAccount = {}; // Reset form
          $scope.errorMessage = ""; // Reset thông báo lỗi
        })
        .catch(function (error) {
          console.error("Lỗi khi tạo tài khoản:", error);
          $scope.errorMessage = "Có lỗi xảy ra khi tạo tài khoản!"; // Thông báo lỗi
        });
    };

    // Hàm đăng xuất
    $scope.logout = function () {
      sessionStorage.removeItem("jwtToken"); // Xóa token khỏi sessionStorage
      // Chuyển hướng đến trang đăng nhập
      window.location.href = "/assets/page/account/login.html";
    };

    // Gọi các hàm khởi tạo
    $scope.getRoles();
    $scope.getAccounts();
  },
]);
