package com.jsi.mbrana.Helpers.EthiopianDateConversion;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Yohannes Tamru on 12/19/13.
 */
public class EthiopianYear {
   private EthiopianMonth Meskerem, Tikimt, Hidar, Tahesas, Tir, Yekatit, Megabit, Miazia, Ginbot, Sene, Hamle, Nehase, Pagume;
   private int ethiopianYear;


    public EthiopianYear(int ethiopianYear)
    {
        this.ethiopianYear = ethiopianYear;
        Meskerem = new EthiopianMonth(MONTHS.MESKEREM, ethiopianYear);
        Tikimt = new EthiopianMonth(MONTHS.TIKIMT, ethiopianYear);
        Hidar = new EthiopianMonth(MONTHS.HIDAR, ethiopianYear);
        Tahesas = new EthiopianMonth(MONTHS.TAHESAS, ethiopianYear);
        Tir = new EthiopianMonth(MONTHS.TIR, ethiopianYear);
        Yekatit = new EthiopianMonth(MONTHS.YEKATIT, ethiopianYear);
        Megabit = new EthiopianMonth(MONTHS.MEGABIT, ethiopianYear);
        Miazia = new EthiopianMonth(MONTHS.MIAZIA, ethiopianYear);
        Ginbot = new EthiopianMonth(MONTHS.GINBOT, ethiopianYear);
        Sene = new EthiopianMonth(MONTHS.SENE, ethiopianYear);
        Hamle = new EthiopianMonth(MONTHS.HAMLE, ethiopianYear);
        Nehase = new EthiopianMonth(MONTHS.NEHASE, ethiopianYear);
        Pagume = new EthiopianMonth(MONTHS.PAGUME, ethiopianYear);
    }

    public EthiopianYear()
    {
        this.ethiopianYear = 1855;
        Meskerem = new EthiopianMonth(MONTHS.MESKEREM, ethiopianYear);
        Tikimt = new EthiopianMonth(MONTHS.TIKIMT, ethiopianYear);
        Hidar = new EthiopianMonth(MONTHS.HIDAR, ethiopianYear);
        Tahesas = new EthiopianMonth(MONTHS.TAHESAS, ethiopianYear);
        Tir = new EthiopianMonth(MONTHS.TIR, ethiopianYear);
        Yekatit = new EthiopianMonth(MONTHS.YEKATIT, ethiopianYear);
        Megabit = new EthiopianMonth(MONTHS.MEGABIT, ethiopianYear);
        Miazia = new EthiopianMonth(MONTHS.MIAZIA, ethiopianYear);
        Ginbot = new EthiopianMonth(MONTHS.GINBOT, ethiopianYear);
        Sene = new EthiopianMonth(MONTHS.SENE, ethiopianYear);
        Hamle = new EthiopianMonth(MONTHS.HAMLE, ethiopianYear);
        Nehase = new EthiopianMonth(MONTHS.NEHASE, ethiopianYear);
        Pagume = new EthiopianMonth(MONTHS.PAGUME, ethiopianYear);
    }

    public EthiopianMonth getMeskerem() {
        return Meskerem;
    }

    public EthiopianMonth getTikimt() {
        return Tikimt;
    }

    public EthiopianMonth getHidar() {
        return Hidar;
    }

    public EthiopianMonth getTahesas() {
        return Tahesas;
    }

    public EthiopianMonth getTir() {
        return Tir;
    }

    public EthiopianMonth getYekatit() {
        return Yekatit;
    }

    public EthiopianMonth getMegabit() {
        return Megabit;
    }

    public EthiopianMonth getMiazia() {
        return Miazia;
    }

    public EthiopianMonth getGinbot() {
        return Ginbot;
    }

    public EthiopianMonth getSene() {
        return Sene;
    }

    public EthiopianMonth getHamle() {
        return Hamle;
    }

    public EthiopianMonth getNehase() {
        return Nehase;
    }

    public EthiopianMonth getPagume() {
        return Pagume;
    }

    public int getEthiopianYear() {
        return ethiopianYear;
    }

    public List<EthiopianMonth> GetMonths()
    {
        List<EthiopianMonth> months = new ArrayList<EthiopianMonth>();
        months.add(Meskerem);
        months.add(Tikimt);
        months.add(Hidar);
        months.add(Tahesas);
        months.add(Tir);
        months.add(Yekatit);
        months.add(Megabit);
        months.add(Miazia);
        months.add(Ginbot);
        months.add(Sene);
        months.add(Hamle);
        months.add(Nehase);
        months.add(Pagume);

        return months;
    }

    public boolean IsLeapYear()
    {
        return this.ethiopianYear + 1 % 4 == 0;
    }

    public static boolean IsLeapYear(int ethiopianYear)
    {
        return (ethiopianYear + 1) % 4 == 0;
    }
}
