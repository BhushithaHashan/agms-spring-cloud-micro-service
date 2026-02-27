@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
public class ZoneController {
    private final ZoneService zoneService; // Talk to Service

    @GetMapping
    public ResponseEntity<List<Zone>> getAll() {
        return ResponseEntity.ok(zoneService.getAllZones());
    }

    @PostMapping
    public ResponseEntity<Zone> create(@RequestBody Zone zone) {
        return ResponseEntity.status(HttpStatus.CREATED).body(zoneService.createZone(zone));
    }
}