package com.example.umair.tourguide;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

public class IconsFragment extends Fragment {
    private MediaPlayer mMediaPlayer;
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };
    private AudioManager mAudioManager;

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mAudioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Location> IconArray = new ArrayList<>();
        IconArray.add(new Location(R.drawable.amanullah_khan,R.string.Ammanullah_khan,R.string.Ammanullah_khan_bio,R.raw.ammanullah_khan));
        IconArray.add(new Location(R.drawable.nusrat_fateh_ali_khan,R.string.Nusrat_fateh_ali_khan,R.string.Nusrat_bio,R.raw.nusrat));
        IconArray.add(new Location(R.drawable.atif_aslam,R.string.Atif_aslam,R.string.Atif_aslam_bio,R.raw.atif_aslam));
        IconArray.add(new Location(R.drawable.ali_zafar,R.string.Ali_zafar,R.string.Ali_zafar_bio,R.raw.ali_zafar));
        IconArray.add(new Location(R.drawable.abrar,R.string.Abrarul_haq,R.string.abrarul_haq_bio,R.raw.abrarul_haq));
        IconArray.add(new Location(R.drawable.sajal_ali,R.string.Sajal_aly,R.string.Sajal_bio,R.raw.sajal_ali));
        IconArray.add(new Location(R.drawable.iman_ali,R.string.Iman_Ali,R.string.Iman_ali_bio,R.raw.iman_ali));
        IconArray.add(new Location(R.drawable.babar_azam,R.string.Babar_azam,R.string.Babar_azam_bio,R.raw.babar_azam));
        IconArray.add(new Location(R.drawable.ashfaq_ahmad,R.string.Ashfaq_ahmad,R.string.Ashfaq_ahmad_bio,R.raw.ashfaq_ahmad));
        IconArray.add(new Location(R.drawable.amrita_pritam,R.string.Amrita_pritam,R.string.Amrita_pritam_bio,R.raw.amrita));
        IconArray.add(new Location(R.drawable.faiz_ahmad,R.string.faiz_ahmad_faiz,R.string.faiz_bio,R.raw.faiz_ahmad));
        IconArray.add(new Location(R.drawable.habib_jalib,R.string.habib_jalib,R.string.habib_jalib_bio,R.raw.habib_jalib));
        IconArray.add(new Location(R.drawable.nawaz_sharif,R.string.Nawaz_sharif,R.string.nawaz_bio,R.raw.nawaz_sharif));
        IconArray.add(new Location(R.drawable.imran_khan,R.string.Imran_khan,R.string.imran_bio,R.raw.imran_khan));
        IconArray.add(new Location(R.drawable.mian_mansha,R.string.Mian_mansha,R.string.mansha_bio,R.raw.mansha));

        View rootView = inflater.inflate(R.layout.activity_list, container, false);
        LocationAdapter locationAdapter = new LocationAdapter(getContext(), IconArray);
        ListView listView = rootView.findViewById(R.id.list);
        listView.setAdapter(locationAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Location w = IconArray.get(position);
                releaseMediaPlayer();
                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback
                    mMediaPlayer = MediaPlayer.create(getContext(),w.getAudioResourceId());
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }

            }
        });
        return rootView;
    }
    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

//    Clean up the media player by releasing its resources.

    private void releaseMediaPlayer(){
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}
