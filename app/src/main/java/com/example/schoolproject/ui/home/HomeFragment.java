package com.example.schoolproject.ui.home;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.schoolproject.R;
import com.example.schoolproject.databinding.FragmentHomeBinding;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private RelativeLayout animationRelative;
    AnimationDrawable animationDrawable;

    private ImageView imageView;
    private int[] imageResources = {R.drawable.raining, R.drawable.sunny, R.drawable.cloudy};
    private int currentImageIndex = 0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //animation
        animationRelative = root.findViewById(R.id.homefragmentAnimation);
        animationDrawable = (AnimationDrawable) animationRelative.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(3500);
        animationDrawable.start();

        imageView = root.findViewById(R.id.imageView_Gif);
        setLiveImage("raining");


        return root;
    }

    private void setLiveImage(String imageName) {
        int imageResource;

        // Find the resource ID for the given image name
        switch (imageName) {
            case "raining":
                imageResource = R.drawable.riaing_giphy;
                break;
            case "sunny":
                imageResource = R.drawable.sunny_giphy;
                break;
            case "cloudy":
                imageResource = R.drawable.cloudy_giphy;
                break;
            default:
                // If the image name is not recognized, set a default image
                imageResource = R.drawable.cloudy;
        }

        // Update the current image index based on the selected image
        for (int i = 0; i < imageResources.length; i++) {
            if (imageResources[i] == imageResource) {
                currentImageIndex = i;
                break;
            }
        }

        // Load the selected image into the ImageView
        Glide.with(requireContext()).load(imageResource).into(imageView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}