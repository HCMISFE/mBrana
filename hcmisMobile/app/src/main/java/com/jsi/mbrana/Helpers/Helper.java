package com.jsi.mbrana.Helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ListView;

import com.jsi.mbrana.Workflow.Reports.ReportSlideFragent.Fragment_Primary;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sololia on 6/14/2016.
 */
public class Helper {
    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isNetworkAvailable(Fragment_Primary fragment) {
        ConnectivityManager connMgr = (ConnectivityManager) fragment.getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getMomentFromNow(Date date) {
        PrettyTime prettyTime = new PrettyTime();
        return prettyTime.format(date);
    }

    public static String EmptyValueCleanUp(String value) {
        if (value.equals("")) {
            return "-";
        } else {

            return value;
        }
    }

    public static String ManufacturerCleanup(String value) {
        if (value.length() > 20) {
            return value.substring(0, 21);
        } else {

            return value;
        }
    }

    public static String ReceiptStatusCleanup(String ReceiptStatus) {
        if (ReceiptStatus.equals("Draft Receive")) {
            return "Draft";
        } else if (ReceiptStatus.equals("Receive Entered")) {
            return "Pending Confirmation";
        } else if (ReceiptStatus.equals("GRNF Printed")) {
            return "Confirmed";
        } else {
            return ReceiptStatus;
        }
    }

    public static String DocumentTypeCleanup(String DocumentType) {
        if (DocumentType != null) {
            switch (DocumentType) {
                case "DLVN":
                    return "DNV";
                default:
                    return DocumentType;
            }
        } else {
            return "";
        }
    }

    public static Bitmap mergeTwoBitmap(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight() + bmp2.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, bmp1.getHeight(), null);
        return bmOverlay;
    }

    public static Bitmap getWholeListViewItemsToBitmap(ListView listview, Adapter adapter) {
        int itemscount = adapter.getCount();
        int allitemsheight = 0;
        List<Bitmap> bmps = new ArrayList<Bitmap>();

        for (int i = 0; i < itemscount; i++) {
            View childView = adapter.getView(i, null, listview);
            childView.measure(View.MeasureSpec.makeMeasureSpec(listview.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
            childView.setDrawingCacheEnabled(true);
            childView.buildDrawingCache();
            bmps.add(childView.getDrawingCache());
            allitemsheight += childView.getMeasuredHeight();
        }

        Bitmap bigbitmap = Bitmap.createBitmap(listview.getMeasuredWidth(), allitemsheight, Bitmap.Config.ARGB_8888);
        Canvas bigcanvas = new Canvas(bigbitmap);
        Paint paint = new Paint();
        int iHeight = 0;

        for (int i = 0; i < bmps.size(); i++) {
            Bitmap bmp = bmps.get(i);
            bigcanvas.drawBitmap(bmp, 0, iHeight, paint);
            iHeight += bmp.getHeight();

            bmp.recycle();
            bmp = null;
        }
        return bigbitmap;
    }

    public static Bitmap takeScreenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static File saveBitmap(Bitmap bm, String fileName) {
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static String shortenFacilityName(String facilityName) {

        String[] HOS = {"Hospital",
                "General Hospital",
                "district Hospital",
                "primary Hospital",
                "Military Hospital",
                "Army Hospital",
                "primery hospital",
                "Primery Hospital",
                "Military Hospitall",
                "Memorial Hospital",
                "Referal Hospital",
                "Gerenal Hospital",
                "H-ospital",
                "Priymeri Hospital",
                "premery hospital",
                "Primery Hospial",
                "Distric Hospital",
                "Referer Hospital",
                "Referral Hospital",
                "RIFERAL Hospital",
                "Referral Hospital",
                "PIRAYMERE Hospital",
                "Primary Hopital",
                "refferall hospiatal",
                "PRIYMERE Hospital",
                "Prameri Hospital",
                "Geneeral Hospital"};
        String[] ZHD = {"Zonal Health Department",
                "Zone Health Department",
                "Zone Health Dpt",
                "Zone health office",
                "Zonal Health office",
                "Zonal Health Departiment",
                "Zonal Health Departement",
                "Zone Health Depat",
                "Zonal Helth office",
                "Zone Health Departement",
                "Zone Health Deptment",
                "Zone Health Departemet",
                "Zonal Department",
                "Zone Health Depat"};
        String[] WoHO = {"Woreda Health office",
                "woreda Health office",
                "Wereda Health Office",
                "W/H/O",
                "Woreda H/Office",
                "Woreda H.Office",
                "woreda H/office",
                "W/H/Office",
                "Woreda Healthn Office",
                "Woreda heaqlth Office",
                "W/H/0",
                "wo/h/o",
                "w/h/o",
                "wored h/off",
                "woreda h/offices",
                "W/Health Office",
                "WHO",
                "Woreda Healt Office",
                "WoHOffice",
                "Wereda Health Office",
                "Woreda Heath Office",
                "woreda office",
                "Worda HO",
                "W/ H/Office",
                "Woreda H. Office",
                "WOREDA HAEALTH OFFICE",
                "Woreda Helath Office",
                "Woreda Health Offic",
                "Woreda Health Ofice",
                "Woreda Heaalth Office",
                "woreda Health offiice",
                "WoredaHealthOffice",
                "WOREDA HEALTH OFFICE",
                "Woreda Healthe Office",
                "Woreda Healtn Office",
                "Woreda Health O ffice",
                "Woreda Health Buearu",
                "Worda Healt Office",
                "Woreda Health Berau",
                "Wereda Helath Office",
                "Woreda Healeth Office",
                "Worda Healh Office",
                "Worda Health office",
                "Woerda Helath Office",
                "Woreda Heaalth Office",
                "Woreda bHO",
                "Woreda Health Offfice",
                "Woreda Health bureau"};
        String[] HC = {"Health Center",
                "H/center",
                "H/C",
                "h/c",
                "Health Centre",
                "Health Ceter",
                "Health Cener",
                "H.C",
                "helath center",
                "Helth Center",
                "Heath Center",
                "health cenetr",
                "h/ centre",
                "Healthc Center",
                "H./C",
                "Helath Center",
                ")H/center",
                "HealthCenter",
                "healh center",
                "healthe center",
                "Health Centr",
                "health Center+",
                "Halth Center",
                "Healt Center",
                "Heaslth Center",
                "Heathe Center",
                "Health Centeer",
                "Healthe Centre",
                "Health centedr",
                "Healthy Centre",
                "Heakth Centre",
                "Health Cemter",
                "Health Henter",
                "Heaalth Center",
                "Health center",
                "Heallth Center",
                "HCenter",
                "Healtth Center",
                "Heather Center",
                "HEALYH CENTER",
                "H/ C",
                "Health Centar",
                "health centet",
                "Health cente",
                "Halthe Center",
                "Health Ceneter"};
        String[] C = {"Clinic",
                "Higher Clinic",
                "Medium Clinic",
                "Clinc",
                "Midiam Clinic",
                "Middle clinic",
                "MEDIM CLINIC",
                "m,clinic",
                "Clininc",
                "M/Cilinic",
                "Primery clinic",
                "Midiam  Clinic",
                "primary Clinic",
                "clinik",
                "medium cliinic",
                "Medium Clinlic",
                "Midiuam Clinic",
                "Middium Clinic",
                "Midiem Clinic",
                "Mediem Clinic",
                "MIDIUM Clinic",
                "cilinic",
                "Cilinc",
                "Mendium clinic"};
        String[] DS = {"Drug Store",
                "Drug Shop",
                "D/S",
                "Druge Store",
                "Durg Stor",
                "Durg Store",
                "D/Store",
                "drug stor",
                "d/s",
                "Druig store",
                "Durag Store",
                "Drug Store_D",
                "Drug Stror",
                "Drag Store",
                "Drug Stotr",
                "Drugn Store",
                "Drug Srore",
                "Drug Sore",
                "Drug Dtore",
                "Druge Shope",
                "Druig store",
                "Drog store",
                "Duge Store",
                "drud store",
                "DStore"};
        String[] RDV = {"Rural Drug vender",
                "R.D.V",
                "Rural Drug Vendor",
                "Rural drugvender",
                "Dural Drug Vendor",
                "Drug Vender"};
        String[] Phar = {"Pharmacy",
                "Pharmac",
                "PHARAMC Y",
                "Farmacy",
                "pharmacyl",
                "pharmcy",
                "PHAMACY",
                "Parmacy",
                "Oharmacy"};
        String[] RHB = {"Regional Health Beuro",
                "R/H/B",
                "Regional Health Brue",
                "Health Bureau",
                "Regional Health Breaue",
                "Health Beuro",
                "Regional Health Bureau",
                "Regional Health Bereau"};
        String[] HO = {"h/offices",
                "H / office",
                "health offce",
                "Health Office",
                "H. Offices",
                "H/office",
                "HEALTH OFFICE",
                "Healthoffice",
                "Health bereo"
        };

        for (int i = 0; i < HOS.length; i++) {
            facilityName = facilityName.replace(HOS[i], "HOS");
        }
        for (int i = 0; i < ZHD.length; i++) {
            facilityName = facilityName.replace(ZHD[i], "ZHD");
        }
        for (int i = 0; i < WoHO.length; i++) {
            facilityName = facilityName.replace(WoHO[i], "WoHO");
        }
        for (int i = 0; i < HC.length; i++) {
            facilityName = facilityName.replace(HC[i], "HC");
        }
        for (int i = 0; i < C.length; i++) {
            facilityName = facilityName.replace(C[i], "C");
        }
        for (int i = 0; i < DS.length; i++) {
            facilityName = facilityName.replace(DS[i], "DS");
        }
        for (int i = 0; i < RDV.length; i++) {
            facilityName = facilityName.replace(RDV[i], "RDV");
        }
        for (int i = 0; i < Phar.length; i++) {
            facilityName = facilityName.replace(Phar[i], "Phar");
        }
        for (int i = 0; i < RHB.length; i++) {
            facilityName = facilityName.replace(RHB[i], "RHB");
        }
        for (int i = 0; i < HO.length; i++) {
            facilityName = facilityName.replace(HO[i], "HO");
        }

        return facilityName;
    }

    public static void hideKeyboard(@NonNull Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static String numberFormatter(int number){
        DecimalFormat number_formatter = new DecimalFormat("#,###,###");
        return number_formatter.format(number);
    }

    public static String dateFormatter(String date_string) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
        Date the_date;
        try {
            the_date = simpleDateFormat.parse(date_string.replace("T", " "));
            if (the_date != null)
                return new SimpleDateFormat("MM-dd-yyyy").format(the_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String dateFormatter(String date_string, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
        Date the_date;
        try {
            the_date = simpleDateFormat.parse(date_string.replace("T", " "));
            if (the_date != null)
                return new SimpleDateFormat(format).format(the_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getDateDifference(Date startDate, Date endDate){
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        return (int) elapsedDays;
    }
}
