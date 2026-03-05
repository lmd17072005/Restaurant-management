ackage com.example.restaurantmanagement.entity;
import com.example.restaurantmanagement.entity.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "reservations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "customer_name", nullable = false)
    private String customerName;
    @Column(name = "customer_phone", nullable = false, length = 20)
    private String customerPhone;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private RestaurantTable table;
    @Column(name = "reservation_time", nullable = false)
    private LocalDateTime reservationTime;
    @Column(name = "number_of_guests", nullable = false)
    private Integer numberOfGuests;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ReservationStatus status = ReservationStatus.PENDING;
    @Column(columnDefinition = "TEXT")
    private String note;
}
