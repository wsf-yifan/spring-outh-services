/*
 * Author     : shaofan
 * Date       : 4/7/2020 4:27 PM
 * Description:
 *
 */

package com.yifan.authclient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class TestController {
    @GetMapping("/getuser")
    public  String getUser(Principal principal){
        return "get user:"+principal.getName();
    }
}
