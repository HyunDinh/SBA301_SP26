package dinhhung.mockmaster_server.repository;

import dinhhung.mockmaster_server.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    // Tìm lịch sử giao dịch của một User cụ thể (Sắp xếp mới nhất lên đầu)
    List<PaymentTransaction> findByUser_UserIdOrderByPaymentDateDesc(String userId);

    // Tìm các giao dịch theo trạng thái (Ví dụ: SUCCESS, PENDING)
    List<PaymentTransaction> findByStatus(String status);

    // Tìm theo mã tham chiếu VNPay (Dùng khi xử lý IPN/Callback từ VNPay)
    Optional<PaymentTransaction> findByVnpTxnRef(String vnpTxnRef);
}