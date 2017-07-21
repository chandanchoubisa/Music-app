package com.example.dell.mzz;

import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.os.Handler;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.LogRecord;

public class Main2Activity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,View.OnClickListener,MediaPlayer.OnCompletionListener
{
    static Main2Activity main2;
    ArrayList playlist;
    int position;
    Song song;
    String s;
    int count=0;
    static ArrayList<Song> addlist=new ArrayList<Song>();
     ListView addview;
    String name;
    String artist;
    ImageView image;
   MainActivity ma1=new MainActivity();
    TextView songname;
    TextView singername;
    Uri u;
    public Handler mHand = new Handler();
    ImageButton playbutton;
    ImageButton forwardbutton;
    ImageButton rewindbutton;
    ImageButton likeButton;
    ImageButton dislikeButton;
    static SeekBar seekbar;
    ImageView image1;
     AddAdapter songAdt;
   static ImageView image2;
    ImageView bit;
    Thread updateseekbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        main2=this;
        songname = (TextView) findViewById(R.id.songname);
        singername = (TextView) findViewById(R.id.singername);
        image1=(ImageView)findViewById(R.id.imageview);
        playbutton = (ImageButton) findViewById(R.id.play_button);
        forwardbutton = (ImageButton) findViewById(R.id.forward_button);
        rewindbutton = (ImageButton) findViewById(R.id.rewind_button);
        likeButton = (ImageButton) findViewById(R.id.like_button);
        image2=(ImageView)findViewById(R.id.activity2image);
        dislikeButton = (ImageButton) findViewById(R.id.dislike_button);
        seekbar = (SeekBar) findViewById(R.id.seek_bar);
        bit = (ImageView) findViewById(R.id.bitimage);
        addview= (ListView)findViewById(R.id.listview);

        Song curr=MainActivity.songList.get(MainActivity.current);
        Main2Activity.image2.setImageBitmap(curr.getBitmap());

        seekbar.setOnSeekBarChangeListener(this);

        Song currsong=MainActivity.songList.get(MainActivity.current);
        image1.setImageBitmap(currsong.getBitmap());
        image2.setImageBitmap(currsong.getBitmap());
        songname.setText(currsong.getTitle());
        songname.setSelected(true);
        songname.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        singername.setText(currsong.getArtist());

       // image1.setImageBitmap(currsong.getBitmap());
       // song=MainActivity.songList.get(MainActivity.current);
      /*  if(MainActivity.mp!=null)
        {
            MainActivity.mp.setOnCompletionListener(this);
        }*/

      //  updateBar(MainActivity.mp.getCurrentPosition());

        playbutton.setOnClickListener(this);
        forwardbutton.setOnClickListener(this);
        rewindbutton.setOnClickListener(this);
        likeButton.setOnClickListener(this);
        Main2Activity.this.runOnUiThread(new Runnable() {
            public void run() {
                Log.d("UI thread", "I am the UI thread");
                if (MainActivity.mp != null) {
                    seekbar.setProgress(0);
                    seekbar.setMax(MainActivity.mp.getDuration());
                    seekbar.setProgress(MainActivity.mp.getCurrentPosition());
                    MainActivity.mp.setOnCompletionListener(Main2Activity.this);
                }
                mHand.postDelayed(this, 1000);
            }
        });

    }



    @Override
   public void onCompletion(MediaPlayer mp) {


        ma1.mainActivity.next();
       count=1;
        Song curr=MainActivity.songList.get(MainActivity.current);
        songname.setText(curr.getTitle());
        singername.setText(curr.getArtist());
       image2.setImageBitmap(curr.getBitmap());
        mp.setLooping(true);

    }

        //Intent intent=getIntent();
       // Bundle b=intent.getExtras();

       // playlist=(ArrayList)b.getParcelableArrayList("songs");
        //s=playlist.toString();
        ///songname.setText(s);
        //position=b.getInt("pos",0);
        //song=MainActivity.songList.get(position);
      //  s=song.getTitle();
        //songname.setText(s);
       // String p=song.getArtist();
        //u= ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,song.getID());
        //String curr=u.getPath();

       /* try {
            mp.setDataSource(getApplicationContext(),u);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        // mp= MediaPlayer.create(getApplicationContext(),u);
         //mp.start();

    @Override
    public void onClick(View v) {
        if (v == playbutton) {
            if (MainActivity.mp.isPlaying()) {
                MainActivity.mp.pause();
                // playbutton.setImageResource(R.drawable.play);
            } else {
                MainActivity.mp.start();
            }
        }
        if(v==forwardbutton)
        {
            //MainActivity.current=MainActivity.current+1;

            ma1.mainActivity.next();
            Song currsong=MainActivity.songList.get(MainActivity.current);
            image1.setImageBitmap(currsong.getBitmap());
            image2.setImageBitmap(currsong.getBitmap());
            //seekbar.setMax(MainActivity.mp.getDuration());
           // ma1.mainActivity.playsong(MainActivity.current);
           // MainActivity.current=MainActivity.current+1;
          //  ma1.playsong(MainActivity.current);
        }
        if(v==rewindbutton)
        {
            //MainActivity.current=MainActivity.current-1;
            ma1.mainActivity.previous();
            Song currsong=MainActivity.songList.get(MainActivity.current);
            image1.setImageBitmap(currsong.getBitmap());
            image2.setImageBitmap(currsong.getBitmap());
            //seekbar.setMax(MainActivity.mp.getDuration());

        }
        if(v==likeButton)
        {
           //startActivity(new Intent(Main2Activity.this,PlayList.class));
            Song addsong=MainActivity.songList.get(MainActivity.current);
             long id=addsong.getID();
             Bitmap bitmap=addsong.getBitmap();
            String title=addsong.getTitle();
             String artist=addsong.getArtist();
            addlist.add(new Song(id,title,artist,bitmap));
            songAdt = new AddAdapter(this,this.addlist);
            addview.setAdapter(songAdt);

        }
        if(v==dislikeButton)
        {
          Intent i=new Intent(getApplicationContext(),PlayList.class);
            startActivity(i);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (MainActivity.mp != null && fromUser) {
            MainActivity.mp.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

 /*   @Override
    public void onCompletion(MediaPlayer mp)
    {
        seekbar.setProgress(0);
        seekbar.setMax(MainActivity.mp.getDuration());
        ma1.mainActivity.next();
        seekbar.setMax(MainActivity.mp.getDuration());
    }*/

   /* @Override
    public void onCompletion(MediaPlayer mp) {
        seekbar.setProgress(0);
        seekbar.setMax(MainActivity.mp.getDuration());
       ma1.mainActivity.next();
        Song curr=MainActivity.songList.get(MainActivity.current);
        image2.setImageBitmap(curr.getBitmap());
    }*/
  /*  @Override
    protected void onDestroy() {
        super.onDestroy();
       // ma1.mainActivity.release();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }*/
}
