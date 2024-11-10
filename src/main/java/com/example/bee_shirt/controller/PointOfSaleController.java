package com.example.bee_shirt.controller;

import com.example.bee_shirt.entity.Bill;
import com.example.bee_shirt.entity.BillDetail;
import com.example.bee_shirt.entity.ShirtDetail;
import com.example.bee_shirt.service.PointOfSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("point-of-sale")
public class PointOfSaleController {

    @Autowired
    private PointOfSaleService pointOfSaleService;

    @GetMapping("")
    public String pos(Model model) {
        model.addAttribute("products", pointOfSaleService.searchShirtDetails(""));
        model.addAttribute("listhdc", pointOfSaleService.getPendingBills());
        return "haha";
    }

    @GetMapping("/search")
    @ResponseBody
    public List<ShirtDetail> search(@RequestParam("query") String query) {
        return pointOfSaleService.searchShirtDetails(query);
    }

    @GetMapping("/get-pending-bill")
    @ResponseBody
    public List<Bill> getPendingBill() {
        return pointOfSaleService.getPendingBills();
    }

    @GetMapping("/get-bill-detail")
    @ResponseBody
    public List<BillDetail> getBillDetail(@RequestParam("codeBill") String codeBill) {
        return pointOfSaleService.getBillDetails(codeBill);
    }

    @GetMapping("/get-shirt-detail")
    @ResponseBody
    public ShirtDetail getShirtDetail(@RequestParam("codeShirtDetail") String codeShirtDetail) {
        return pointOfSaleService.getShirtDetail(codeShirtDetail);
    }

    @GetMapping("create-blank-bill")
    @ResponseBody
    public ResponseEntity<String> createNewBill() {
        return ResponseEntity.ok(pointOfSaleService.createNewBill());
    }

    @PostMapping("add-to-cart")
    @ResponseBody
    public ResponseEntity<String> addItemToCart(@RequestParam String codeShirtDetail, @RequestParam String codeBill, @RequestParam Integer quantity) {
        return ResponseEntity.ok(pointOfSaleService.addItemToCart(codeShirtDetail, codeBill, quantity));
    }

    @PostMapping("change-quantity")
    @ResponseBody
    public ResponseEntity<String> changeItemValue(@RequestParam String codeBillDetail, @RequestParam Integer quantity) {
        return ResponseEntity.ok(pointOfSaleService.changeItemQuantity(codeBillDetail, quantity));
    }

    @PostMapping("remove-item-from-cart")
    @ResponseBody
    public ResponseEntity<String> removeItemFromCart(@RequestParam String codeBillDetail) {
        return ResponseEntity.ok(pointOfSaleService.removeItemFromCart(codeBillDetail));
    }

    @PostMapping("cancel")
    public ResponseEntity<String> cancel(@RequestParam String codeBill) {
        return ResponseEntity.ok(pointOfSaleService.cancelBill(codeBill));
    }

    @PostMapping("checkout")
    public ResponseEntity<String> checkOut(@RequestParam String codeBill, @RequestParam(required = false) String codeVoucher) {
        return ResponseEntity.ok(pointOfSaleService.checkout(codeBill, codeVoucher));
    }

    @GetMapping("/close")
    public ResponseEntity<String> close() {
        return ResponseEntity.ok(pointOfSaleService.closeWebcam());
    }

    @GetMapping("/scanBarcode")
    public ResponseEntity<String> scanBarcode() {
        return ResponseEntity.ok(pointOfSaleService.scanBarcode());
    }
}
