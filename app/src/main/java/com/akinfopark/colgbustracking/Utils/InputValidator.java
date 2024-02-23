package com.akinfopark.colgbustracking.Utils;

import android.text.TextUtils;
import android.util.Log;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AK INFOPARK on 11-09-2017.
 */

public class InputValidator {

    public boolean validatePan(String pan) {
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

        Matcher matcher = pattern.matcher(pan);
// Check if pattern matches
        if (matcher.matches()) {
            Log.i("Matching", "Yes");
            return true;
        } else {
            return false;
        }


    }

    public static boolean isValidAadhaar(String value) {
        String regex = "^\\d{4}\\s\\d{4}\\s\\d{4}$";
        final Pattern pattern = Pattern.compile(regex);
        //Log.i("OcrVa", value.trim());
        if (value.length() > 0) {
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                if (Character.isSpaceChar(c)) {
                    //Log.i("OcrVaSpace","Space");
                }
            }

        }
// This can be repeated in a loop with different inputs:
        Matcher matcher = pattern.matcher(value);

        return matcher.matches();

    }

    public static boolean isValidNum(String num) {
        try {

            final String regExp = "[0-9]+([,.][0-9]{1,2})?";
            final Pattern pattern = Pattern.compile(regExp);

// This can be repeated in a loop with different inputs:
            Matcher matcher = pattern.matcher(num);
            if (matcher.matches()) {
                return Double.parseDouble(num) > 0 /*&& Double.valueOf (num)<=500000*/ && BigDecimal.valueOf(Double.parseDouble(num)).scale() <= 2;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean validateName(String txt) {

       /* String regx = "/^[a-zA-Z\\s]*$/";
        Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(txt);
        return matcher.find();*/
        String regx = "[\\p{Alnum}\\s]";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(txt);
        boolean isName = true;
        /*if(!matcher.matches()){
            return false;
        }*/
        if (txt.equalsIgnoreCase("")) {
            return false;
        }

        for (int i = 0; i < txt.length(); i++) {
            char charAt = txt.charAt(i);
            if (!Character.isSpaceChar(charAt)) {
                if (!Character.isLetter(charAt)) {
                    isName = false;
                }

            }

        }
        /*if (!Character.isUpperCase(txt.charAt(0))) {
            isName = false;
        }*/
        return isName;
    }

    public boolean validateIfsc(String ifsc) {
        Pattern pattern = Pattern.compile("[A-Z]{4}[0-9]{7}");

        Matcher matcher = pattern.matcher(ifsc);
// Check if pattern matches
        if (matcher.matches()) {
            Log.i("Matching", "Yes");
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidYouTubeUrl(String url) {
        Pattern pattern = Pattern.compile("^((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)([\\w\\-]+)(\\S+)?$");
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidEmailOptioanl(CharSequence target) {
        return TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
/*
    public static ValidationResult isValidEmailE(CharSequence target) {
        ValidationResult validationResult = new ValidationResult();
        validationResult.isValid = false;
        if (target.toString().trim().equals("")) {
            validationResult.isValid = false;
            validationResult.message = "Enter email";
        } else if (!isValidEmail(target)) {
            validationResult.isValid = false;
            validationResult.message = App.getInstance().getString(R.string.invalid_email);
        } else {
            validationResult.isValid = true;
        }
        return validationResult;
    }
*/

  /*  public static ValidationResult isValidMobileEmpty(String target) {
        ValidationResult validationResult = new ValidationResult();
        validationResult.isValid = false;
        if (target.trim().equals("")) {
            validationResult.isValid = false;
            validationResult.message = "Enter mobile number";
        } else if (!isValidMob(target)) {
            validationResult.isValid = false;
            validationResult.message = App.getInstance().getString(R.string.phone_validation);
        } else {
            validationResult.isValid = true;
        }
        return validationResult;
    }*/

    public static boolean isValidEmailOptional(String target) {
        return TextUtils.isEmpty(target) || android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidMobile(String mobile) {
        if (mobile.length() > 9 /*&& mobile.length() < 12*/) {
            for (int i = 0; i < mobile.length(); i++) {
                char charAt = mobile.charAt(i);
                if (!Character.isDigit(charAt)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }

    }

    public static boolean isValidPass(String password) {

        String PASSWORD_PATTERN = "((?=.*[a-z][A-Z])(?=.*\\d)(?=.*[@#$%!]).{8,20})";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        //return matcher.matches();
        return !password.contains(" ") && password.length() >= 8 && password.length() < 17;
    }

    /*public static ValidationResult isValidPassE(String password) {

        ValidationResult validationResult = new ValidationResult();
        validationResult.isValid = false;
        if (password.trim().equalsIgnoreCase("")) {
            validationResult.message = "Enter password";

        } else if (!isValidPass(password)) {
            validationResult.message = App.getInstance().getString(R.string.password_validation);

        } else {
            validationResult.isValid = true;
        }
        return validationResult;
    }*/


    public static boolean isValidCardNum(String cardNum) {
        if (cardNum.length() > 12) {
            for (int i = 0; i < cardNum.length(); i++) {
                char charAt = cardNum.charAt(i);
                if (!Character.isDigit(charAt)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }

    }

    public static boolean isValidMob(String mobile) {
        if (mobile.length() > 9 && mobile.length() < 12) {
            for (int i = 0; i < mobile.length(); i++) {
                char charAt = mobile.charAt(i);
                if (!Character.isDigit(charAt)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }

    }

    public static boolean validateNameEng(String txt) {

        boolean isName = true;
        /*if(!matcher.matches()){
            return false;
        }*/
        if (txt.equalsIgnoreCase("")) {
            return false;
        }

        for (int i = 0; i < txt.length(); i++) {
            char charAt = txt.charAt(i);
            if (!Character.isSpaceChar(charAt)) {
                if (!((int) charAt >= 65 && (int) charAt <= 90 || ((int) charAt >= 97 && (int) charAt <= 122))) {
                    isName = false;
                }

            }
        }
        /*if (!Character.isUpperCase(txt.charAt(0))) {
            isName = false;
        }*/
        return isName;
    }

    /*public static ValidationResult validateNameEngE(String txt) {

        boolean isName = true;
        *//*if(!matcher.matches()){
            return false;
        }*//*
        ValidationResult validationResult = new ValidationResult();
        if (txt.equalsIgnoreCase("")) {
           validationResult.isValid = false;
           validationResult.message ="Enter name";
        }

        for (int i = 0; i < txt.length(); i++) {
            char charAt = txt.charAt(i);
            if (!Character.isSpaceChar(charAt)) {
                if (!((int) charAt >= 65 && (int) charAt <= 90 || ((int) charAt >= 97 && (int) charAt <= 122))) {
                    isName = false;
                }

            }
        }
        *//*if (!Character.isUpperCase(txt.charAt(0))) {
            isName = false;
        }*//*
        if(!isName){
            validationResult.message = "Enter name";
        }
        validationResult.isValid = isName;

        return validationResult;
    }*/


    public static boolean isValidCardNumPattern(long number) {
//return true;
        return (getSize(number) >= 13 &&
                getSize(number) <= 16) &&
                (prefixMatched(number, 4) ||
                        prefixMatched(number, 5) ||
                        prefixMatched(number, 37) ||
                        prefixMatched(number, 6)) &&
                ((sumOfDoubleEvenPlace(number) +
                        sumOfOddPlace(number)) % 10 == 0);
    }

    // Get the result from Step 2
    public static int sumOfDoubleEvenPlace(long number) {
        int sum = 0;
        String num = number + "";
        for (int i = getSize(number) - 2; i >= 0; i -= 2)
            sum += getDigit(Integer.parseInt(num.charAt(i) + "") * 2);

        return sum;
    }

    // Return this number if it is a single digit, otherwise,
    // return the sum of the two digits
    public static int getDigit(int number) {
        if (number < 9)
            return number;
        return number / 10 + number % 10;
    }

    // Return sum of odd-place digits in number
    public static int sumOfOddPlace(long number) {
        int sum = 0;
        String num = number + "";
        for (int i = getSize(number) - 1; i >= 0; i -= 2)
            sum += Integer.parseInt(num.charAt(i) + "");
        return sum;
    }

    // Return true if the digit d is a prefix for number
    public static boolean prefixMatched(long number, int d) {
        return getPrefix(number, getSize(d)) == d;
    }

    // Return the number of digits in d
    public static int getSize(long d) {
        String num = d + "";
        return num.length();
    }

    // Return the first k number of digits from
    // number. If the number of digits in number
    // is less than k, return number.
    public static long getPrefix(long number, int k) {
        if (getSize(number) > k) {
            String num = number + "";
            return Long.parseLong(num.substring(0, k));
        }
        return number;
    }
}
