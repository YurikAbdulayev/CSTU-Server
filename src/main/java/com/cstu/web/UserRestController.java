package com.cstu.web;

import com.cstu.domain.CstuUser;
import com.cstu.domain.UserRepository;
import com.cstu.utils.UserUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private UserRepository repository;

    @Inject
    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping(value = "/create/user")
    public ResponseEntity<String> createNewUser(@RequestBody CstuUser user) {
        if (Objects.equals(user.getLogin(), repository.findByLogin(user.getLogin()).getLogin())) {
            return ResponseEntity.ok().body("Користувач з таким логіном вже існує");
        }
        CstuUser currentUser = repository.save(user);
        UserUtils.sendMail(currentUser);
        return ResponseEntity.ok().body(HttpStatus.OK.toString());
    }

    @PostMapping(value = "/logout/{id}")
    public ResponseEntity<String> logout(@PathVariable("id") Long id) {
        CstuUser currentUser = repository.findOne(id);
        currentUser.setToken(null);
        repository.save(currentUser);
        return ResponseEntity.ok().body(HttpStatus.OK.toString());
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody CstuUser user) {
        try {
            CstuUser currentUser = repository.findByLogin(user.getLogin());
            if (Objects.equals(currentUser.getPassword(), user.getPassword())) {
                if (!currentUser.getActive()) {
                    return ResponseEntity.ok().body("NOT_ACTIVE");
                }
                currentUser.setToken(UserUtils.generateToken());
                repository.save(currentUser);
                return ResponseEntity.ok().body(currentUser);
            }else {
                return ResponseEntity.badRequest().body(HttpStatus.UNAUTHORIZED.toString());
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST.toString());
        }
    }

    @GetMapping(value = "/verify/{id}")
    public ResponseEntity<String> verify(@PathVariable("id") Long id) {
        CstuUser currentUser = repository.findOne(id);
        if (!currentUser.getActive()) {
            currentUser.setActive(true);
            repository.save(currentUser);
            return ResponseEntity.ok().body("Користувача " + currentUser.getEmail() + " щойно активовано.");
        } else {
            return ResponseEntity.ok().body("Користувач " + currentUser.getEmail() + " вже активний");
        }

    }

}
