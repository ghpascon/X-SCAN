enum RfidReaderType {
  uhfUart('uhf_uart'),
  unknown('unknown');

  const RfidReaderType(this.value);

  final String value;

  static RfidReaderType fromValue(String? value) {
    if (value == null) {
      return RfidReaderType.unknown;
    }

    for (final type in RfidReaderType.values) {
      if (type.value == value) {
        return type;
      }
    }

    return RfidReaderType.unknown;
  }
}
