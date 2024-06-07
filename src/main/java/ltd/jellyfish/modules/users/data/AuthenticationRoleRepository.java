package ltd.jellyfish.modules.users.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ltd.jellyfish.models.AuthenticationRole;

@Repository("UserData-AothData")
public interface AuthenticationRoleRepository extends JpaRepository<AuthenticationRole, String> {

}
