class RfidPlatformInfo {
  const RfidPlatformInfo({
    required this.manufacturer,
    required this.brand,
    required this.model,
    required this.device,
    required this.product,
    required this.hardware,
    required this.board,
    required this.androidSdkInt,
    required this.sdkAvailable,
    required this.readerName,
    required this.readerType,
    required this.profileId,
  });

  final String manufacturer;
  final String brand;
  final String model;
  final String device;
  final String product;
  final String hardware;
  final String board;
  final int androidSdkInt;
  final bool sdkAvailable;
  final String readerName;
  final String readerType;
  final String profileId;

  bool get isLikelyChainwayC72 {
    final lowerManufacturer = manufacturer.toLowerCase();
    final lowerModel = model.toLowerCase();
    return lowerManufacturer.contains('chainway') || lowerModel.contains('c72');
  }
}
