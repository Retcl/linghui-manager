package ltd.jellyfish.modules.items.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ltd.jellyfish.modules.items.models.Prices;

@Repository
public interface PricesDataRepository extends JpaRepository<Prices, String> {

}
