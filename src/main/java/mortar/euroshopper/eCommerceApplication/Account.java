package mortar.euroshopper.eCommerceApplication;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Data
@NoArgsConstructor
@Table(name = "accounts")
public class Account implements Serializable, Comparable<Account> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String address;

    private String postalCode;

    private String city;

    private String country;

    private String phoneNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    private List<Order> orders;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "roles")
    @Column(name = "role")
    private List<String> roles;

    private LocalDateTime created;

    private Boolean privacyPolicyIsRead;

    public Account(String em, String pw, String fn, String ln, String ad, String pc, String ci, String co, String pn, List<String> ro, LocalDateTime cr, Boolean pp) {

        this.email = em;
        this.password = pw;
        this.firstName = fn;
        this.lastName = ln;
        this.address = ad;
        this.postalCode = pc;
        this.city = ci;
        this.country = co;
        this.phoneNumber = pn;
        this.orders = new ArrayList<>();
        this.roles = ro;
        this.created = cr;
        this.privacyPolicyIsRead = pp;
    }

    @Transient
    public String getClientNumber() {

        DecimalFormat df = new DecimalFormat("CLI00000");

        return df.format(id);
    }

    @Override
    public int compareTo(Account other) {

        return lastName.compareToIgnoreCase(other.lastName);
    }

}
