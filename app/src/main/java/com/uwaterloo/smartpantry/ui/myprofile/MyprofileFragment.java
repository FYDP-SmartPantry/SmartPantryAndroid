package com.uwaterloo.smartpantry.ui.myprofile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.uwaterloo.smartpantry.R;
import com.uwaterloo.smartpantry.ui.login.LoginActivity;
import com.uwaterloo.smartpantry.user.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.uwaterloo.smartpantry.ui.myprofile.MyprofileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyprofileFragment extends Fragment {
    private Button btnSignOut;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView username, email, id, familyname, givenname;
    ImageView userProfileImg;
    private GoogleSignInClient googleSignInClient;

    public MyprofileFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyprofileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static com.uwaterloo.smartpantry.ui.myprofile.MyprofileFragment newInstance(String param1, String param2) {
        com.uwaterloo.smartpantry.ui.myprofile.MyprofileFragment fragment = new com.uwaterloo.smartpantry.ui.myprofile.MyprofileFragment();
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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_myprofile, container, false);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        username = v.findViewById(R.id.name);
        email = v.findViewById(R.id.email);
        userProfileImg = v.findViewById(R.id.profile);
        id = v.findViewById(R.id.personid);
        if (acct != null) {
            username.setText(acct.getDisplayName());
            email.setText(acct.getEmail());
            id.setText(acct.getId());
            Uri personProfilUri =acct.getPhotoUrl();
            Glide.with(this).load(String.valueOf(personProfilUri)).into(userProfileImg);
            User user = User.getInstance();
            user.setUserInfo(acct.getId(), acct.getEmail(), acct.getDisplayName());
        }
        btnSignOut = v.findViewById(R.id.sign_out_btn);
        btnSignOut.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                switch (v.getId()) {
                    case R.id.sign_out_btn:
                        signOut();
                        break;
                }
            }
        });

        return v;
    }

    private void signOut() {
        googleSignInClient.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity(), "sign out", Toast.LENGTH_LONG).show();
            }
        });

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.putExtra("status", "loggedout");
        startActivity(intent);
    }
}
