package kh.edu.istad.platform.order.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "businesses")
@Setter
@Getter
@NoArgsConstructor
public class BusinessEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "active", nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "business", fetch = FetchType.EAGER)
    private List<BusinessProductEntity> products = new ArrayList<>();


}
