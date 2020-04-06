/*
 * Author     : shaofan
 * Date       : 4/6/2020 5:07 PM
 * Description:
 *
 */

package com.yifan.oauth2server.data;

import com.yifan.oauth2server.model.Taco;
import org.springframework.data.repository.CrudRepository;

public interface TacoRepository
    extends CrudRepository<Taco, Long> {
}
