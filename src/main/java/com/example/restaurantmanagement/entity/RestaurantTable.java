ackage com.example.restaurantmanagement.entity;
import com.example.restaurantmanagement.entity.enums.TableStatus;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "restaurant_tables")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RestaurantTable extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "table_number", nullable = false, unique = true)
    private Integer tableNumber;
    @Column(nullable = false)
    private Integer capacity;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TableStatus status = TableStatus.AVAILABLE;
}
