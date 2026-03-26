package dinhhung.mockmaster_server.repository;

import dinhhung.mockmaster_server.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Integer> {
    java.util.Optional<SubscriptionPlan> findByPlanName(String planName);
}