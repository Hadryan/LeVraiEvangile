package org.levraievangile.Presenter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.levraievangile.Model.Annee;
import org.levraievangile.Model.Audio;
import org.levraievangile.Model.BonASavoir;
import org.levraievangile.Model.Mois;
import org.levraievangile.Model.Pdf;
import org.levraievangile.Model.Video;
import org.levraievangile.R;

import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by JESUS EST YAHWEH on 03/01/2017.
 */

public class CommonPresenter {

    public static final String KEY_ALL_NEWS_YEARS_LIST = "KEY_ALL_NEWS_YEARS_LIST";
    public static final String KEY_ALL_GOOD_TO_KNOW_LIST = "KEY_ALL_GOOD_TO_KNOW_LIST";
    public static final String KEY_SHORT_CODE = "KEY_SHORT_CODE";
    public static final String KEY_ALL_VIDEOS_LIST = "KEY_ALL_VIDEOS_LIST";
    public static final String KEY_ALL_AUDIOS_LIST = "KEY_ALL_AUDIOS_LIST";
    public static final String KEY_ALL_PDFS_LIST = "KEY_ALL_PDFS_LIST";
    public static final String KEY_ALL_NEWS_MONTH_LIST = "KEY_ALL_NEWS_MONTH_LIST";

    public static final String KEY_AUDIO_SELECTED = "KEY_AUDIO_SELECTED";

    public static final String GOOGLE_DRIVE_READER = "https://drive.google.com/viewer?url=";

    public static final String KEY_VIDEO_PLAYER_SEND_DATA = "KEY_VIDEO_PLAYER_SEND_DATA";
    public static final String KEY_VIDEO_PLAYER_RETURN_DATA = "KEY_VIDEO_PLAYER_RETURN_DATA";
    public static final String KEY_VALUE_POSITION_VIDEO_SELECTED = "KEY_VALUE_POSITION_VIDEO_SELECTED";
    public static final String KEY_VALUE_VIDEO_PLAY_NEXT = "KEY_VALUE_VIDEO_PLAY_NEXT";
    public static final String KEY_VALUE_VIDEO_PLAY_PREVIOUS = "KEY_VALUE_VIDEO_PLAY_PREVIOUS";
    public static final int VALUE_VIDEO_SELECTED_REQUEST_CODE = 11;

    public static final String KEY_NOTIF_AUDIO_TIME_ELAPSED = "KEY_NOTIF_AUDIO_TIME_ELAPSED";
    public static final String KEY_NOTIF_AUDIOS_LIST = "KEY_NOTIF_AUDIOS_LIST";
    public static final String KEY_NOTIF_PLAYER_SELECTED = "KEY_NOTIF_PLAYER_SELECTED";
    public static final String KEY_NOTIF_PLAYER_PLAY_NEXT = "KEY_NOTIF_PLAYER_PLAY_NEXT";
    public static final String KEY_NOTIF_PLAYER_PREVIOUS = "KEY_NOTIF_PLAYER_PREVIOUS";

    public static final int VALUE_PERMISSION_TO_SAVE_FILE = 103;

    private static final String FOLDER_NAME[] = {"LVE", "LVE/Audios", "LVE/Videos", "LVE/Pdfs"};

    // Attributes database
    private static SQLiteDatabase db;
    private static final String database = "questionsreponses.db";
    public static void buildDataBase(Context context){
        db = context.openOrCreateDatabase(database, context.MODE_PRIVATE, null);
    }
    public static SQLiteDatabase getDb() {
        return db;
    }

    public CommonPresenter(){}

    public static ArrayList<Video> listeSousMenuVideo(){
        ArrayList<Video> liste = new ArrayList<>();
        liste.add(new Video(R.mipmap.sm_enseignement,"Enseignements", "enseignements"));
        liste.add(new Video(R.mipmap.sm_question_reponse,"Questions/Réponses", "questions-reponses"));
        liste.add(new Video(R.mipmap.sm_temoignage,"Témoignages", "temoignages"));
        liste.add(new Video(R.mipmap.sm_reportage,"Reportages", "reportages"));
        liste.add(new Video(R.mipmap.sm_interview,"Interviews", "interviews"));
        liste.add(new Video(R.mipmap.sm_conversion,"Conversions", "conversions"));
        liste.add(new Video(R.mipmap.sm_confession,"Confessions", "confessions"));
        liste.add(new Video(R.mipmap.sm_jesus_guerit_toujours,"Jésus guérit toujours", "jesus-guerit-toujours"));
        liste.add(new Video(R.mipmap.sm_film,"Films", "films"));
        liste.add(new Video(R.mipmap.sm_dessin_anime,"Déssins animés", "dessins-animes"));
        return liste;
    }

    public static ArrayList<Audio> listeSousMenuAudio(){
        ArrayList<Audio> liste = new ArrayList<Audio>();
        liste.add(new Audio(R.mipmap.sm_enseignement,"Enseignements", "enseignements"));
        liste.add(new Audio(R.mipmap.sm_question_reponse,"Questions/Réponses", "questions-reponses"));
        liste.add(new Audio(R.mipmap.sm_temoignage,"Témoignages", "temoignages"));
        liste.add(new Audio(R.mipmap.sm_interview,"Interviews", "interviews"));
        return liste;
    }

    public static ArrayList<Pdf> listeSousMenuPdf(){
        ArrayList<Pdf> liste = new ArrayList<Pdf>();
        liste.add(new Pdf(R.mipmap.sm_pdf,"Livres", "livres"));
        liste.add(new Pdf(R.mipmap.sm_pdf,"Magazines", "magazines"));
        liste.add(new Pdf(R.mipmap.sm_pdf,"Bon à savoir", "bon-a-savoir"));
        liste.add(new Pdf(R.mipmap.sm_pdf,"Sainte Bible", "sainte-bible"));
        return liste;
    }

    /**
     * Display simple message text
     * @param context
     * @param title
     * @param message
     */
    public static void showMessage(final Context context, String title, String message, final boolean closeActivity){
        Hashtable<String, Integer> resolutionEcran = getScreenSize(context);
        int width = resolutionEcran.get("largeur");
        int height = resolutionEcran.get("hauteur");
        int imgWidth = width <= height ? width : height;
        int newWidth = (int)(imgWidth*0.75f);
        int newHeight = (int)(imgWidth*0.40f);

        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_message);
        dialog.getWindow().setLayout((int)(newWidth*1.30f), ActionBar.LayoutParams.WRAP_CONTENT);

        TextView titre = dialog.findViewById(R.id.title);
        TextView detaille = dialog.findViewById(R.id.msg_detail);

        titre.setText(title);

        buildTextViewToHtmlData(detaille, message);

        Button button_close = dialog.findViewById(R.id.button_close);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(closeActivity){
                    ((Activity)context).finish();
                }
            }
        });

        dialog.show();
    }

    /**
     * Build textview to Html
     * @param textView
     * @param textValue
     */
    public static void buildTextViewToHtmlData(TextView textView, String textValue){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(textValue, Html.FROM_HTML_MODE_LEGACY));
        }
        else{
            textView.setText(Html.fromHtml(textValue));
        }
    }

    /**
     * Get mipmap by typeShortcode
     * @param shortcode
     * @return
     */
    public static int getMipmapByTypeShortcode(String shortcode){
        int mipmap = R.mipmap.sm_film;
        switch (shortcode){
            case "enseignements":
                mipmap = R.mipmap.sm_enseignement;
                break;
            case "questions-reponses":
                mipmap = R.mipmap.sm_question_reponse;
                break;
            case "temoignages":
                mipmap = R.mipmap.sm_temoignage;
                break;
            case "reportages":
                mipmap = R.mipmap.sm_reportage;
                break;
            case "interviews":
                mipmap = R.mipmap.sm_interview;
                break;
            case "conversions":
                mipmap = R.mipmap.sm_conversion;
                break;
            case "confessions":
                mipmap = R.mipmap.sm_confession;
                break;
            case "jesus-guerit-toujours":
                mipmap = R.mipmap.sm_jesus_guerit_toujours;
                break;
            case "films":
                mipmap = R.mipmap.sm_film;
                break;
            case "dessins-animes":
                mipmap = R.mipmap.sm_dessin_anime;
                break;
            case "livres":
                mipmap = R.mipmap.sm_pdf;
                break;
            case "magazines":
                mipmap = R.mipmap.sm_pdf;
                break;
            case "bon-a-savoir":
                mipmap = R.mipmap.sm_pdf;
                break;
            case "sainte-bible":
                mipmap = R.mipmap.sm_pdf;
                break;
        }
        return mipmap;
    }



    public static String getLibelleByTypeShortcode(String shortcode){
        String libelle = "Enseignements";
        switch (shortcode){
            case "enseignements":
                libelle = "Enseignements";
                break;
            case "questions-reponses":
                libelle = "Questions/Réponses";
                break;
            case "temoignages":
                libelle = "Témoignages";
                break;
            case "reportages":
                libelle = "Reportages";
                break;
            case "interviews":
                libelle = "Interviews";
                break;
            case "conversions":
                libelle = "Conversions";
                break;
            case "confessions":
                libelle = "Confessions";
                break;
            case "jesus-guerit-toujours":
                libelle = "Jésus guérit toujours";
                break;
            case "films":
                libelle = "Films";
                break;
            case "dessins-animes":
                libelle = "Dessins animés";
                break;
            case "livres":
                libelle = "Livres";
                break;
            case "magazines":
                libelle = "Magazines";
                break;
            case "bon-a-savoir":
                libelle = "Bon à savoir";
                break;
            case "sainte-bible":
                libelle = "Sainte bible";
                break;
        }
        return libelle;
    }


    /**
     * Get string date : Day/Month/Year
     * @param date
     * @return
     */
    public static String getStringDate(String date){
        String tabMonth[] = {"JAN", "FÉV", "MAR", "AVR", "MAI", "JUN", "JUL", "AOÜ", "SEP", "OCT", "NOV", "DÉC"};
        return date.split("-")[2].trim()+" "+tabMonth[Integer.parseInt(date.split("-")[1].trim())-1]+" "+date.split("-")[0].trim();
    }

    /**
     * Change format date : Year - Month - Day
     * @param date
     * @return
     */
    public static String changeFormatDate(String date){
        return date.split("-")[2].trim()+"/"+date.split("-")[1].trim()+"/"+date.split("-")[0].trim();
    }

    public static String changeFormatDuration(String duration){
        String tagDuree[] = duration.split(":");
        return ((Integer.parseInt(tagDuree[0])*60)+Integer.parseInt(tagDuree[1]))+"min";
    }

    /**
     * Get screen size
     * @param context
     * @return
     */
    public static Hashtable<String, Integer> getScreenSize(Context context) {
        Hashtable<String, Integer> dimension = new Hashtable<>();
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        dimension.put("largeur", width);
        dimension.put("hauteur", height);
        return dimension;
    }


    /**
     * Verify if connection exists
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context){
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.getType() == networkType) return true;
            }
        }
        catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Verify if wifi connection exits
     * @param context
     * @return
     */
    public static boolean isMobileWIFIConnected(Context context){
        int networkType = ConnectivityManager.TYPE_WIFI;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.getType() == networkType) return true;
        }
        catch (Exception e) {
            return false;
        }
        return false;
    }


    /**
     * Download file
     * @param context
     * @param myHttpUrl
     * @param nameOfFile
     * @param descriptionOfFile
     * @param typeOfFile
     */
    public static void getFileByDownloadManager(Context context, String myHttpUrl, String nameOfFile, String descriptionOfFile, String typeOfFile){

        File SDCardRoot = Environment.getExternalStorageDirectory();
        try {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(myHttpUrl));
            request.setTitle(nameOfFile);
            request.setDescription(descriptionOfFile.equalsIgnoreCase("") == false ? descriptionOfFile : "");
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(false);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            // Create folder
            createFolder();
            // Set destination of file
            String destination = (typeOfFile.equalsIgnoreCase("audio") ? FOLDER_NAME[1] : typeOfFile.equalsIgnoreCase("video") ? FOLDER_NAME[2] : FOLDER_NAME[3]);
            request.setDestinationInExternalPublicDir(SDCardRoot + "/" + destination, nameOfFile);
            // Download
            DownloadManager manager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
        }
        catch (Exception ex){}
    }

    public static boolean isStorageDownloadFileAccepted(Context context){
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return (permissionCheck == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Share application data
     * @param context
     */
    public static void shareApplication(Context context)
    {
        String message = "Bonjour,\nJe tiens à te faire découvrir l'application android Le Vrai Evangile.\nClique sur le lien qui apparaît en bas de ce message, et n'hésite pas à en parler autour de toi.\nGloire à Jésus-Christ.\n\n"+context.getResources().getString(R.string.url_play_store);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "Partager l'application"));
    }

    /**
     * Share audio data
     * @param context
     * @param audio
     */
    public static void shareAudio(Context context, Audio audio)
    {
        String id = ""+audio.getId();
        String title = audio.getTitre();
        String url = context.getResources().getString(R.string.url_partager_audio);
        String description = "Bonjour,\nJe t'invite à écouter cet audio.\n\nTITRE: "+title+"\n LIEN: "+url.replace("{TYPE}", audio.getType_shortcode()).replace("{ID}", ""+id);
        sendShareResource(context, description, title);
    }

    /**
     * Share video data
     * @param context
     * @param video
     */
    public static void shareVideo(Context context, Video video)
    {
        String id = ""+video.getId();
        String title = video.getTitre();
        String url = context.getResources().getString(R.string.url_partager_video);
        String description = "Bonjour,\nJe t'invite à visionner cette vidéo.\n\nTITRE: "+title+"\n LIEN: "+url.replace("{TYPE}", video.getType_shortcode()).replace("{ID}", ""+id);
        sendShareResource(context, description, title);
    }

    /**
     * Send Share data
     * @param context
     * @param description
     * @param title
     */
    private static void sendShareResource(Context context, String description, String title){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, description);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, title));
    }


    public static int getNumberToDisplay(Context context){
        int number = 1;
        Hashtable<String, Integer> screenSize = getScreenSize(context);
        boolean isPortrait = screenSize.get("largeur") <= screenSize.get("hauteur");
        if(isTablette(context)){
            number = isPortrait ? 3 : 5;
        }
        else{
          number = isPortrait ? 2 : 4;
        }
        return number;
    }

    /**
     * Verify if it's a tablet
     * @param context
     * @return
     */
    public static boolean isTablette(Context context)
    {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    /**
     * Save data in share preferences
     * @param context
     * @param key
     * @param contentData
     */
    public static void saveDataInSharePreferences(Context context, String key, String contentData){
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, contentData);
            editor.commit();
        }
        catch (Exception ex){}
    }


    /**
     * Retrieve data in share preferences
     * @param context
     * @param key
     * @return
     */
    public static String getDataFromSharePreferences(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }


    /**
     * Replace accents by correspondant a letter
     * @param str
     * @return
     */
    public static String removeAccents(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    /**
     * Get all save years
     * @param context
     * @return
     */
    public static ArrayList<Annee> getAllSaveYears(Context context){
        ArrayList<Annee> mList = new ArrayList<>();
        String srcFichier = getDataFromSharePreferences(context, KEY_ALL_NEWS_YEARS_LIST);
        try {
            JSONArray jsonArray = new JSONArray(srcFichier);

            //--
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String annee = jsonObject.getString("annee");
                //--
                mList.add(new Annee(R.mipmap.sm_calendrier, annee));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return mList;
    }

    /**
     * Get all save good to know
     * @param context
     * @return
     */
    public static ArrayList<BonASavoir> getAllSaveGoodToKnow(Context context){
        ArrayList<BonASavoir> mList = new ArrayList<>();
        String srcFichier = getDataFromSharePreferences(context, KEY_ALL_GOOD_TO_KNOW_LIST);
        try {
            JSONArray jsonArray = new JSONArray(srcFichier);
            //--
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String titre = jsonObject.getString("titre");
                boolean etat = jsonObject.getBoolean("etat");
                int nbvue = jsonObject.getInt("nbvue");
                String type_media = jsonObject.getString("type_media");
                String date = jsonObject.getString("date");
                int mipmap = R.mipmap.sm_infos;
                //--
                mList.add(new BonASavoir(id, titre, nbvue, type_media, etat, date, mipmap));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return mList;
    }

    /**
     * Get all save videos saved
     * @param context
     * @return
     */
    public static ArrayList<Video> getAllVideoSavedBy(Context context, String shortCode){
        ArrayList<Video> mList = new ArrayList<>();
        String keyShortCode = KEY_ALL_VIDEOS_LIST+"-"+shortCode;
        String srcFichier = getDataFromSharePreferences(context, keyShortCode);
        try {
            JSONArray jsonArray = new JSONArray(srcFichier);
            //--
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String titre = jsonObject.getString("titre");
                String duree = jsonObject.getString("duree");
                String urlacces = jsonObject.getString("urlacces");
                String src = jsonObject.getString("src");
                String auteur = jsonObject.getString("auteur");
                String type_libelle = jsonObject.getString("type_libelle");
                String type_shortcode = jsonObject.getString("type_shortcode");
                String date = jsonObject.getString("date");
                int mipmap = getMipmapByTypeShortcode(shortCode);
                mList.add(new Video(id, urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode, mipmap));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return mList;
    }


    /**
     * Get all save audios saved
     * @param context
     * @return
     */
    public static Audio getAudioSelected(Context context){
        String srcFichier = getDataFromSharePreferences(context, KEY_AUDIO_SELECTED);
        Audio audio = null;
        try {
            JSONObject jsonObject = new JSONObject(srcFichier);
            int id = jsonObject.getInt("id");
            String titre = jsonObject.getString("titre");
            String duree = jsonObject.getString("duree");
            String urlacces = jsonObject.getString("urlacces");
            String src = jsonObject.getString("src");
            String auteur = jsonObject.getString("auteur");
            String type_libelle = jsonObject.getString("type_libelle");
            String type_shortcode = jsonObject.getString("type_shortcode");
            String date = jsonObject.getString("date");
            int mipmap = getMipmapByTypeShortcode(type_shortcode);
            audio = new Audio(id, urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode, mipmap);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return audio;
    }

    /**
     * Get all save audios saved
     * @param context
     * @return
     */
    public static ArrayList<Audio> getAllAudioSavedBy(Context context, String shortCode){
        ArrayList<Audio> mList = new ArrayList<>();
        String keyShortCode = KEY_ALL_AUDIOS_LIST+"-"+shortCode;
        String srcFichier = getDataFromSharePreferences(context, keyShortCode);
        try {
            JSONArray jsonArray = new JSONArray(srcFichier);
            //--
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String titre = jsonObject.getString("titre");
                String duree = jsonObject.getString("duree");
                String urlacces = jsonObject.getString("urlacces");
                String src = jsonObject.getString("src");
                String auteur = jsonObject.getString("auteur");
                String type_libelle = jsonObject.getString("type_libelle");
                String type_shortcode = jsonObject.getString("type_shortcode");
                String date = jsonObject.getString("date");
                int mipmap = getMipmapByTypeShortcode(shortCode);
                mList.add(new Audio(id, urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode, mipmap));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return mList;
    }

    /**
     * Get all notification audios list saved
     * @param context
     * @return
     */
    public static ArrayList<Audio> getAllNotificationAudios(Context context){
        ArrayList<Audio> mList = new ArrayList<>();
        String srcFichier = getDataFromSharePreferences(context, KEY_NOTIF_AUDIOS_LIST);
        try {
            JSONArray jsonArray = new JSONArray(srcFichier);
            //--
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String titre = jsonObject.getString("titre");
                String duree = jsonObject.getString("duree");
                String urlacces = jsonObject.getString("urlacces");
                String src = jsonObject.getString("src");
                String auteur = jsonObject.getString("auteur");
                String type_libelle = jsonObject.getString("type_libelle");
                String type_shortcode = jsonObject.getString("type_shortcode");
                String date = jsonObject.getString("date");
                int mipmap = getMipmapByTypeShortcode(type_shortcode);
                mList.add(new Audio(id, urlacces, src, titre, auteur, duree, date, type_libelle, type_shortcode, mipmap));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return mList;
    }

    /**
     * Get all save audios saved
     * @param context
     * @return
     */
    public static ArrayList<Pdf> getAllPdfSavedBy(Context context, String shortCode){
        ArrayList<Pdf> mList = new ArrayList<>();
        String keyShortCode = KEY_ALL_PDFS_LIST+"-"+shortCode;
        String srcFichier = getDataFromSharePreferences(context, keyShortCode);
        try {
            JSONArray jsonArray = new JSONArray(srcFichier);
            //--
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String titre = jsonObject.getString("titre");
                String urlacces = jsonObject.getString("urlacces");
                String src = jsonObject.getString("src");
                String auteur = jsonObject.getString("auteur");
                String type_libelle = jsonObject.getString("type_libelle");
                String type_shortcode = jsonObject.getString("type_shortcode");
                String date = jsonObject.getString("date");
                int mipmap = getMipmapByTypeShortcode(shortCode);
                mList.add(new Pdf(id, urlacces, src, titre, auteur, date, type_libelle, type_shortcode, mipmap));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return mList;
    }

    /**
     * Get all month news saved
     * @param context
     * @return
     */
    public static ArrayList<Mois> getAllNewsMonthSavedBy(Context context, String yearValue){
        ArrayList<Mois> mList = new ArrayList<>();
        String keyShortCode = KEY_ALL_NEWS_MONTH_LIST+"-"+yearValue;
        String srcFichier = getDataFromSharePreferences(context, keyShortCode);
        try {
            JSONArray jsonArray = new JSONArray(srcFichier);
            //--
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int chiffre = jsonObject.getInt("chiffre");
                String lettre = jsonObject.getString("lettre");
                mList.add(new Mois(chiffre, lettre));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return mList;
    }

    /**
     * Get Notification Previous Resource value
     * @param currentValue
     * @param totalResource
     * @return
     */
    public static int getNotifPlayerPreviousValue(int currentValue, int totalResource){
        int position = currentValue > 0 ? currentValue-1 : totalResource-1;
        return position;
    }

    /**
     * Get Notification Next Ressource value
     * @param currentValue
     * @param totalRessouce
     * @return
     */
    public static int getNotifPlayerNextValue(int currentValue, int totalRessouce){
        int position = currentValue < totalRessouce-1 ? currentValue+1 : 0;
        return position;
    }

    /**
     * Get Previous Ressource value
     * @param currentValue
     * @return
     */
    public static int getPreviousRessourceValue(int currentValue){
        int position = currentValue > 0 ? currentValue-1 : currentValue;
        return position;
    }

    /**
     * Get Scroll Previous Ressource value
     * @param currentValue
     * @param totalRessouce
     * @return
     */
    public static int getScrollToPreviousValue(int currentValue, int totalRessouce){
        return currentValue+2 > totalRessouce-1 ? currentValue+2 : currentValue;
    }

    /**
     * Get Next Ressource value
     * @param currentValue
     * @param totalRessouce
     * @return
     */
    public static int getNextRessourceValue(int currentValue, int totalRessouce){
        int position = currentValue < totalRessouce-1 ? currentValue+1 : totalRessouce-1;
        return position;
    }

    /**
     * Get Scroll Next Ressource value
     * @param currentValue
     * @param totalRessouce
     * @return
     */
    public static int getScrollToNextValue(int currentValue, int totalRessouce){
        return currentValue < totalRessouce-2 ? currentValue+2 : currentValue;
    }

    /**
     * Canceled CountDownTimer
     * @param countDownTimer
     */
    public static void cancelCountDownTimer(CountDownTimer countDownTimer){
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    /**
     * Stop videoview
     * @param videoView
     */
    public static void stopVideoViewPlayer(VideoView videoView){
        if(videoView != null && videoView.isPlaying()){
            videoView.stopPlayback();
        }
    }



    /**
     * Create folder
     */
    public static void createFolder(){
        for(int i=0; i<FOLDER_NAME.length; i++){
            try {
                File root = new File(Environment.getExternalStorageDirectory(), FOLDER_NAME[i]);
                if (!root.exists()) {
                    root.mkdirs();
                }
            }
            catch (Exception e){
                Log.i("TAG_FOLDER_ERROR", e.getMessage());
            }
        }
    }

    private static String getAudioPath(){
        File SDCardRoot = Environment.getExternalStorageDirectory();
        return Environment.getExternalStoragePublicDirectory(SDCardRoot + "/" + FOLDER_NAME[1] + "/").getAbsolutePath();
    }

    private static String getVideoPath(){
        File SDCardRoot = Environment.getExternalStorageDirectory();
        return Environment.getExternalStoragePublicDirectory(SDCardRoot + "/" + FOLDER_NAME[2] + "/").getAbsolutePath();
    }

    private static String getPdfPath(){
        File SDCardRoot = Environment.getExternalStorageDirectory();
        return Environment.getExternalStoragePublicDirectory(SDCardRoot + "/" + FOLDER_NAME[3] + "/").getAbsolutePath();
    }

    /**
     * Get app volume
     * @param context
     */
    public static void getApplicationVolume(Context context){
        AudioManager audio = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
    }

    /**
     * Stop Media player
     * @param mediaPlayer
     */
    public static void stopMediaPlayer(MediaPlayer mediaPlayer){
        if(mediaPlayer!=null) {
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.reset();// It requires again setDataSource for player object.
                mediaPlayer.stop();// Stop it
                mediaPlayer.release();// Release it
                mediaPlayer = null; // Initialize it to null so it can be used later
            }
        }
    }

    public static void saveNotificationParameters(Context context, int positionSelected, int totalAudios){
        CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_PLAYER_SELECTED, ""+positionSelected);
        int previousPosition = CommonPresenter.getNotifPlayerPreviousValue(positionSelected, totalAudios);
        CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_PLAYER_PREVIOUS, ""+previousPosition);
        int nextPosition = CommonPresenter.getNotifPlayerNextValue(positionSelected, totalAudios);
        CommonPresenter.saveDataInSharePreferences(context, KEY_NOTIF_PLAYER_PLAY_NEXT, ""+nextPosition);
    }
}
