package softuni.bg.bikeshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softuni.bg.bikeshop.models.Address;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address,Long> {
    Optional<Address> findByStreetAndCityAndPostalCode(String street, String city, String postalCode);
}
