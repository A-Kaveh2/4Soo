package ir.rasen.charsoo.view.fragment;

/**
 * Created by Sina KH on 6/23/2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.object.User;
import ir.rasen.charsoo.view.adapter.AdapterUsers;

public class FragmentSearchUser extends Fragment {
    public static final String TAG = "FragmentSearchUser";

    private ListView listView;
    private AdapterUsers adapter;

    public static FragmentSearchUser newInstance() {
        FragmentSearchUser fragment = new FragmentSearchUser();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //page = getArguments().getInt("someInt", 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_user_specials,
                container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        ArrayList<User> users = new ArrayList<>();
        User User1 = new User();
        User1.name="عنوان";
        User1.userIdentifier = "user1";
        User1.coverPictureId = 6372;
        users.add(User1);
        User User2 = new User();
        User2.name="عنوان";
        User2.userIdentifier = "user2";
        User2.coverPictureId = 5410;
        users.add(User2);
        User User3 = new User();
        User3.name="عنوان 3212313131312";
        User3.userIdentifier = "user3";
        User3.coverPictureId = 6372;
        users.add(User3);
        User User4 = new User();
        User4.name="BUSINESS";
        User4.userIdentifier = "avsf";
        User4.coverPictureId = 5410;
        users.add(User4);
        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new AdapterUsers(getActivity(), users);
        listView.setAdapter(adapter);
    }

}
