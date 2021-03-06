package com.cstu.web.api;

import com.cstu.domain.CstuUser;
import com.cstu.domain.Message;
import com.cstu.domain.MessageRepository;
import com.cstu.domain.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MessageRestController {

    @Inject
    private MessageRepository repository;

    @Inject
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<String> testMessage() {
        return ResponseEntity.ok().body("Hello World");
    }

    @PostMapping(value = "/message")
    public ResponseEntity<String> createNewMessage(@RequestBody Message message) {
        repository.save(message);
        return ResponseEntity.ok().body(HttpStatus.OK.toString());
    }

    @GetMapping(value = "message/{identifier}/{login}")
    public ResponseEntity<List<Message>> verify(@PathVariable("identifier") String identifier,
                                                @PathVariable("login") String login) {
        try {
            CstuUser currentUser = userRepository.findByLogin(login);
            if (currentUser != null) {
                List<Message> messageList = repository.findByIdentifier(identifier);
                return ResponseEntity.ok().body(messageList);
            } else {
                return ResponseEntity.badRequest().body(new ArrayList<>());
            }
        }catch (NullPointerException e) {
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }
    }
}
