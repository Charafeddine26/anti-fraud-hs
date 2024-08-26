package antifraud.repository;

import antifraud.domain.dto.IpAdressDTO;
import antifraud.domain.model.IpAdress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IpAdressRepository extends JpaRepository<IpAdress, Long> {
    boolean existsByIp(String ip);

    IpAdress findByIp(String ip);

    List<IpAdress> findAllByOrderByIdAsc();
}
