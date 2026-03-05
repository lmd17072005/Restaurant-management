ackage com.example.restaurantmanagement.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
@Entity
@Table(name = "menu_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MenuItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;
    @Column(name = "image_url")
    private String imageUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(name = "is_available", nullable = false)
    @Builder.Default
    private Boolean isAvailable = true;
}
