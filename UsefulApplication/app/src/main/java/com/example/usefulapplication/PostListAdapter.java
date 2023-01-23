package com.example.usefulapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usefulapplication.dao.UserPostDao;
import com.example.usefulapplication.database.AppDatabase;
import com.example.usefulapplication.database.DatabaseFactory;
import com.example.usefulapplication.model.Track;
import com.example.usefulapplication.model.UserPost;
import com.example.usefulapplication.service.DeezerController;
import com.example.usefulapplication.service.TrackRepository;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {
    private List<UserPost> posts;
    private LayoutInflater inflater;

    Retrofit retrofit;
    DeezerController service;
    TrackRepository trackRepository;
    private LifecycleOwner lifecycleOwner;
    private Fragment parentFragment;
    private Context context;
    private static MediaPlayer mediaPlayer;
    private static PostListAdapter.PostViewHolder currentMediaPlayerHolder;
    Map<Integer, String> songUris;

    public PostListAdapter(Context context, List<UserPost> posts, LifecycleOwner viewLifecycleOwner, Fragment fragment) {
        this.inflater = LayoutInflater.from(context);
        this.posts = posts;
        this.retrofit = new Retrofit.Builder().baseUrl("https://deezerdevs-deezer.p.rapidapi.com").addConverterFactory(GsonConverterFactory.create()).build();
        this.service = retrofit.create(DeezerController.class);
        this.trackRepository = new TrackRepository(service);
        this.lifecycleOwner = viewLifecycleOwner;
        this.parentFragment = fragment;
        this.context = context;
        this.mediaPlayer = new MediaPlayer();
        this.songUris = new HashMap<>();
    }

    @NonNull
    @Override
    public PostListAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.postlist_item, parent, false);

        mediaPlayer.setOnCompletionListener(mp -> {
            mp.reset();
            currentMediaPlayerHolder.playSongButton.setText("Play song");
            currentMediaPlayerHolder.playSongButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_play_circle_15, 0, 0, 0);
            currentMediaPlayerHolder = null;
        });
        return new PostViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PostListAdapter.PostViewHolder holder, int position) {
        UserPost post = posts.get(position);

        holder.postLocationView.setText(post.getLocation());
        holder.postCaptionView.setText(post.getCaption());
        holder.postDateView.setText("Image taken: "+post.getDate());
        holder.postImageView.setImageURI(Uri.parse(post.getImageUri()));
        holder.postDatePostedView.setText("Posted: "+convertMillisToDate(post.getPostCreationTimeMillis()));

        Log.i("millis to date", "onBindViewHolder: "+convertMillisToDate(post.getPostCreationTimeMillis()));

        holder.postMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("NB", "onClick: Post menu button clicked");
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.post_popup_menu, popupMenu.getMenu());
                popupMenu.getMenu().findItem(R.id.menu_edit).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        stopSong();
                        Log.i("NB", "onMenuItemClick: " + menuItem.getTitle());
                        Bundle bundle = new Bundle();
                        bundle.putLong("postId", post.getUid());
                        bundle.putString("trackId", post.getSongId());
                        bundle.putString("caption", post.getCaption());
                        bundle.putString("location", post.getLocation());
                        bundle.putString("date", post.getDate());
                        bundle.putString("locationLat", post.getLocationLatitude());
                        bundle.putString("locationLong", post.getLocationLongitude());
                        bundle.putString("imageUri", post.getImageUri());
                        bundle.putString("postCreationTimeMillis", post.getPostCreationTimeMillis());
                        NavHostFragment.findNavController(parentFragment).navigate(R.id.action_postFragment_to_editPost, bundle);
                        return true;
                    }
                });

                popupMenu.getMenu().findItem(R.id.menu_delete).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                        alertDialogBuilder.setCancelable(true);
                        alertDialogBuilder.setTitle("Alert");
                        alertDialogBuilder.setMessage("Are you sure you want to delete the post?");
                        alertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(currentMediaPlayerHolder.equals(holder)){
                                    stopSong();
                                }
                                AppDatabase appDatabase = DatabaseFactory.getAppDatabase(view.getContext());
                                UserPostDao userPostDao = appDatabase.userPostDao();
                                userPostDao.deletePost(post);
                                posts.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                notifyItemRangeChanged(holder.getAdapterPosition(), posts.size());
                            }
                        });
                        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        AlertDialog dialog = alertDialogBuilder.create();
                        dialog.show();
                        return true;
                    }
                });

                popupMenu.show();
            }
        });

        holder.postLocationView.setOnClickListener(view -> {
            PostListAdapter.stopSong();
            ViewMapHandler viewMapHandler = new ViewMapHandler(post.getLocationLatitude(), post.getLocationLongitude(), this.parentFragment.getActivity());
            viewMapHandler.handle();
        });

        holder.postImageContainer.setOnClickListener(postImageContainerView -> {
            if(currentMediaPlayerHolder != null && holder.postTrackContainer.getVisibility() == View.VISIBLE && currentMediaPlayerHolder.equals(holder)){
                stopSong();
            }
            holder.postTrackContainer.setVisibility(holder.postTrackContainer.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            holder.playSongButton.setOnClickListener(playSongButtonView -> {
                String songUriString = songUris.get(position);
                if(songUriString == null || songUriString.isEmpty()){
                    Toast.makeText(context, "Cannot play song.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Uri myUri = Uri.parse(songUriString);
                if(mediaPlayer.isPlaying() && currentMediaPlayerHolder.equals(holder)){
                    stopSong();
                }else if(mediaPlayer.isPlaying() && !currentMediaPlayerHolder.equals(holder)){
                    stopSong();
                    playSong(myUri, holder);
                }else{
                    playSong(myUri, holder);
                }
            });;
        });

        MutableLiveData<Track> track = new MutableLiveData<>();
        requestTrack(post.getSongId(), track);

        final Observer<Track> trackObserver = new Observer<Track>() {
            @Override
            public void onChanged(@Nullable Track track) {
                try{
                    holder.postTitleView.setText(track.getTitle());
                    holder.postArtistView.setText(track.getArtist().getName());
                    songUris.put(position, track.getPreview());
                    Log.i("NB", "onChanged: " + track.getAlbum().getCover_medium());
                    Picasso.get().load(track.getAlbum().getCover_medium()).into(holder.postTrackImageView);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        track.observe(lifecycleOwner, trackObserver);
    }

    private void playSong(Uri songUri, PostListAdapter.PostViewHolder holder){
        if(songUri == null || songUri.toString().isEmpty()){
            return;
        }
        try {
            mediaPlayer.setDataSource(parentFragment.getContext(), songUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentMediaPlayerHolder = holder;
        mediaPlayer.start();
        currentMediaPlayerHolder.playSongButton.setText("Stop song");
        currentMediaPlayerHolder.playSongButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_stop_circle_15, 0, 0, 0);

    }

    public static void stopSong(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.reset();
            currentMediaPlayerHolder.playSongButton.setText("Play song");
            currentMediaPlayerHolder.playSongButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.baseline_play_circle_15, 0, 0, 0);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void requestTrack(String trackId, MutableLiveData<Track> track) {
        if(track.getValue() == null){
            Call<Track> trackCall = trackRepository.getTrack(trackId);
            trackCall.enqueue(new Callback<Track>() {
                @Override
                public void onResponse(Call<Track> call, Response<Track> response) {
                    if (response.isSuccessful()) {
//                    Log.i(this.getClass().getSimpleName(), response.body().);
                        track.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Track> call, Throwable t) {
                    // show error message to user
                    Log.i(this.getClass().getSimpleName(), "Error: " + t.toString());
                }
            });
        }
    }

    private String convertMillisToDate(String millis){
        DateFormat df = new SimpleDateFormat("dd:MM:yy");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(millis));
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH)+1;
        int year = cal.get(Calendar.YEAR);
        return day+"/"+month+"/"+year;
//        return df.format(cal.getTime());
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        public final TextView postDateView;
        public final TextView postDatePostedView;
        public final Button postLocationView;
        public final TextView postCaptionView;
        public final ImageView postTrackImageView;
        public final TextView postTitleView;
        public final TextView postArtistView;
        public final Button postMenuButton;
        public final ImageView postImageView;
        public final ConstraintLayout postImageContainer;
        public final LinearLayout postTrackContainer;
        public final Button playSongButton;
        final PostListAdapter adapter;

        public PostViewHolder(@NonNull View itemView, PostListAdapter adapter) {
            super(itemView);
            this.postDateView = itemView.findViewById(R.id.post_date);
            this.postDatePostedView = itemView.findViewById(R.id.postCreationDateTextView);
            this.postLocationView = itemView.findViewById(R.id.post_location);
            this.postCaptionView = itemView.findViewById(R.id.post_caption);
            this.postTitleView = itemView.findViewById(R.id.post_song_title);
            this.postArtistView = itemView.findViewById(R.id.post_song_artist);
            this.postTrackImageView = itemView.findViewById(R.id.post_song_image);
            this.postMenuButton = itemView.findViewById(R.id.post_menu_button);
            this.postImageView = itemView.findViewById(R.id.post_image);
            this.postImageContainer = itemView.findViewById(R.id.postImageContainer);
            this.postTrackContainer = itemView.findViewById(R.id.postTrackContainer);
            this.playSongButton = itemView.findViewById(R.id.play_song_button);
            this.adapter = adapter;
        }
    }

    class ViewMapHandler extends Fragment {
        private String latitude;
        private String longitude;
        private Activity mainActivity;
        public ViewMapHandler(String latitude, String longitude, Activity mainActivity){
            this.latitude = latitude;
            this.longitude = longitude;
            this.mainActivity = mainActivity;
        }

        public void handle() {
            if(locationPermissionsSet(context)){
                navigateToSelectMapLocationFragment(latitude, longitude);
            }else{
                Log.i("selectLocationButton", "onViewCreated: permissions not set");
                requestPermissions();
            }
        }

        private boolean locationPermissionsSet(Context context){
            return ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }

        private void requestPermissions(){
            Activity main = (MainActivity) mainActivity;
            String[] permissions = {android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(main, permissions, 1);
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if(requestCode == 1){
                if(grantResults.length == 1){
                    Log.i("requesting permissions", "onRequestPermissionsResult: permissions granted");
                    navigateToSelectMapLocationFragment(latitude, longitude);
                }
            }
        }

        private void navigateToSelectMapLocationFragment(String latitude, String longitude) {
            Bundle bundle = new Bundle();
            bundle.putString("locationLat", latitude);
            bundle.putString("locationLong", longitude);
            NavHostFragment.findNavController(parentFragment).navigate(R.id.action_postFragment_to_viewLocationMapFragment, bundle);
        }
    }
}