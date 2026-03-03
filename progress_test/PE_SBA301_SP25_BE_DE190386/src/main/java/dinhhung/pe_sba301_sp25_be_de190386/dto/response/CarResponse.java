package dinhhung.pe_sba301_sp25_be_de190386.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CarResponse {
    private Integer carId;
    private String carName;
    private Integer unitsInStock;
    private Double unitPrice;
    private String countryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}