package com.info5059.casestudy.PurchaseOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.info5059.casestudy.product.Product;
import com.info5059.casestudy.product.ProductRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;

@Component
public class PurchaseOrderDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ProductRepository prodRepo;

    @Transactional
    public PurchaseOrder create(PurchaseOrder pOrder) {
        PurchaseOrder newPO = new PurchaseOrder();
        newPO.setPodate(LocalDateTime.now());
        newPO.setVendorid(pOrder.getVendorid());
        newPO.setAmount(pOrder.getAmount());
        entityManager.persist(newPO);

        for (PurchaseOrderLineItem item : pOrder.getItems()) {
            PurchaseOrderLineItem newLineItem = new PurchaseOrderLineItem();
            newLineItem.setPoid(newPO.getId());
            newLineItem.setProductid(item.getProductid());
            newLineItem.setPrice(item.getPrice());
            newLineItem.setQty(item.getQty());
            entityManager.persist(newLineItem);

            // we also need to update the QOO on the product table
            Product prod = prodRepo.getReferenceById(item.getProductid());
            prod.setQoo(prod.getQoo() + item.getQty());
            prodRepo.saveAndFlush(prod);
        }
        entityManager.flush();
        entityManager.refresh(newPO);
        return newPO;
    }

}
