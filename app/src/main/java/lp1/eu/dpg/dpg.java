package lp1.eu.dpg;// Requires Java 8 or newer

import android.os.Build;
import android.util.Log;

import java.nio.CharBuffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class dpg {

    private boolean debug = false;
    private String result = "";
    private String encryption = "";

    private final int iterations = 32768;
    private final int key_length_in_bytes = 64;

    private final char[] hex_array = "0123456789ABCDEF".toCharArray();

    // This is a FIFO data structure similar to std::queue in C++
    private Queue<Integer> number_queue = new LinkedList<Integer>();

    /**
     * Method to make passwords easy for humans to read
     **/

    private String readable(String password) {
        int x = 0;
        String str = "";
        while (x < password.length()) {
            str += password.substring(x, x + 3) + " ";
            x += 3;
        }
        return str;
    }

    /**
     * Convert byte[] to hex string
     * https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
     **/
    private String byte_array_to_hex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hex_array[v >>> 4];
            hexChars[j * 2 + 1] = hex_array[v & 0x0F];
        }

        return new String(hexChars);
    }

    /**
     * Convert char[] to byte[] and return byte[]
     */
    private byte[] chars_to_bytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
                byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(charBuffer.array(), '\u0000');
        Arrays.fill(byteBuffer.array(), (byte) 0);
        return bytes;
    }

    /**
     * A method to get 64 random bytes deterministically based on user input
     * And to also fill the queue with numbers
     **/
    private void fill_q(char[] sentence, char[] word, String encryption) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 512 bit key_length (64 bytes). Python and C++ use bytes rather than bits.
        KeySpec ks = new PBEKeySpec(sentence, chars_to_bytes(word), iterations, key_length_in_bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(encryption);

        if (debug) {
            String hex_random_bytes = byte_array_to_hex(skf.generateSecret(ks).getEncoded());
            System.out.println("PBKDF2:  " + hex_random_bytes);
        }

        byte[] random_bytes = skf.generateSecret(ks).getEncoded();

        for (byte b : random_bytes) {
            if (debug) {
                // & 0xFF ensures the int is unsigned
                System.out.println(((int) b) & 0xFF);
            }

            number_queue.add(((int) b) & 0xFF);
        }
    }

    public String getPassword(String StringSentence, String StringWord) {
        result = "";
        number_queue.clear();
        char[] sentence = StringSentence.toCharArray();
        char[] word = StringWord.toCharArray();

        try {
            fill_q(sentence, word, encryption);

            if (debug) {
                System.out.println("number_queue size: " + number_queue.size());
            }

            while (result.length() < (24 - 3)) {
                result += jars.lower().get(number_queue.remove());
            }
            result += jars.special().get(number_queue.remove());
            result += jars.upper().get(number_queue.remove());
            result += jars.numbers().get(number_queue.remove());
        } catch (NoSuchAlgorithmException exc) {
            Log.e("ALGORITHM_EXCEPTION", exc.toString());
        } catch (InvalidKeySpecException exc) {
            Log.e("INVALID_KEY", exc.toString());
        }

        return result;
    }

    public dpg() {
        encryption = Build.VERSION.SDK_INT >= 26 ? "PBKDF2WithHmacSHA512" : "PBKDF2WithHmacSHA1";
    }
}

