class RfidFailure implements Exception {
  const RfidFailure(this.message);

  final String message;

  @override
  String toString() => message;
}
