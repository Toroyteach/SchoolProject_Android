package com.example.schoolproject.ui.news;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolproject.Auth.User;
import com.example.schoolproject.R;
import com.example.schoolproject.services.SharedPrefManager;
import com.example.schoolproject.ui.news.models.NewsModel;
import com.example.schoolproject.ui.news.models.NewsRecyclerViewAdapter;
import com.example.schoolproject.ui.notification.NotificationFragment;
import com.example.schoolproject.ui.notification.utils.UserNotificationModel;
import com.example.schoolproject.ui.notification.utils.UserNotificationRecyclerViewAdapter;
import com.example.schoolproject.utils.URLs;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {

    private NewsFragment binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private NewsRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private List<NewsModel> newsList;

    private TextView newsTitle;
    private TextView newsBody;
    private ImageView newsImage;

    ProgressBar progressBar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the contactList and adapter

        newsTitle = (TextView) view.findViewById(R.id.main_news_title);
        newsBody = (TextView) view.findViewById(R.id.main_news_body);
        newsImage = (ImageView) view.findViewById(R.id.main_news_image);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_news);

        recyclerView = view.findViewById(R.id.newsrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsList = new ArrayList<>();
        adapter = new NewsRecyclerViewAdapter(newsList, this::OnReadMoreClickListener);
        recyclerView.setAdapter(adapter);
        fetchNewsData();

    }

    private void OnReadMoreClickListener(int i) {
    }

    private void fetchNewsData() {

        progressBar.setVisibility(View.VISIBLE);

        String url = URLs.URL_GET_NEWS;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Parse JSON data and populate the contactList

                        try {

                            JSONObject obj = new JSONObject(response);
                            if(obj.optString("status").equals("true")){

                                progressBar.setVisibility(View.GONE);
                                JSONArray dataArray  = obj.getJSONArray("data");

                                newsList.clear();

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataobj = dataArray.getJSONObject(i);

                                    //Show the first News Item from List
                                    if(i == 0){
                                        newsTitle.setText(dataobj.getString("title"));
                                        newsBody.setText(dataobj.getString("body"));

                                        String imageUrl = dataobj.getString("image"); // Replace with your image URL
                                        String image =  URLs.URL_GET_IMAGE+imageUrl.substring(1);
                                        Picasso.get().load(image).into(newsImage);
                                    } else {

                                        String title =  dataobj.getString("title");
                                        String body =  dataobj.getString("body");
                                        String category =  dataobj.getString("category");

                                        String imageUrl = dataobj.getString("image");
                                        String image =  URLs.URL_GET_IMAGE+imageUrl.substring(1);
                                        String created =  dataobj.getString("created_at");

                                        NewsModel contact = new NewsModel(title, body, image, created);
                                        newsList.add(contact);

                                    }
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle JSON parsing error
                        }

                        // Notify the adapter that the data set has changed
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        progressBar.setVisibility(View.GONE);
                        if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                            Toast.makeText(getContext(), "There Are No News To Show", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            System.out.println(error.getMessage());
                        }
                    }
                });

        // Add the request to the Volley request queue
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }
}