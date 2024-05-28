package ltd.jellyfish.modules.users.services;

import ltd.jellyfish.modules.users.data.AuthenticationRoleRepository;
import ltd.jellyfish.modules.users.data.RoleToUsersRepository;
import ltd.jellyfish.modules.users.data.UsersDataRepository;
import ltd.jellyfish.modules.users.dtos.UserDTO;

import org.springframework.stereotype.Service;

@Service
public record UserDataService(
        RoleToUsersRepository roleToUsersRepository,
        UsersDataRepository usersDataRepository,
        AuthenticationRoleRepository authenticationRoleRepository
) {


    public void addUsers(UserDTO userDTO){
        
    }
}
