package com.info5059.casestudy.PurchaseOrder;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class PurchaseOrderController {
    @Autowired
    private PurchaseOrderDAO purchaseOrderDAO;

    @PostMapping("/api/purchaseorders")
    public ResponseEntity<PurchaseOrder> addOne(@RequestBody PurchaseOrder purchaseOrder) {
        return new ResponseEntity<PurchaseOrder>(purchaseOrderDAO.create(purchaseOrder), HttpStatus.OK);
    }
}
