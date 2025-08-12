package com.lunionlab.turbo_restaurant.utilities;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.regex.*;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class Utility {


    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

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

    public static Date dateFromString(String date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date d = dateFormat.parse(date);
            return d;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
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

    public static boolean checkEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        if (email.isEmpty() || email == null) {
            return false;
        }
        Matcher matcher = emailPattern.matcher(email);

        return matcher.matches();
    }

    public static Boolean passwordValidator(String password, int PassLength) {
        // mot de passe doit contenir au moins passLength caractere
        if (password.length() < PassLength) {
            return false;
        }

        // mot de passe doit contenir au moins un chiffre
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        // mot de passe doit contenir au moins une lettre Minisc
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        // mot de passe doit contenir au moins une lettre MAJ
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        // mot de passe doit contenir au moins un caractere special
        if (!password.matches(".*[@#_].*")) {
            return false;
        }
        return true;
    }

    public static LocalTime StrToLocalTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:M");
        return LocalTime.parse(time, formatter);
    }

    public static String getDayOfWeekFrench() {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.FRENCH).toUpperCase();
    }

    public static String generateOrderCode(Long totalAmount, String restaurantName) {
        String resto = restaurantName.substring(0, 3).toUpperCase();
        String code = "CMD" + resto + String.valueOf(totalAmount);
        code += new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return code;
    }

    public static String genererNouveauApiKey() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
