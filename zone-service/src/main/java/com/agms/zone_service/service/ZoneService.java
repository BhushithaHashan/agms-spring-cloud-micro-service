@Service
@RequiredArgsConstructor
public class ZoneService {
    private final ZoneRepository repository;

    public List<Zone> getAllZones() {
        return repository.findAll();
    }

    public Zone createZone(Zone zone) {
        
        if (zone.getName() == null || zone.getName().isEmpty()) {
            throw new RuntimeException("Zone name cannot be empty");
        }
        return repository.save(zone);
    }
}