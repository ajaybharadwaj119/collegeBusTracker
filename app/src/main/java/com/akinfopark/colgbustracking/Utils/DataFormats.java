package com.akinfopark.colgbustracking.Utils;

import android.util.Log;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class
DataFormats {

    public static NumberFormat formatCurrency = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
    public static String rupeeSign = "₹";

    public static SimpleDateFormat dateFormatDb = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static SimpleDateFormat dateFormatFile = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());
    public static SimpleDateFormat dateFormatDbDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static SimpleDateFormat dateFormatDbTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    public static SimpleDateFormat dateFormatNative = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
    public static SimpleDateFormat dateFormatNative2 = new SimpleDateFormat("dd-MMM-yyyy\nhh:mm a", Locale.getDefault());
    public static SimpleDateFormat dateFormatNativeDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
    public static SimpleDateFormat dateFormatNativeDate3 = new SimpleDateFormat("MMM-dd", Locale.getDefault());
    public static SimpleDateFormat dateFormatNativeDate4 = new SimpleDateFormat("MMM-dd-yy", Locale.getDefault());
    public static SimpleDateFormat dateFormatNativeDate1 = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
    public static SimpleDateFormat dateFormatNativeDate2 = new SimpleDateFormat("dd/MMM/yy", Locale.getDefault());
    public static SimpleDateFormat dateFormatNativeDate5 = new SimpleDateFormat("dd/MMM", Locale.getDefault());
    public static SimpleDateFormat dateFormatNativeTime = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    public static SimpleDateFormat dateFormatTimeStampChat = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    public DataFormats() {
       /* DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("Rs ");
        ((DecimalFormat) formatCurrency).setDecimalFormatSymbols(dfs);*/
    }

    public static String convertTimeStamp(long timestamp, SimpleDateFormat dateFormat) {
        Date date = new Date(timestamp * 1000L);
        return dateFormat.format(date);
    }


    public static String getFormattedDateTime(String dbDate) {

        if (dbDate.equalsIgnoreCase("0000-00-00 00:00:00")) {
            return "";
        }
        try {
            return DataFormats.dateFormatNative.format(DataFormats.dateFormatDb.parse(dbDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return dbDate;
        }
    }

    public static String getFormattedDate(String dbDate) {
        if (dbDate.equalsIgnoreCase("0000-00-00 00:00:00")) {
            return "";
        }
        try {
            return DataFormats.dateFormatNativeDate.format(DataFormats.dateFormatDb.parse(dbDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return dbDate;
        }
    }

    public static String getFormattedTime(String dbDate) {
        if (dbDate.equalsIgnoreCase("0000-00-00 00:00:00")) {
            return "";
        }
        try {
            return DataFormats.dateFormatNativeTime.format(DataFormats.dateFormatDb.parse(dbDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return dbDate;
        }
    }

    public static String getFormattedDateFromDate(String dbDate) {
        if (dbDate.equalsIgnoreCase("0000-00-00")) {
            return "";
        }
        try {
            return DataFormats.dateFormatNativeDate.format(DataFormats.dateFormatDbDate.parse(dbDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return dbDate;
        }
    }

    public static String getFormattedTimeFromTime(String dbDate) {
        /*if (dbDate.equalsIgnoreCase("00:00:00")) {
            return "";
        }*/
        try {
            return DataFormats.dateFormatNativeTime.format(DataFormats.dateFormatDbTime.parse(dbDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return dbDate;
        }
    }

    public static String getFormattedAmt(String amt) {
        try {
            return formatCurrency.format(Double.parseDouble(amt));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            if (amt == null || amt.equalsIgnoreCase(""))
                return "₹ 0.00";
            return "₹" + amt;
        }
    }

    public static String getFormattedAmtRs(String amt) {
        try {
            return formatCurrency.format(Double.parseDouble(amt));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return amt;
        }
    }

    public static String getFormattedAmt(double amt) {
        try {
            return formatCurrency.format(patchDecimalDouble(amt));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "" + amt;
        }
    }

    public static String convertDate(String dbDate, SimpleDateFormat format1, SimpleDateFormat format2) {
        if (dbDate.equalsIgnoreCase("0000-00-00") || dbDate.equalsIgnoreCase("0000-00-00 00:00:00") || dbDate.equalsIgnoreCase("00:00")) {
            return "";
        }
        try {
            String formatted = format2.format(format1.parse(dbDate));
            Log.i("FormattedDateOK", formatted);
            return formatted;
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("FormattedDateError", dbDate);
            return dbDate;
        }
    }

    public static String getCurrentDate(SimpleDateFormat format2) {

        return format2.format(new Date());
    }

    public static long convertDateToUnix(SimpleDateFormat format, String date) {
        try {
            Date dateToConvert = format.parse(date);
            if (dateToConvert != null) {
                return dateToConvert.getTime() / 1000L;
            }
            return 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

    }

    public static String getTodayYesterday(String date) {
        try {


            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date dateToConvert = dateFormatNativeDate.parse(date + " 12:00 AM");
            long currentUnix = calendar.getTime().getTime() / 1000L;

            long givenUnix = (dateToConvert != null ? dateToConvert.getTime() : 0) / 1000L;

            long difference = currentUnix - givenUnix;

            Log.i("TodayYesterday", currentUnix + "-" + givenUnix + "=" + difference);

            if (currentUnix == givenUnix) {
                return "Today";
            } else if (difference > 86399 && difference < 172800) {
                return "Yesterday";
            } else {
                return date;
            }

        } catch (ParseException e) {

            e.printStackTrace();
            return date;
        }

    }

    public static String convertDateFormat(String inputDate, String outputFormat,String inputFormat) {
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputFormat);
            Date date = inputDateFormat.parse(inputDate);

            SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat);
            return outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String patchDecimal(double d) {
        BigDecimal bdTest = bigDecimal(d);
        return String.valueOf(bdTest);
    }

    public static double patchDecimalDouble(double d) {
        BigDecimal bdTest = bigDecimal(d);
        return bdTest.doubleValue();
    }

    public static BigDecimal bigDecimal(double d) {
        BigDecimal bdTest = new BigDecimal("" + d);
        bdTest = bdTest.setScale(2, BigDecimal.ROUND_HALF_UP);
        Log.d("DecimalO", String.valueOf(d));
        Log.d("DecimalR", String.valueOf(bdTest));
        return bdTest;
    }
}

