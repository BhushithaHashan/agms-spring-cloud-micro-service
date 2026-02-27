@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double targetTemperature;
}