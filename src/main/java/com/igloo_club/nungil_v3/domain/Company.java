package com.igloo_club.nungil_v3.domain;

import com.igloo_club.nungil_v3.domain.enums.CompanyScale;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Company implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String companyName;

    @NonNull
    private String email;

    @Column(columnDefinition = "VARCHAR(20)")
    @Enumerated(value = EnumType.STRING)
    private CompanyScale scale;

    public void updateScale(CompanyScale scale) {
        this.scale = scale;
    }

    public Company cloneWithScale(CompanyScale scale) {
        try {
            Company company = this.clone();
            company.updateScale(scale);
            return company;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Company clone() throws CloneNotSupportedException {
        try {
            return (Company) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
