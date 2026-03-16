class RfidTag {
  const RfidTag({
    required this.epc,
    required this.timestamp,
    this.tid,
    this.rssi,
    this.count = 1,
  });

  final String epc;
  final String? tid;
  final int? rssi;
  final DateTime timestamp;
  final int count;

  RfidTag copyWith({
    String? epc,
    String? tid,
    int? rssi,
    DateTime? timestamp,
    int? count,
  }) {
    return RfidTag(
      epc: epc ?? this.epc,
      tid: tid ?? this.tid,
      rssi: rssi ?? this.rssi,
      timestamp: timestamp ?? this.timestamp,
      count: count ?? this.count,
    );
  }
}
