package com.info5059.casestudy.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.*;

@CrossOrigin
@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Modifying
    @Transactional
    @Query("delete from Product where id = ?1")
    int deleteOne(String productid);
    List<Product> findByVendorid(Long vendorid);

}