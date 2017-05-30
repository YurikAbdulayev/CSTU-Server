package com.cstu.web;

import com.cstu.domain.CstuUser;
import com.cstu.domain.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private UserRepository repository;

    @Inject
    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }


    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public ResponseEntity<String> sayHello() {
        System.out.println("hello");
        return ResponseEntity.ok().body("Hello");
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addUser() {
        System.out.println("post");
        CstuUser cstuUser = new CstuUser();
        cstuUser.setName("Yurik");
        CstuUser user = repository.save(cstuUser);
        return ResponseEntity.ok().body(user);
    }

}
