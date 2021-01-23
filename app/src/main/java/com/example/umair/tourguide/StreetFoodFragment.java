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

import java.util.ArrayList;

public class StreetFoodFragment extends Fragment{
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
        final ArrayList<Location> streetFoodArray = new ArrayList<>();
        streetFoodArray.add(new Location(R.drawable.chicken_karahi,R.string.Chicken_Karahi_,R.string.butt_karahi_address,R.raw.chiecken_karahi));
        streetFoodArray.add(new Location(R.drawable.falooda,R.string.Falooda,R.string.Falooda_address,R.raw.baba_ji_kulfi_walay));
        streetFoodArray.add(new Location(R.drawable.halwa_puri,R.string.halwa_puri,R.string.halwa_puri_address,R.raw.taj_mahal));
        streetFoodArray.add(new Location(R.drawable.hareesa,R.string.Hareesa,R.string.hareesa_address,R.raw.hareesa));
        streetFoodArray.add(new Location(R.drawable.ice_cream,R.string.Ice_cream,R.string.ice_cream_address,R.raw.chaman_ice_cream));
        streetFoodArray.add(new Location(R.drawable.nihari,R.string.Nihari,R.string.nihari_address,R.raw.muhammdi_nihari));
        streetFoodArray.add(new Location(R.drawable.paye,R.string.paye,R.string.paye_address,R.raw.paye));
        streetFoodArray.add(new Location(R.drawable.samosay,R.string.Samosay,R.string.samosay_address,R.raw.samosay));
        streetFoodArray.add(new Location(R.drawable.shami_burger,R.string.Shami_burger,R.string.Shami_burger_address,R.raw.rangeela_burger));
        streetFoodArray.add(new Location(R.drawable.taka_tak,R.string.Taka_Tak,R.string.TakaTak_address,R.raw.taka_tak));


        View rootView = inflater.inflate(R.layout.activity_list, container, false);
        LocationAdapter locationAdapter = new LocationAdapter(getContext(), streetFoodArray);
        ListView listView = rootView.findViewById(R.id.list);
        listView.setAdapter(locationAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Location w = streetFoodArray.get(position);
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
