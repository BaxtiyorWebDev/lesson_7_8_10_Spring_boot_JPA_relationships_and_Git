package uz.pdp.online.appjparelationships.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.appjparelationships.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {// ADDRESS DEGAN ENTITY BILAN ISHLASH UCHUN XIZMAT QILADI
}
