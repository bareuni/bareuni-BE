package com.umc.BareuniBE.entities;

import com.umc.BareuniBE.global.BaseEntity;
import com.umc.BareuniBE.global.enums.ReviewType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reviewIdx")
    private Long reviewIdx;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital")
    private Hospital hospital;

    // 별점
    @Column(name = "totalScore", nullable = false)
    private int totalScore;

    // 진료 결과 점수
    @Enumerated(value = EnumType.STRING)
    @Column(name = "treatmentScore", nullable = false)
    private ReviewType treatmentScore;

    // 서비스 품질 점수
    @Enumerated(value = EnumType.STRING)
    @Column(name = "serviceScore", nullable = false)
    private ReviewType serviceScore;

    // 시설 및 장비 점수
    @Enumerated(value = EnumType.STRING)
    @Column(name = "equipmentScore", nullable = false)
    private ReviewType equipmentScore;

    @Column(name = "content", nullable = false)
    @Size(min=20)
    private String content;

    @Column(name = "payment", nullable = false)
    private Long payment;

    @Column(name = "receipt", nullable = false)
    private boolean receipt;

    @Column(name = "images", nullable = false, length = 1000)
    private String images;

    public Review(User user, Hospital hospital) {
        this.user = user;
        this.hospital = hospital;
    }
}
