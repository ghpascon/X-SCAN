class RfidPlatformInfo {
  const RfidPlatformInfo({
    required this.manufacturer,
    required this.model,
    required this.sdkAvailable,
    required this.readerName,
  });

  final String manufacturer;
  final String model;
  final bool sdkAvailable;
  final String readerName;

  bool get isLikelyChainwayC72 {
    final lowerManufacturer = manufacturer.toLowerCase();
    final lowerModel = model.toLowerCase();
    return lowerManufacturer.contains('chainway') || lowerModel.contains('c72');
  }
}
