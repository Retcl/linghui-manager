package ltd.jellyfish.modules.authentication.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ltd.jellyfish.models.AuthenticationRole;

@Repository
public interface AuthenticationRoleRepository extends JpaRepository<AuthenticationRole, String> {

}
