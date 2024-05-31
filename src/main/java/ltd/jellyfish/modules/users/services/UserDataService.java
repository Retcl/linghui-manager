package ltd.jellyfish.modules.users.services;

import ltd.jellyfish.models.AuthenticationRole;
import ltd.jellyfish.models.Result;
import ltd.jellyfish.models.RoleToUser;
import ltd.jellyfish.models.Users;
import ltd.jellyfish.modules.users.data.AuthenticationRoleRepository;
import ltd.jellyfish.modules.users.data.RoleToUsersRepository;
import ltd.jellyfish.modules.users.data.UsersDataRepository;
import ltd.jellyfish.modules.users.dtos.UserDTO;

import java.sql.Date;

import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public record UserDataService(
        RoleToUsersRepository roleToUsersRepository,
        UsersDataRepository usersDataRepository,
        AuthenticationRoleRepository authenticationRoleRepository,
        PasswordEncoder passwordEncoder
) {

    /**
     * 注册用户信息
     * @param userDTO 用户注册数据
     * @return 统一返回数据格式
     */
    public Result<?> addUsers(UserDTO userDTO){
        Result<?> reply = null;
        Users users = new Users();
        users.setName(userDTO.getUsername());
        Example<Users> userExample = Example.of(users);
        users = usersDataRepository.findOne(userExample).get();
        if (users.getPassword() != null || !users.getPassword().isEmpty()){
            reply = new Result<>();
            reply.setSuccess(false);
            reply.setMessage("用户已存在");
        }else{
            users.setName(userDTO.getUsername());
            users.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            users.setCreateTime(new Date(System.currentTimeMillis()));
            usersDataRepository.save(users);
            userExample = Example.of(users);
            users = usersDataRepository.findOne(userExample).get();
            String uid = users.getId();
            AuthenticationRole authenticationRole = new AuthenticationRole();
            RoleToUser roleToUser = new RoleToUser();
            
            reply = new Result<>();
            reply.setSuccess(true);
            reply.setMessage("用户注册成功");
        }
        return reply;
    }
}
