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

public class HistoryFragment extends Fragment {
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
        final ArrayList<Location> historyArray = new ArrayList<>();
        historyArray.add(new Location(R.drawable.badshahi_mosque,R.string.Badshahi_Mosque,R.string.Badshahi_Mosque_Address,R.raw.badshahi_masjid));
        historyArray.add(new Location(R.drawable.chauburji,R.string.Chauburji,R.string.Chaburji_address,R.raw.chauburji));
        historyArray.add(new Location(R.drawable.haveli_nau_nihal_singh,R.string.Haveli_Nau_Nihal_singh,R.string.haveli_Nau_Nihal_singh_address,R.raw.haveli_nau_nihal));
        historyArray.add(new Location(R.drawable.jahangir_tomb,R.string.Jahangir_tomb,R.string.Jahangir_tomb_address,R.raw.jahangir_tomb));
        historyArray.add(new Location(R.drawable.lahore_fort,R.string.Lahore_Fort,R.string.Lahore_fort_address,R.raw.lahore_fort));
        historyArray.add(new Location(R.drawable.lohari_gate,R.string.Lohari_gate,R.string.Lohari_gate_address,R.raw.lahori_gate));
        historyArray.add(new Location(R.drawable.moti_masjid,R.string.Moti_Masjid,R.string.Moti_Masjid_address,R.raw.moti_masjid));
        historyArray.add(new Location(R.drawable.sheesh_mahal,R.string.Sheesh_mahal,R.string.Sheesh_mahal_address,R.raw.sheesh_mahal));
        historyArray.add(new Location(R.drawable.tomb_of_allama_iqbal,R.string.Tomb_Allama_iqbal,R.string.Tomb_iqbal_address,R.raw.iqbal));
        historyArray.add(new Location(R.drawable.tomb_of_anarkali,R.string.Tomb_anarkali,R.string.Tomb_anarkali_address,R.raw.anarkali));
        historyArray.add(new Location(R.drawable.tomb_of_nur_jahan,R.string.Tomb_Nur_jahan,R.string.Tomb_Nur_jahan_address,R.raw.nur_jahan));

        View rootView = inflater.inflate(R.layout.activity_list, container, false);
        LocationAdapter locationAdapter = new LocationAdapter(getContext(), historyArray);
        ListView listView = rootView.findViewById(R.id.list);
        listView.setAdapter(locationAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Location w = historyArray.get(position);
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
