/*
 * Author     : shaofan
 * Date       : 4/6/2020 5:05 PM
 * Description:
 *
 */

package com.yifan.oauth2server.data;

import com.yifan.oauth2server.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository
    extends CrudRepository<Order, Long> {
}
