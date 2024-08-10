package com.igloo_club.nungil_v3.domain;

import com.igloo_club.nungil_v3.domain.enums.CompanyScale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;

    private String email;

    @Column(columnDefinition = "VARCHAR(20)")
    @Enumerated(value = EnumType.STRING)
    private CompanyScale scale;

    public Company updateOrCreateCompanyWithScale(CompanyScale scale) {

        if (this.scale == null) {
            this.scale = scale;
            return this;
        }

        if (!this.scale.equals(scale)) {
            return Company.builder()
                    .companyName(this.companyName)
                    .email(this.email)
                    .scale(scale)
                    .build();
        }

        return this;
    }
}
