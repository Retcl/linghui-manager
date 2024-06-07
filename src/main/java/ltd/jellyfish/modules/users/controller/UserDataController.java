package ltd.jellyfish.modules.users.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ltd.jellyfish.models.Result;
import ltd.jellyfish.modules.authentication.token.annotation.UnuseToken;
import ltd.jellyfish.modules.users.dtos.UserDTO;
import ltd.jellyfish.modules.users.services.UserDataService;

@RestController
public record UserDataController(
    UserDataService userDataService
) {

    @UnuseToken
    @PostMapping("/register")
    public Result<?> register(@RequestBody UserDTO userDTO){
        return userDataService.addUsers(userDTO);
    }
}
