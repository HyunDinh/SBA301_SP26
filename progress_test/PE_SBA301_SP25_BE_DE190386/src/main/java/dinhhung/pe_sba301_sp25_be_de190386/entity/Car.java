package dinhhung.pe_sba301_sp25_be_de190386.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Cars")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CarID")
    private Integer carId;

    @Column(name = "CarName", nullable = false)
    private String carName;

    @Column(name = "UnitsInStock")
    private Integer unitsInStock;

    @Column(name = "UnitPrice")
    private Double unitPrice;

    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    // Thiết lập mối quan hệ Many-to-One với Country
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CountryID", nullable = false)
    @ToString.Exclude
    private Country country;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Car car = (Car) o;
        return getCarId() != null && Objects.equals(getCarId(), car.getCarId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Car{" +
                "carId=" + carId +
                ", carName='" + carName + '\'' +
                ", unitsInStock=" + unitsInStock +
                ", unitPrice=" + unitPrice +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", country=" + country +
                '}';
    }
}