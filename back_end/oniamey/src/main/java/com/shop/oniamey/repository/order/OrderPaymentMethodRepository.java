package com.shop.oniamey.repository.order;

import com.shop.oniamey.entity.OrderPaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPaymentMethodRepository extends JpaRepository<OrderPaymentMethod, Long> {
}
