/*
 * Author     : shaofan
 * Date       : 4/6/2020 11:28 AM
 * Description:
 *
 */

package com.yifan.oauth2server.data;

import org.springframework.data.repository.CrudRepository;
import com.yifan.oauth2server.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
