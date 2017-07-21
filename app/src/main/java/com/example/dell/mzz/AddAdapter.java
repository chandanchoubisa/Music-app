package com.example.dell.mzz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dell on 02-Apr-17.
 */


    public class AddAdapter extends BaseAdapter
    {
        public ArrayList<Song> songs;
        public LayoutInflater songInf;
        public AddAdapter(Context c, ArrayList<Song> theSongs){
            songs=theSongs;
            songInf=LayoutInflater.from(c);
        }
        @Override
        public int getCount() {
            return songs.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //map to song layout
            LinearLayout songLay = (LinearLayout) songInf.inflate
                    (R.layout.addlayout, parent, false);

            //get title and artist views
            TextView songView = (TextView) songLay.findViewById(R.id.song_title);
            TextView artistView = (TextView) songLay.findViewById(R.id.song_artist);
            ImageView bitI = (ImageView) songLay.findViewById(R.id.bitimage);
            //get song using position
            Song currSong = songs.get(position);
            //get title and artist strings
            bitI.setImageBitmap(currSong.getBitmap());
            songView.setText(currSong.getTitle());
            artistView.setText(currSong.getArtist());

            //set position as tag
            songLay.setTag(position);
            return songLay;
        }
        }

