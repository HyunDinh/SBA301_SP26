package dinhhung.pe_sba301_sp25_be_de190386.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Country")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    @Id
    @Column(name = "CountryID")
    private String countryId;

    @Column(name = "CountryName", nullable = false)
    private String countryName;

    // Một quốc gia có danh sách nhiều xe
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private List<Car> cars;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Country country = (Country) o;
        return getCountryId() != null && Objects.equals(getCountryId(), country.getCountryId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}