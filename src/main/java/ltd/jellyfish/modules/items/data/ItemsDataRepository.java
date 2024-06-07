package ltd.jellyfish.modules.items.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ltd.jellyfish.modules.items.models.Items;

@Repository
public interface ItemsDataRepository extends JpaRepository<Items, String> {

}
