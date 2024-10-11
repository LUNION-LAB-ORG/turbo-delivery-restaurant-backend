package com.lunionlab.turbo_restaurant.utilities;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.temporal.TemporalUnit;
import java.util.Date;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class Utility {

    public static Date dateFromInteger(Integer number, TemporalUnit unit) {
        Date now = new Date();
        Date date = Date
                .from(now.toInstant().plus(number, unit));
        return date;
    }

    public static String formatDate(Date date, String format) {
        // pour l'heure hh:mm:ss
        // format = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static String changeDateFormat(String date, String outputFormat) {
        try {

            // Crée un SimpleDateFormat pour le format d'entrée
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");

            // Parse la chaîne en une date
            Date datePars = inputFormat.parse(date);
            // Crée un SimpleDateFormat pour le format de sortie (yyyy-MM-dd)
            SimpleDateFormat ouputFormat = new SimpleDateFormat(outputFormat);

            // Formate la date dans le nouveau format
            return ouputFormat.format(datePars);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generatePassword(int length) {
        String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        String NUMBER = "0123456789";
        String SPECIAL_CHARACTERS = "@#_";
        String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER + SPECIAL_CHARACTERS;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        sb.append(CHAR_LOWER.charAt(random.nextInt(CHAR_LOWER.length())));
        sb.append(CHAR_UPPER.charAt(random.nextInt(CHAR_UPPER.length())));
        sb.append(NUMBER.charAt(random.nextInt(NUMBER.length())));
        sb.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

        for (int i = 4; i < length; i++) {
            sb.append(DATA_FOR_RANDOM_STRING.charAt(random.nextInt(DATA_FOR_RANDOM_STRING.length())));
        }

        return sb.toString();
    }

    public static String hashPassword(String password) {
        String salt = BCrypt.gensalt();
        String passwordHash = BCrypt.hashpw(password, salt);
        return passwordHash;
    }

    public static Boolean checkPassword(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
