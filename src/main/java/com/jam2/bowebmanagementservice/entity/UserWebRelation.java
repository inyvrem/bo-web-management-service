package com.jam2.bowebmanagementservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="user_web_relation")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWebRelation implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID userWebId;

    @OneToOne
    @JoinColumn(name="auth_id")
    private AuthAccount authAccount;

    private UUID webHeroId;
    private UUID webAboutId;
    private UUID webPortfolioId;


}
