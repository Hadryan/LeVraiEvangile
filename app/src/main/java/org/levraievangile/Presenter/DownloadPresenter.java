package org.levraievangile.Presenter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import org.levraievangile.Model.DownloadFile;
import org.levraievangile.Model.LoadDownloadAudio;
import org.levraievangile.Model.LoadDownloadVideo;
import org.levraievangile.View.Interfaces.DownloadView;

import java.util.ArrayList;

/**
 * Created by Maranatha on 11/12/2017.
 */

public class DownloadPresenter implements DownloadView.ILoadDownload {
    // Ref interface
    private DownloadView.IDownload iDownload;
    private DownloadView.IPlaceholder iPlaceholder;
    // Ref LoadDownloadFile
    private LoadDownloadAudio loadDownloadAudio;
    private LoadDownloadVideo loadDownloadVideo;

    // Constructor
    public DownloadPresenter(DownloadView.IDownload iDownload) {
        this.iDownload = iDownload;
    }

    public DownloadPresenter(DownloadView.IPlaceholder iPlaceholder) {
        this.iPlaceholder = iPlaceholder;
    }

    // Load download data
    public void loadDownloadData(Context context){
        iDownload.initialize();
        iDownload.events();
    }

    public void loadPlaceHolderData(Context context, View rootView, int positionFrag){
        iPlaceholder.initialize(rootView);
        iPlaceholder.events();
        //--
        loadFragmentData(context, positionFrag);
    }

    // Refresh all data
    public void loadFragmentData(Context context, int positionFrag){

        // Load Audio files
        switch (positionFrag){
            // AUDIOS
            case 0:
                loadDownloadAudio = new LoadDownloadAudio();
                loadDownloadAudio.initializeData(context, this);
                loadDownloadAudio.execute();
                break;
            // VIDEOS
            case 1:
                loadDownloadVideo = new LoadDownloadVideo();
                loadDownloadVideo.initializeData(context, this);
                loadDownloadVideo.execute();
                break;
            // PDF
            case 2:
                break;

        }
    }

    @Override
    public void downloadAudioStarted() {

    }

    @Override
    public void downloadAudioFinished(ArrayList<DownloadFile> downloadFiles) {
        iPlaceholder.loadDownloadAudioData(downloadFiles, 1);
    }

    @Override
    public void downloadAudioFailure() {

    }

    @Override
    public void downloadVideoStarted() {

    }

    @Override
    public void downloadVideoFinished(ArrayList<DownloadFile> downloadFiles) {
        iPlaceholder.loadDownloadVideoData(downloadFiles, 1);
    }

    @Override
    public void downloadVideoFailure() {

    }
}
