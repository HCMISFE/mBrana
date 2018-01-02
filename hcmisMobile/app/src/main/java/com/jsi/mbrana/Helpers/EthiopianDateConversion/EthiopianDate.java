package com.jsi.mbrana.Helpers.EthiopianDateConversion;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Yohannes Tamru on 12/17/13.
 */
public class EthiopianDate {
    private int ethiopianYear, ethiopianMonth, ethiopianDay;
    private Date gregorianDate;
    //private EthiopianDate ethiopianDate;

    private Calendar calendar = Calendar.getInstance();

    //region Constructors

    public EthiopianDate(int ethiopianYear, int ethiopianMonth, int ethiopianDay)
    {
        this.ethiopianYear = ethiopianYear;
        this.ethiopianMonth = ethiopianMonth;
        this.ethiopianDay = ethiopianDay;

        gregorianDate = EthiopianDateConversion.EthiopianDateToGregorianDate(this.ethiopianYear, this.ethiopianMonth, this.ethiopianDay);
        calendar.setTime(gregorianDate);
    }

    public EthiopianDate(Date gregorianDate)
    {
        this.gregorianDate = gregorianDate;
        calendar.setTime(gregorianDate);

        EthiopianDate date = EthiopianDateConversion.GregorianDateToEthiopianDate(gregorianDate);
        this.ethiopianYear = date.GetYear();
        this.ethiopianMonth = date.GetMonth();
        this.ethiopianDay = date.GetDay();
    }

    //endregion

    //region Getters

    //region Getters for Ethiopian Date
    public int GetYear()
    {
        return ethiopianYear;
    }

    public int GetMonth()
    {
        return ethiopianMonth;
    }

    public int GetDay()
    {
        return ethiopianDay;
    }

    public String GetGeezeDay()
    {
        return GeezeNumeralConversion.ToEthiopianNumeral(ethiopianDay);
    }

    public String GetEthiopianDayOfWeek()
    {
        switch(calendar.get(Calendar.DAY_OF_WEEK) )
        {
            case 1: return "እሁድ";
            case 2: return "ሰኞ";
            case 3: return "ማክሰኞ";
            case 4: return "እሮብ";
            case 5: return "ሀሙስ";
            case 6: return "አርብ";
            case 7: return "ቅዳሜ";
        }
        return "";
    }

    //endregion

    //region  Getters for Gregorian date
    public int GetGregorianDay()
    {
        if(gregorianDate == null)
        {
            gregorianDate = EthiopianDateConversion.EthiopianDateToGregorianDate(ethiopianYear,ethiopianMonth,ethiopianDay);
            calendar.setTime(gregorianDate);
        }

        return calendar.get(Calendar.DATE);
    }

    public int GetGregorianMonth()
    {
        if(gregorianDate == null)
        {
            gregorianDate = EthiopianDateConversion.EthiopianDateToGregorianDate(ethiopianYear,ethiopianMonth,ethiopianDay);
            calendar.setTime(gregorianDate);
        }

        return calendar.get(Calendar.MONTH) + 1; //Again, thanks for ass who decided calendar.get(Calendar.MONTH) should be a zero based index
    }

    public int GetGregorianYear()
    {
        if(gregorianDate == null)
        {
            gregorianDate = EthiopianDateConversion.EthiopianDateToGregorianDate(ethiopianYear,ethiopianMonth,ethiopianDay);
            calendar.setTime(gregorianDate);
        }

        return calendar.get(Calendar.YEAR);
    }

    public Date GetGregorianDate()
    {
        return gregorianDate;
    }
    //endregion

    //endregion

    //region ToStrings
    public String ToLongEthiopianDateString()
    {
        return  GetEthiopianDayOfWeek() + ", " + EthiopianMonth.GetMonthName(ethiopianMonth) + " " + this.ethiopianDay + ", " + this.GetYear();
    }

    public String ToShortEthiopianDateString()
    {
        return this.ethiopianDay + "/" + this.ethiopianMonth + "/" + this.ethiopianYear;
    }

    public String ToShortGregorianDateString()
    {
        if(gregorianDate == null)
        {
            gregorianDate = EthiopianDateConversion.EthiopianDateToGregorianDate(ethiopianYear,ethiopianMonth,ethiopianDay);
            calendar.setTime(gregorianDate);
        }

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(gregorianDate);
    }

    public String ToLongGregorianDateString()
    {
        if(gregorianDate == null)
        {
            gregorianDate = EthiopianDateConversion.EthiopianDateToGregorianDate(ethiopianYear,ethiopianMonth,ethiopianDay);
            calendar.setTime(gregorianDate);
        }

        DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        return dateFormat.format(gregorianDate);
    }

    public boolean equals(EthiopianDate otherDate)
    {
        return this.GetDay() == otherDate.GetDay() && this.GetMonth() == otherDate.GetMonth() && this.GetYear() == otherDate.GetYear();
    }
    //endregion

    //region navigation
    public EthiopianDate Tomorrow()
    {
       return null;
    }

    public EthiopianDate Yesterday()
    {
        return null;
    }
    //endregion
}