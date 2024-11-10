package com.example.bee_shirt.controller;

import com.example.bee_shirt.dto.response.ApiResponse;
import com.example.bee_shirt.entity.Bill;
import com.example.bee_shirt.entity.BillDetail;
import com.example.bee_shirt.entity.ShirtDetail;
import com.example.bee_shirt.service.PointOfSaleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("point-of-sale")
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PointOfSaleController {

    @Autowired
    private PointOfSaleService pointOfSaleService;

    @GetMapping("")
    public ApiResponse<List<ShirtDetail>> pos() {
        log.info("Fetching product list...");
        List<ShirtDetail> products = pointOfSaleService.searchShirtDetails("");
        log.info("Found {} products", products.size());

        return ApiResponse.<List<ShirtDetail>>builder()
                .code(1000)
                .message("Products fetched successfully")
                .result(products)
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<ShirtDetail>> search(@RequestParam("query") String query) {
        log.info("Searching for products with query: {}", query);
        List<ShirtDetail> products = pointOfSaleService.searchShirtDetails(query);
        log.info("Found {} products", products.size());

        return ApiResponse.<List<ShirtDetail>>builder()
                .code(1000)
                .message("Search results fetched successfully")
                .result(products)
                .build();
    }

    @GetMapping("/get-pending-bill")
    public ApiResponse<List<Bill>> getPendingBill() {
        log.info("Fetching pending bills...");
        List<Bill> bills = pointOfSaleService.getPendingBills();
        log.info("Found {} pending bills", bills.size());

        return ApiResponse.<List<Bill>>builder()
                .code(1000)
                .message("Pending bills fetched successfully")
                .result(bills)
                .build();
    }

    @GetMapping("/get-bill-detail")
    public ApiResponse<List<BillDetail>> getBillDetail(@RequestParam("codeBill") String codeBill) {
        log.info("Fetching bill details for bill code: {}", codeBill);
        List<BillDetail> billDetails = pointOfSaleService.getBillDetails(codeBill);
        log.info("Found {} bill details", billDetails.size());

        return ApiResponse.<List<BillDetail>>builder()
                .code(1000)
                .message("Bill details fetched successfully")
                .result(billDetails)
                .build();
    }

    @GetMapping("/get-shirt-detail")
    public ApiResponse<ShirtDetail> getShirtDetail(@RequestParam("codeShirtDetail") String codeShirtDetail) {
        log.info("Fetching shirt detail for code: {}", codeShirtDetail);
        ShirtDetail shirtDetail = pointOfSaleService.getShirtDetail(codeShirtDetail);

        return ApiResponse.<ShirtDetail>builder()
                .code(1000)
                .message("Shirt detail fetched successfully")
                .result(shirtDetail)
                .build();
    }

    @GetMapping("create-blank-bill")
    public ApiResponse<String> createNewBill() {
        log.info("Creating new bill...");
        String result = pointOfSaleService.createNewBill();
        return ApiResponse.<String>builder()
                .code(1000)
                .message(result)
                .result("New bill created successfully")
                .build();
    }

    @PostMapping("add-to-cart")
    public ApiResponse<String> addItemToCart(@RequestParam String codeShirtDetail, @RequestParam String codeBill, @RequestParam Integer quantity) {
        log.info("Adding item to cart: Shirt code {}, Bill code {}, Quantity {}", codeShirtDetail, codeBill, quantity);
        String result = pointOfSaleService.addItemToCart(codeShirtDetail, codeBill, quantity);
        return ApiResponse.<String>builder()
                .code(1000)
                .message(result)
                .result("Item added to cart successfully")
                .build();
    }

    @PostMapping("change-quantity")
    public ApiResponse<String> changeItemQuantity(@RequestParam String codeBillDetail, @RequestParam Integer quantity) {
        log.info("Changing quantity for bill detail code: {}, New quantity: {}", codeBillDetail, quantity);
        String result = pointOfSaleService.changeItemQuantity(codeBillDetail, quantity);
        return ApiResponse.<String>builder()
                .code(1000)
                .message(result)
                .result("Quantity updated successfully")
                .build();
    }

    @PostMapping("remove-item-from-cart")
    public ApiResponse<String> removeItemFromCart(@RequestParam String codeBillDetail) {
        log.info("Removing item from cart with bill detail code: {}", codeBillDetail);
        String result = pointOfSaleService.removeItemFromCart(codeBillDetail);
        return ApiResponse.<String>builder()
                .code(1000)
                .message(result)
                .result("Item removed from cart successfully")
                .build();
    }

    @PostMapping("cancel")
    public ApiResponse<String> cancel(@RequestParam String codeBill) {
        log.info("Cancelling bill with code: {}", codeBill);
        String result = pointOfSaleService.cancelBill(codeBill);
        return ApiResponse.<String>builder()
                .code(1000)
                .message(result)
                .result("Bill cancelled successfully")
                .build();
    }

    @PostMapping("checkout")
    public ApiResponse<String> checkout(@RequestParam String codeBill, @RequestParam(required = false) String codeVoucher) {
        log.info("Processing checkout for bill: {}", codeBill);
        String result = pointOfSaleService.checkout(codeBill, codeVoucher);
        return ApiResponse.<String>builder()
                .code(1000)
                .message(result)
                .result("Checkout completed successfully")
                .build();
    }

    @GetMapping("/close")
    public ApiResponse<String> close() {
        log.info("Closing webcam...");
        String result = pointOfSaleService.closeWebcam();
        return ApiResponse.<String>builder()
                .code(1000)
                .message(result)
                .result("Webcam closed successfully")
                .build();
    }

    @GetMapping("/scanBarcode")
    public ApiResponse<String> scanBarcode() {
        log.info("Scanning barcode...");
        String result = pointOfSaleService.scanBarcode();
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Barcode scan completed")
                .result(result)
                .build();
    }
}
