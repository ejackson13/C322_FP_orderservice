package edu.iu.c322.finalProject.orderservice.repository;

import edu.iu.c322.finalProject.orderservice.model.entity.Order;
import edu.iu.c322.finalProject.orderservice.model.entity.SellerItem;
import edu.iu.c322.finalProject.orderservice.model.entity.SellerShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface SellerItemRepository extends JpaRepository<SellerItem, Integer> {

    SellerItem findBySellerItemId(Integer seller_item_id);

    @Modifying
    @Query("UPDATE SellerItem s SET s.inventory = :inventory WHERE s.sellerItemId = :sellerItemId")
    void updateInventory(@Param("inventory") int inventory, @Param("sellerItemId") int sellerItemId);

}
