package com.shop.product_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "attributes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attributeId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 50)
    private String unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL)
    private List<ProductAttribute> productAttributes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private Long createdBy;
    private Long updatedBy;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}
