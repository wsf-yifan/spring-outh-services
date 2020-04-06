/*
 * Author     : shaofan
 * Date       : 4/6/2020 5:03 PM
 * Description:
 *
 */

package com.yifan.oauth2server.data;

import com.yifan.oauth2server.model.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository
    extends CrudRepository<Ingredient, String> {
}
