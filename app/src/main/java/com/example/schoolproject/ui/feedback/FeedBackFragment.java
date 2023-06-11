package com.example.schoolproject.ui.feedback;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolproject.Auth.User;
import com.example.schoolproject.R;
import com.example.schoolproject.services.SharedPrefManager;
import com.example.schoolproject.ui.notification.NotificationFragment;
import com.example.schoolproject.ui.notification.utils.UserNotificationRecyclerViewAdapter;
import com.example.schoolproject.utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedBackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedBackFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FeedBackFragment binding;

    private RelativeLayout animationRelative;
    AnimationDrawable animationDrawable;

    Button addFeedbackBtn;
    EditText feeback_subject_txt;
    EditText feedback_message_txt;

    ProgressBar progressBar;

    public FeedBackFragment() {
        // Required empty public constructor
    }


    public static FeedBackFragment newInstance(String param1, String param2) {
        FeedBackFragment fragment = new FeedBackFragment();
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
        return inflater.inflate(R.layout.fragment_feed_back, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Animation
        animationRelative = view.findViewById(R.id.feedbackfragmentAnimation);
        animationDrawable = (AnimationDrawable) animationRelative.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(3500);
        animationDrawable.start();

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_feeback);

        feeback_subject_txt = (EditText) view.findViewById(R.id.editTextMessage_feeack);
        feedback_message_txt = (EditText) view.findViewById(R.id.editTextSubject_feedback);

        addFeedbackBtn = (Button) view.findViewById(R.id.buttonSubmit_feedback);
        addFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFeedback();
            }
        });
    }

    public void addFeedback(){

        final String feedbackSbj = feeback_subject_txt.getText().toString().trim();
        final String feedbackMsg = feedback_message_txt.getText().toString().trim();

        //first we will do the validations

        if (TextUtils.isEmpty(feedbackSbj)) {
            feeback_subject_txt.requestFocus();
            Toast.makeText(getActivity(), "Subject of the feedback is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(feedbackMsg)) {
            Toast.makeText(getActivity(), "Body of the feedback is required", Toast.LENGTH_SHORT).show();
            feedback_message_txt.requestFocus();
            return;
        }

        postDataUsingVolley(feedbackSbj, feedbackMsg);

    }

    private void postDataUsingVolley(String feedback_subject, String feedback_message) {
        // url to post our data
        progressBar.setVisibility(View.VISIBLE);


        feeback_subject_txt.setText("");
        feedback_message_txt.setText("");

        //get userId
        User user =  SharedPrefManager.getInstance(getActivity()).getUser();
        final String username = user.getUsername();
        final String email = user.getEmail();
        final String phone = user.getPhone();

        System.out.println(feedback_message+feedback_message+username+email+phone);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_POST_FEEDBACK, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty
                progressBar.setVisibility(View.GONE);

                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    JSONObject respObj = new JSONObject(response);
                    String message = respObj.getString("message");

                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    //Call the method to refresh the list of custom alerts

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("mobile", phone);
                params.put("name", username);
                params.put("email", email);
                params.put("comment", feedback_message);
                params.put("subject", feedback_subject);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}