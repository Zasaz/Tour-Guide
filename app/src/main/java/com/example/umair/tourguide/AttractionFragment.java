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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class AttractionFragment extends Fragment {
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
        final ArrayList<Location> attractionArray = new ArrayList<>();
        attractionArray.add(new Location(R.drawable.lahore_museum,R.string.Lahore_museum,R.string.Lahore_museum_address,R.raw.lahore_museum));
        attractionArray.add(new Location(R.drawable.lahore_fort,R.string.Lahore_Fort,R.string.Lahore_fort_address,R.raw.lahore_fort));
        attractionArray.add(new Location(R.drawable.lahore_zoo,R.string.Lahore_zoo,R.string.Lahore_zoo_address,R.raw.lahore_zoo));
        attractionArray.add(new Location(R.drawable.data_darbar,R.string.Data_darbar,R.string.Data_darbar_address,R.raw.data_darbar));
        attractionArray.add(new Location(R.drawable.eiffel_tower_replica,R.string.Eiffel_tower_replica,R.string.Eiffel_tower_address,R.raw.eiffel_tower));
        attractionArray.add(new Location(R.drawable.emporium_mall,R.string.Emporium_mall,R.string.Emporium_address,R.raw.emporium));
        attractionArray.add(new Location(R.drawable.grand_mosque,R.string.Grand_mosque,R.string.Grand_masjid_address,R.raw.grand_jamia_masjid));
        attractionArray.add(new Location(R.drawable.minar_e_pakistan,R.string.Minar_e_Pakistan,R.string.Minar_e_Pakistan_address,R.raw.minare_pakistan));
        attractionArray.add(new Location(R.drawable.shalimar_bagh,R.string.Shalimar_Bagh,R.string.Shalimar_bagh_address,R.raw.shalimar_bagh));
        attractionArray.add(new Location(R.drawable.wazir_khan_mosque,R.string.Wazir_khan,R.string.wazir_khan_address,R.raw.wazir_khan));

        View rootView = inflater.inflate(R.layout.activity_list, container, false);
        LocationAdapter locationAdapter = new LocationAdapter(getContext(), attractionArray);
        ListView listView = rootView.findViewById(R.id.list);
        listView.setAdapter(locationAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Location w = attractionArray.get(position);
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

//     Clean up the media player by releasing its resources.

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
