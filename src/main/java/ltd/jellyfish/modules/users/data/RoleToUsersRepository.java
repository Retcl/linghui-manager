package ltd.jellyfish.modules.users.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ltd.jellyfish.models.RoleToUser;

@Repository
public interface RoleToUsersRepository extends JpaRepository<RoleToUser, String> {
    
}
