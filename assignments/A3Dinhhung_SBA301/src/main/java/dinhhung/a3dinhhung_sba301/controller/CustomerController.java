package dinhhung.a3dinhhung_sba301.controller;

import dinhhung.a3dinhhung_sba301.entities.BookingReservation;
import dinhhung.a3dinhhung_sba301.entities.Customer;
import dinhhung.a3dinhhung_sba301.repositories.BookingRepository;
import dinhhung.a3dinhhung_sba301.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {

    @Autowired private CustomerRepository customerRepo;
    @Autowired private BookingRepository bookingRepo;

    // Cập nhật thông tin cá nhân
    @PutMapping("/profile/{id}")
    public Customer updateProfile(@PathVariable Long id, @RequestBody Customer updatedData) {
        Customer existing = customerRepo.findById(id).orElseThrow();
        existing.setCustomerFullName(updatedData.getCustomerFullName());
        existing.setTelephone(updatedData.getTelephone());
        existing.setCustomerBirthday(updatedData.getCustomerBirthday());
        return customerRepo.save(existing);
    }

    // Xem lịch sử đặt phòng của chính mình
    @GetMapping("/history/{customerId}")
    public List<BookingReservation> getMyHistory(@PathVariable Long customerId) {
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        return bookingRepo.findByCustomerOrderByBookingDateDesc(customer);
    }
}