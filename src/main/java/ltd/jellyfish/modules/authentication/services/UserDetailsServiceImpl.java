package ltd.jellyfish.modules.authentication.services;

import org.springframework.data.domain.Example;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ltd.jellyfish.models.AuthenticationRole;
import ltd.jellyfish.models.RoleToUser;
import ltd.jellyfish.models.Users;
import ltd.jellyfish.modules.authentication.data.AuthenticationRoleRepository;
import ltd.jellyfish.modules.authentication.data.RoleToUsersRepository;
import ltd.jellyfish.modules.authentication.data.UsersDataRepository;

import java.util.*;

@Service
public record UserDetailsServiceImpl(
    UsersDataRepository usersDataRepository,
    AuthenticationRoleRepository roleRepository,
    RoleToUsersRepository roleToUsersRepository
) implements UserDetailsService {

    /**
     * @param username 用户名
     * @return
     * @throws UsernameNotFoundException 登陆用户的流程，查询用户是否存在，如果用户不存在则抛出异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = new Users();
        users.setName(username);
        Example<Users> userExample = Example.of(users);
        users = usersDataRepository.findOne(userExample).get();
        if (users == null) {
            throw new UsernameNotFoundException("username not found");
        }
        List<SimpleGrantedAuthority> authorities = getAuthentications(users);
        return new User(username, users.getPassword(), authorities);
    }

    private List<SimpleGrantedAuthority> getAuthentications(Users users){
        RoleToUser roleToUser = new RoleToUser();
        roleToUser.setUserId(users.getId());
        Example<RoleToUser> roExample = Example.of(roleToUser);
        List<RoleToUser> roleToUsers = roleToUsersRepository.findAll(roExample);
        AuthenticationRole authenticationRole = null;
        List<SimpleGrantedAuthority> reply = new ArrayList<>();
        for (RoleToUser roleSig : roleToUsers) {
            authenticationRole = new AuthenticationRole();
            authenticationRole.setId(roleSig.getRoleId());
            Example<AuthenticationRole> authenticationRoleExample = Example.of(authenticationRole);
            authenticationRole = roleRepository.findOne(authenticationRoleExample).get();
            reply.add(new SimpleGrantedAuthority(authenticationRole.getRoleName()));
        }
        return reply;
    }

}
