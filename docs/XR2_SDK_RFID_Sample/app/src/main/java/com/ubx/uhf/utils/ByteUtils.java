package com.ubx.uhf.utils;

public class ByteUtils {

    /**
     * Convert Hex String to Byte Array
     *
     * @param hexString the hex string
     * @return the byte [ ]
     */
    public static byte[] hexStringToBytes(String hexString) {
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() >> 1];
        int index = 0;
        for (int i = 0; i < hexString.length(); i++) {
            if (index > hexString.length() - 1) {
                return byteArray;
            }
            byte highDit = (byte) (Character.digit(hexString.charAt(index), 16) & 0xFF);
            byte lowDit = (byte) (Character.digit(hexString.charAt(index + 1), 16) & 0xFF);
            byteArray[i] = (byte) (highDit << 4 | lowDit);
            index += 2;
        }
        return byteArray;
    }

    /*
     * Convert byte array to hexadecimal string
     */
    public static String bytes2HexString(byte[] array) {
        StringBuilder builder = new StringBuilder();

        for (byte b : array) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            builder.append(hex);
        }

        return builder.toString().toUpperCase();
    }
    /**
     * Get PC value
     * @param epc   EPC value in hexadecimal (length is a multiple of 4), for example：0123456789ABCD  、 08CD6600 、 EF89
     * @return Return the PC value as a 4-digit hexadecimal value, for example：0030 、 0080 、 0101
     */
    public static String getPC(String epc){
        String pc ="0000";
        int len = epc.length()/4;//EPC is divided by 4 to get the length value in decimal
        int b = len << 11;//The obtained length value is shifted to the left by 11 bits to obtain the byte value.
        String aHex = Integer.toHexString(b);//The byte value is converted into hexadecimal, which is the PC value indicating the EPC length.
        if (aHex.length() == 3){
            pc = "0"+aHex;//If the length of the byte converted to hexadecimal is 3 digits, add 0 to the front high bit, for example: "003" becomes "0003" after adding 0."
        } else {
            pc = aHex;
        }
        return pc;
    }
}
