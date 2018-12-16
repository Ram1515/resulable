package com.courier.service.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.courier.service.R;
import com.courier.service.ui.authenticate.AppController;
import com.courier.service.ui.interfaces.YesNoDialogConfirmation;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class U {
    private static U mInstance;
    private static ProgressDialog mProgress;
    private Dialog mDialog;
    public static U getInstance() {
        if (mInstance == null) {
            mInstance = new U();
        }
        return mInstance;
    }

    public static void toast(Context mContext, String message) {
        Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        //  toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }




    public static float dpToPx(float _Dp) {
        return _Dp * Resources.getSystem().getDisplayMetrics().density;
    }

    public static boolean isConnectedToInternet(Context mContext) {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    public static boolean isValidWord(String w) {
        return w.matches("[A-Za-z][^.]*");
    }
    public static boolean isValidMobile(String phone)
    {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }


    public static void showDialog(final Context context, String msg) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dilog_for_messages);
        dialog.setCancelable(false);
        dialog.show();
        TextView ok = (TextView) dialog.findViewById(R.id.ok_alert_message);
        TextView alertText = (TextView) dialog.findViewById(R.id.alert_message_text);
        alertText.setText(msg);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }
    public static void showOkDialog(final Context context, String msg, final String type, final YesNoDialogConfirmation mListener) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dilog_for_messages);
        dialog.setCancelable(false);
        dialog.show();

        TextView ok = (TextView) dialog.findViewById(R.id.ok_alert_message);
        TextView alertText = (TextView) dialog.findViewById(R.id.alert_message_text);
        alertText.setText(msg);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListener!=null)
                    mListener.yesClicked(0,type);
                dialog.cancel();
            }
        });
    }

    public static boolean checkForEncode(String string) {
        String pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(string);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }

    public static String encodeData(String message) {

        byte[] data = new byte[0];
        try {
            data = message.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        return base64;

    }
    public static String decodemessage(String message) {


        String text = null;
        try {
            byte[] data = Base64.decode(message, Base64.DEFAULT);
            text = new String(data, "UTF-8");
            return text;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return message;
    }

//    public static void openKeyboard(Context mContext)
//    {
//
//        InputMethodManager inputMethodManager =
//                (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.toggleSoftInputFromWindow(linearLayout.getApplicationWindowToken(),
//                InputMethodManager.SHOW_FORCED, 0);
//    }

    public static String DateTimetoDate (String format)
    {

        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;
            date = df.parse(format);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String mydate = sdf.format(date);

            return mydate;
        } catch (ParseException e) {
            // To change body of catch statement use File | Settings | File Templates.
            e.printStackTrace();
        }

        return format;
    }


    public static String normalDatetoUtc(String date, String outputFormat) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.INPUT_UTC, Locale.ENGLISH);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date myDate = simpleDateFormat.parse(date);
            SimpleDateFormat converted = new SimpleDateFormat(outputFormat,Locale.ENGLISH);
            converted.setTimeZone(TimeZone.getDefault());
            String utcTime = converted.format(myDate);
            Log.e("utc",""+utcTime);
            return utcTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    //utc to normal
    public static String getUtcTime(String time,String inputFormat, String outputFormat) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(inputFormat, Locale.ENGLISH);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date myDate = simpleDateFormat.parse(time);
            SimpleDateFormat converted = new SimpleDateFormat(outputFormat,Locale.ENGLISH);
            converted.setTimeZone(TimeZone.getDefault());
            String utcTime = converted.format(myDate);
            Log.e("checkutctime",""+utcTime);
            return utcTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    //normal to utc
    public static String utcDateTime(String date, String format) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            Date myDate = simpleDateFormat.parse(date);
            SimpleDateFormat converted = new SimpleDateFormat(format);
            converted.setTimeZone(TimeZone.getTimeZone("UTC"));
            String utcDate = converted.format(myDate);
            return utcDate.toUpperCase();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int width(float w) {
        float s = (float) w / 100;
        return (int) (AppController.getInstance().getScreenWidth() * s);
    }

    //mmmmmm
    public static void yesNoDialog(final Context mContext, final YesNoDialogConfirmation mListener, String msg, final String type) {
        final Dialog mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.dialog_yes_no);
        mDialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = AppController.getInstance().getScreenWidth() - U.width(5);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        mDialog.getWindow().setAttributes(lp);
        TextView txtMessage = (TextView) mDialog.findViewById(R.id.txt_message);
        txtMessage.setText(msg);
        Button btnYes = (Button) mDialog.findViewById(R.id.btn_yes);
        Button btnNo = (Button) mDialog.findViewById(R.id.btn_no);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                if (mListener != null)
                    mListener.yesClicked(0, type);
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    // for hiding  keyboard method
    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }




    public static int screenWidth(Activity mActivity) {
        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static void showGPSDisabledAlertToUser(final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                               // HomeFragment.gprsflag =true;
                      Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(callGPSSettingIntent);

                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                       // HomeFragment.gprsflag =false;
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }

    public static String setDate(String subscriptionCreatedDate) {

        SimpleDateFormat sdf = new SimpleDateFormat(Constants.CAL_DATE_FORMAT);
        Date dt1 = null;

        try {
            dt1 = sdf.parse(subscriptionCreatedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String day = (String) android.text.format.DateFormat.format("dd", dt1);
        String year = (String) android.text.format.DateFormat.format("yyyy", dt1);
        String month = (String) android.text.format.DateFormat.format("MMM", dt1);
        String completeDate = month + " " + day + " " + year;
        Log.e("Completedtae", "" + completeDate);

        return completeDate;
    }

    public static boolean isGPSEnabled (Context mContext){
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static String changeDateFormat(String changeDate) {

        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "MM-dd-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        java.util.Date date = null;
        String str ="";
        try {
            date = inputFormat.parse(changeDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String saveImageToExternalStorage(Bitmap bitmap) {

        // Calendar now = Calendar.getInstance();
        String ImageURi = null;
        String imageFileName = "image"+ System.currentTimeMillis()+ ".jpg";
        //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
        try {
            File dir = new File(Environment.getExternalStorageDirectory(), "CroppedImages");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //  String mPath = sdf.format(now.getTime()) + ".jpg";
            File imageFile = new File(dir, imageFileName);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            ImageURi = imageFile.getAbsolutePath();
            Log.e("File Path: ", " " + imageFile.getAbsolutePath());
            Log.e("ImageChecking","progreesss"+"done");
            // Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("ImageChecking","Some error occured"+"Some error occured");
            // Toast.makeText(MainActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();
        }
        return ImageURi;
    }

    //progress dialog
    public void showProgress(Context mContext, boolean trans) {
        try {
            if (mProgress == null) {
                mProgress = new ProgressDialog(mContext);
                mProgress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            mProgress.show();
            if (trans) {
                mProgress.setContentView(R.layout.blank_layout);
                mProgress.getWindow().setDimAmount(0.0f);
            } else
                mProgress.setContentView(R.layout.dialog_progress);
            mProgress.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
            mProgress = null;
        }
    }

    public static void hideProgress() {
        try {
            if (mProgress != null) {
                mProgress.hide();
                mProgress.dismiss();
                mProgress = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mProgress = null;
        }
    }
    public static String changeTimeFormat(String deapartureTime)
    {
        String changetime="";
        SimpleDateFormat converted = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat sf = new SimpleDateFormat("hh:mm:ss");
        try {
            Date dt = sf.parse(deapartureTime);
            changetime = converted.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return changetime;
    }
}
