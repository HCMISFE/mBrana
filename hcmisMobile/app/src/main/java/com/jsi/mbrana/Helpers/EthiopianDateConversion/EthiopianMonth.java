package com.jsi.mbrana.Helpers.EthiopianDateConversion;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Yohannes Tamru on 12/19/13.
 */
public class EthiopianMonth {
    private List<EthiopianDate> monthDates = new ArrayList<EthiopianDate>();
    private int MonthNumber = 1, Year;

    //region Constructors
    public EthiopianMonth(MONTHS selectedMONTH, int Year)
    {
        this.Year = Year;
        switch (selectedMONTH)
        {
            case MESKEREM:
                MonthNumber = 1;
                monthDates = MonthDatesFactory(Year, 1);
                break;
            case TIKIMT:
                MonthNumber = 2;
                monthDates = MonthDatesFactory(Year, 2);
                break;
            case HIDAR:
                MonthNumber = 3;
                monthDates = MonthDatesFactory(Year, 3);
                break;
            case TAHESAS:
                MonthNumber = 4;
                monthDates = MonthDatesFactory(Year, 4);
                break;
            case TIR:
                MonthNumber = 5;
                monthDates = MonthDatesFactory(Year, 5);
                break;
            case YEKATIT:
                MonthNumber = 6;
                monthDates = MonthDatesFactory(Year, 6);
                break;
            case MEGABIT:
                MonthNumber = 7;
                monthDates = MonthDatesFactory(Year, 7);
                break;
            case MIAZIA:
                MonthNumber = 8;
                monthDates = MonthDatesFactory(Year, 8);
                break;
            case GINBOT:
                MonthNumber = 9;
                monthDates = MonthDatesFactory(Year, 9);
                break;
            case SENE:
                MonthNumber = 10;
                monthDates = MonthDatesFactory(Year, 10);
                break;
            case HAMLE:
                MonthNumber = 11;
                monthDates = MonthDatesFactory(Year, 11);
                break;
            case NEHASE:
                MonthNumber = 12;
                monthDates = MonthDatesFactory(Year, 12);
                break;
            case PAGUME:
                MonthNumber = 13;
                monthDates = MonthDatesFactory(Year, 13);
                break;
        }
    }

    public EthiopianMonth(int MonthNumber, int Year)
    {
        this.Year = Year;
        this.MonthNumber = MonthNumber;
        switch (MonthNumber)
        {
            case 1:
                monthDates = MonthDatesFactory(Year, 1);
                break;
            case 2:
                monthDates = MonthDatesFactory(Year, 2);
                break;
            case 3:
                monthDates = MonthDatesFactory(Year, 3);
                break;
            case 4:
                monthDates = MonthDatesFactory(Year, 4);
                break;
            case 5:
                monthDates = MonthDatesFactory(Year, 5);
                break;
            case 6:
                monthDates = MonthDatesFactory(Year, 6);
                break;
            case 7:
                monthDates = MonthDatesFactory(Year, 7);
                break;
            case 8:
                monthDates = MonthDatesFactory(Year, 8);
                break;
            case 9:
                monthDates = MonthDatesFactory(Year, 9);
                break;
            case 10:
                monthDates = MonthDatesFactory(Year, 10);
                break;
            case 11:
                monthDates = MonthDatesFactory(Year, 11);
                break;
            case 12:
                monthDates = MonthDatesFactory(Year, 12);
                break;
            case 13:
                monthDates = MonthDatesFactory(Year, 13);
                break;
        }
    }
    //endregion

    //region Getters

    public List<EthiopianDate> GetDates()
    {
        return monthDates;
    }

    public static String GetMonthName(int month)
    {
        if(month < 1 || month > 13)
            throw new IllegalArgumentException("Month cannot be less than zero or greater than thirteen");

        switch(month)
        {
            case 1: return "መስከረም";
            case 2: return "ጥቅምት";
            case 3: return  "ሕዳር";
            case 4: return  "ታህሳስ";
            case 5: return  "ጥር";
            case 6: return  "የካቲት";
            case 7: return  "መጋቢት";
            case 8: return  "ሚያዚያ";
            case 9: return  "ግንቦት";
            case 10: return  "ሰኔ";
            case 11: return  "ሐምሌ";
            case 12: return  "ነሐሴ";
            case 13: return  "ጳጉሜ";
        }
        return "መስከረም"; //It should never get here, but if for some reason it does, i'm returning meskerem, 1/13 chance of being correct
    }

    public int GetYear()
    {
        return this.Year;
    }

    public String GetMonthName()
    {
        return GetMonthName(MonthNumber);
    }

    //endregion

    //region Navigation

    public EthiopianMonth NextMonth()
    {
        int nextMonth, year;
        if(MonthNumber < 13)
        {
            nextMonth = MonthNumber + 1;
            year = Year;
        }
        else
        {
            nextMonth = 1;
            year = Year + 1;
        }
        return new EthiopianMonth(nextMonth, year);
    }

    public EthiopianMonth PreviousMonth()
    {
        int previousMonth, year;
        if(MonthNumber > 1)
        {
            previousMonth = MonthNumber - 1;
            year = Year;
        }
        else
        {
            previousMonth = 13;
            year = Year - 1;
        }
        return new EthiopianMonth(previousMonth, year);
    }

    public EthiopianMonth PreviousYear()
    {
        return new EthiopianMonth(MonthNumber, Year - 1);
    }

    public EthiopianMonth NextYear()
    {
        return new EthiopianMonth(MonthNumber, Year + 1);
    }

    //endregion

    //region Helpers

    private List<EthiopianDate> MonthDatesFactory(int ethiopianYear, int ethiopianMonth)
    {
        List<EthiopianDate> dates = new ArrayList<EthiopianDate>();

        if(ethiopianMonth < 13 && ethiopianMonth > 0)
            for(int day = 1; day <= 30; day++)
                dates.add(new EthiopianDate(ethiopianYear, ethiopianMonth, day));
        else if (ethiopianMonth == 13)
        {
            if(EthiopianYear.IsLeapYear(ethiopianYear))
                for(int day = 1; day <= 6; day++)
                    dates.add(new EthiopianDate(ethiopianYear, ethiopianMonth, day));
            else
                for(int day = 1; day <= 5; day++)
                    dates.add(new EthiopianDate(ethiopianYear, ethiopianMonth, day));

        }

        return AlignSundayAsFirstDayOfWeek(dates, ethiopianYear, ethiopianMonth);
    }

    private List<EthiopianDate> AlignSundayAsFirstDayOfWeek(List<EthiopianDate> dates, int ethiopianYear, int ethiopianMonth)
    {
        if(ethiopianMonth > 1 && ethiopianMonth <= 13)
        {
            int lastSundayOfLastMonthIndex = GetLastSundayOfLastMonthIndex(dates);
            if(lastSundayOfLastMonthIndex == 23)
                return dates;

            List<EthiopianDate> additions = new ArrayList<EthiopianDate>();
            for(int i = lastSundayOfLastMonthIndex + 1; i < 31; i++)
            {
                additions.add(new EthiopianDate(ethiopianYear, ethiopianMonth - 1, i));
            }
            dates.addAll(0, additions);
        }
        else if (ethiopianMonth == 1)
        {
            int lastSundayOfLastMonthIndex = GetLastSundayOfLastMonthIndex(dates);
            //if(lastSundayOfLastMonthIndex == 0)
            //    return dates;

            List<EthiopianDate> additions = new ArrayList<EthiopianDate>();
            for(int i = lastSundayOfLastMonthIndex + 1; i < 5; i++)
            {
                additions.add(new EthiopianDate(ethiopianYear, ethiopianMonth - 1, i));
            }
            dates.addAll(0, additions);
        }
        return dates;
    }

    private List<EthiopianDate> AlignSundayAsLastDayOfWeek(List<EthiopianDate> dates, int ethiopianYear, int ethiopianMonth)
    {
        int firstSundayLocation = GetFirstSundayOfTheMonthIndex(dates);
        if(firstSundayLocation == 0)
            return dates;

        int neededAddition = 6 - firstSundayLocation;
        List<EthiopianDate> additions = new ArrayList<EthiopianDate>();
        for(int i = 0; i < neededAddition; i++)
        {
                additions.add(new EthiopianDate(ethiopianYear, ethiopianMonth - 1, 30 - i));
        }
        dates.addAll(0, additions);
        return dates;
    }

    private static int GetLastSundayOfLastMonthIndex(List<EthiopianDate> dates)
    {
        for(int i = 0; i < dates.size() ; i++)
        {
            if(dates.get(i).GetEthiopianDayOfWeek().equals("እሁድ"))
            {
                return 30 -  (7 - i);
            }
        }
        return 6; //it shouldn't really get hear
    }

    private static int GetFirstSundayOfTheMonthIndex(List<EthiopianDate> dates)
    {
       for(int i = 0; i < dates.size() ; i++)
       {
           if(dates.get(i).GetEthiopianDayOfWeek().equals("እሁድ"))
           {
               return i;
           }
       }
       return 6; //it shouldn't really get hear
    }

    public static int GetFirstDayOfMonthIndex(List<EthiopianDate> dates)
    {
        for(int i = 0; i < dates.size() ; i++)
        {
            if(dates.get(i).GetDay() == 1)
            {
                return i;
            }
        }
        return 6; //it shouldn't really get hear
    }
    //endregion
}
