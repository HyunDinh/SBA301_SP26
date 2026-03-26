package dinhhung.mockmaster_server.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class AdminDashboardStats {
    private long totalUsers;
    private long totalWorkspaces;
    private long totalEndpoints;
    private double totalRevenue; // Tổng tiền từ các giao dịch thành công
    private List<UserSummaryDTO> recentUsers;
}

