package dinhhung.pe_sba301_sp25_be_de190386.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "AccountMember")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountMember {
    @Id
    @Column(name = "MemberID")
    private String memberId;

    @Column(name = "MemberPassword", nullable = false)
    private String memberPassword;

    @Column(name = "EmailAddress", unique = true)
    private String emailAddress;

    @Column(name = "MemberRole")
    private Integer memberRole;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        AccountMember that = (AccountMember) o;
        return getMemberId() != null && Objects.equals(getMemberId(), that.getMemberId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}