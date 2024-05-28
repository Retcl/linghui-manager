package ltd.jellyfish.modules.authentication.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ltd.jellyfish.models.Users;

@Repository
public interface UsersDataRepository extends JpaRepository<Users, String> {

}
