package com.jsi.mbrana.Helpers.EthiopianDateConversion;

 /*
  * Created by Yohannes Tamru on 12/21/13.
  */

import java.util.ArrayList;
import java.util.List;

public class GeezeNumeralConversion {
   private static final char one = '፩', two = '፪', three = '፫', four = '፬', five = '፭', six = '፮', seven = '፯',
    eight = '፰', nine = '፱', ten = '፲', twenty = '፳', thirty = '፴', fourty = '፵', fifty = '፶',
    sixty = '፷', seventy = '፸', eighty ='፹', ninety ='፺', hundred = '፻', twoHundred = '፼';

    public static String ToEthiopianNumeral(int number)
    {
        number = Math.abs(number);
        String arabianNumeral = String.valueOf(number);
        if(arabianNumeral.length() < 3)
        {
             return String.valueOf(ToEthiopianNumeralBasicConversion(number));
        }
        else
        {
            List<Integer> tens = GetTens(number);
            String ConvertedString = "";
            for(int i = 0; i < tens.size(); i++)
            {
                int currentTen = tens.get(i);
                String convertedTen = ToEthiopianNumeralBasicConversion(currentTen);
                if(i != tens.size() - 1)
                {
                    if(i % 2 == 0)
                    {
                        if(currentTen == 0)
                            continue;
                        else if(currentTen == 1)
                            convertedTen = String.valueOf(hundred);
                         else
                            convertedTen += hundred;
                    }
                    else
                    {
                        if(currentTen == 0)
                            continue;
                        else if(currentTen == 1)
                            convertedTen = String.valueOf(twoHundred);
                        else
                            convertedTen += twoHundred;
                    }
                }
                ConvertedString += convertedTen;
            }

            return ConvertedString;
        }
    }

    private static List<Integer> GetTens(int arabianNumber)
    {   //Gets a number like 5694342
        //returns a list of Integer [5], [69], [43], [42]
        String arabianNumeral = String.valueOf(arabianNumber);
        List<Integer> numerals = new ArrayList<Integer>();
        while(arabianNumeral.length() > 2)
        {
            String lastTwoDigits = arabianNumeral.substring(arabianNumeral.length() - 2, arabianNumeral.length());
            numerals.add(0, Integer.valueOf(lastTwoDigits));
            arabianNumeral = arabianNumeral.substring(0 , arabianNumeral.length() - 2);
        }
        numerals.add(0, Integer.valueOf(arabianNumeral));
        return numerals;
    }

    private static String ToEthiopianNumeralBasicConversion(int arabianNumeral)
    {
        arabianNumeral = Math.abs(arabianNumeral);
        switch (arabianNumeral)
        {
            case 0: return "-";
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 20:
            case 30:
            case 40:
            case 50:
            case 60:
            case 70:
            case 80:
            case 90:
                return String.valueOf(GeezeBaseNumeralFactory(arabianNumeral));
            default:
                int quotient = arabianNumeral / 10;
                int remainder = arabianNumeral % 10;
                return String.valueOf((char)GeezeBaseNumeralFactory(quotient * 10)) + String.valueOf((char)GeezeBaseNumeralFactory(remainder));
        }
    }

    private static char GeezeBaseNumeralFactory(int arabianNumeral)
    {
        arabianNumeral = Math.abs(arabianNumeral);
        if((arabianNumeral < 10 || arabianNumeral % 10 == 0) && arabianNumeral <= 200 )
        {
            switch (arabianNumeral)
            {
                case 1: return one;
                case 2: return two;
                case 3: return three;
                case 4: return four;
                case 5: return five;
                case 6: return six;
                case 7: return seven;
                case 8: return eight;
                case 9: return nine;
                case 10: return ten;
                case 20: return twenty;
                case 30: return thirty;
                case 40: return  fourty;
                case 50: return fifty;
                case 60: return sixty;
                case 70: return seventy;
                case 80: return eighty;
                case 90: return ninety;
                case 100: return hundred;
                case 200: return twoHundred;
            }
        }
        else
        {
            return '-';//throw new IllegalArgumentException("Unsupported numeral request");
        }
        return '-';//this means conversion didn't work
    }

    private static int ToArabianNumeralBasicConversion(char ethiopianNumeral)
    {
        switch (ethiopianNumeral)
        {
            case one:
                return 1;
            case two:
                return 2;
            case three:
                return 3;
            case four:
                return 4;
            case five:
                return 5;
            case six:
                return 6;
            case seven:
                return 7;
            case eight:
                return 8;
            case nine:
                return 9;
            case ten:
                return 10;
            case twenty:
                return 20;
            case thirty:
                return 30;
            case fourty:
                return 40;
            case fifty:
                return 50;
            case sixty:
                return 60;
            case seventy:
                return 70;
            case eighty:
                return 80;
            case ninety:
                return 90;
            case hundred:
                return 100;
            case twoHundred:
                return 200;
            default:
                return -1;
        }/**/
    }

}