package com.example.dell.mzz;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    Uri u;
    Song song;
    Song selectedFromList;
    Bitmap bitmap=null;
    static MediaPlayer mp = new MediaPlayer();
    ListView list1;
    ListView list2;
 static int current;
    int temp=0;
    int temp1=0;
    int count=0;
    Main2Activity ma;
    LinearLayout lLayout;
    ImageButton playbutton;
    ImageButton pausebutton;
    ImageButton forwardbutton;
    ImageButton rewindbutton;
    ImageView image;
    ImageView image3;
    ImageButton likeButton;
    ImageButton play;
    TextView title;
    TextView songname;
    ImageButton dislikeButton;
    Cursor cs;
    static MainActivity mainActivity;
    static ArrayList<Song> songList;
    public ListView songView;
    public Intent playIntent;
    private boolean musicBound = false;
    AudioManager sAudioManager;


   AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // Permanent loss of audio focus
                // Pause playback immediately
                pauseMusic();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                // Pause playback
                pauseMusic();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                sAudioManager.adjustVolume(AudioManager.ADJUST_LOWER, 1);
                temp = 2;
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // Your app has been granted audio focus again
                // Raise volume to normal, restart playback if necessary
                if (temp == 1) {
                    startMusic();
                    temp = 0;
                } else if (temp == 2) {
                    sAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, 1);
                    temp = 0;
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        sAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mainActivity=this;
        songView = (ListView) findViewById(R.id.listview1);
        lLayout = (LinearLayout) findViewById(R.id.linear);
        image=(ImageView)findViewById(R.id.bitimage1);
       image3=(ImageView)findViewById(R.id.imageview);
        title=(TextView)findViewById(R.id.textView1);
        songname=(TextView)findViewById(R.id.textView2);
        play=(ImageButton)findViewById(R.id.pause);
       // lLayout.setAlpha((float) 0.4);
        playbutton = (ImageButton) findViewById(R.id.pause);

       // forwardbutton = (ImageButton) findViewById(R.id.forwardbutton);
        //rewindbutton = (ImageButton) findViewById(R.id.rewindbutton);
        //likeButton = (ImageButton) findViewById(R.id.like_button);
        //dislikeButton = (ImageButton) findViewById(R.id.dislike_button);
        songList = new ArrayList<Song>();

        getSongslist();
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
        final SongAdapter songAdt = new SongAdapter(this, songList);
        songView.setAdapter(songAdt);
        count=songAdt.getCount();
        songView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedFromList = songList.get(position);
                current=position;
                playsong(current);
                Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(i);
                // startActivity(new Intent(getApplicationContext(),Main2Activity.class).putExtra("pos",position));

            }
        });

        lLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(i);
            }
        });


        playbutton.setOnClickListener(this);
     //   mp.setOnCompletionListener(this);

       // likeButton.setOnClickListener(this);
        //dislikeButton.setOnClickListener(this);

    }
  //  public static MainActivity getInstance()
  //  {
//
      //  return mainActivity;
    //}
    //list1=(ListView)findViewById(R.id.listview1);
    //playlist=new ArrayList<String>();
    // String[] proj = { MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME };
    //Cursor audioCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, null, null, null);



    public void playsong(int current)
    {
         mp.release();
        Song currsong = songList.get(current);
        int result = sAudioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            long thisId = currsong.getID();
            u = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, thisId);
            // }
            mp = new MediaPlayer();
             mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            image.setImageBitmap(currsong.getBitmap());
            // image2.setImageBitmap(currsong.getBitmap());
            title.setText(currsong.getTitle());
            songname.setText(currsong.getArtist());
         //   mp = MediaPlayer.create(getApplicationContext(), u);
            try {
                mp.setDataSource(getApplicationContext(), u);
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.start();

            // Main2Activity.seekbar.setProgress(0);
           // Main2Activity.seekbar.setMax(mp.getDuration());
            //mp.setOnCompletionListener(this);

        }
    }

    public void pauseMusic() {
        mp.pause();
    }

    public void startMusic() {
        mp.start();

    }
    public void next() {
        if (temp1 == 0) {
            if (current == count - 1) {
                current = 0;
                release();
            } else {
                current = current + 1;
                playsong(current);

            }
        } else if (temp1 == 1) {
            if (current== count - 1) {
                current = 0;
            } else {
                current= current + 1;
            }
            playsong(current);
        } else {
            playsong(current);
        }
    }
        public void previous()
        {
        if (temp1 == 0) {
            if (current != 0) {
                current = current - 1;
            }
            playsong(current);
        } else if (temp1 == 1) {
            if (current!= 0) {
                current = current - 1;
            } else {
                current = count - 1;
            }
            playsong(current);
        } else {
            playsong(current);
        }
    }
    public void release() {

        if (mp != null) {
            mp.release();
            mp = null;
            temp= 0;
            sAudioManager.abandonAudioFocus(afChangeListener);
        }
    }

   /* @Override
    public void onCompletion(MediaPlayer mp) {
        Main2Activity.seekbar.setProgress(0);
        Main2Activity.seekbar.setMax(MainActivity.mp.getDuration());
        next();
        Song curr=MainActivity.songList.get(MainActivity.current);
        Main2Activity.image2.setImageBitmap(curr.getBitmap());
    }*/




   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }*/

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //connect to the service
    public void getSongslist() {
        ContentResolver musicResolver =getContext().getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            String path = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);

            //add songs to list
            do {
                long albId = musicCursor.getLong(albumColumn);
                final Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
                Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, albId);
                //


                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), albumArtUri);
                } catch (Exception exception) {
                    bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ic_launcher);
                }
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist,bitmap));
            }
            while (musicCursor.moveToNext());

        }
      musicCursor.close();
    }

    private Context getContext() {
        return this;
    }

    @Override
    public void onClick(View v) {

        if (v == playbutton) {
            if (mp.isPlaying()) {
                mp.pause();
                play.setImageResource(R.drawable.bplayarrow);
                // playbutton.setImageResource(R.drawable.play);
            } else {
               mp.start();
                play.setImageResource(R.drawable.bpausearrow);
            }
        }

    }


}

       /* if(audioCursor != null){
            if(audioCursor.moveToFirst()){
                int titleColumn = audioCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.TITLE);
                int idColumn = audioCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media._ID);
                int artistColumn = audioCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.ARTIST);
                do{
                    long thisId = audioCursor.getLong(idColumn);
                    String thisTitle = audioCursor.getString(titleColumn);
                    String thisArtist = audioCursor.getString(artistColumn);
                    pla.add(new song(thisId, thisTitle, thisArtist));

                    int audioIndex = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);

                    playlist.add(audioCursor.getString(audioIndex));
                }while(audioCursor.moveToNext());
            }*/


       /* audioCursor.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.songlayout,R.id.textView, playlist);
        list1.setAdapter(adapter);
        lLayout=(LinearLayout)findViewById(R.id.Linearlayout);
        lLayout.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(MainActivity.this,Main2Activity.class);
        startActivity(intent);
    }*/

