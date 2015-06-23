package ir.rasen.charsoo.view.fragment;

/**
 * Created by Sina KH on 6/23/2015.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import ir.rasen.charsoo.R;
import ir.rasen.charsoo.controller.object.Post;
import ir.rasen.charsoo.view.adapter.AdapterSpecialProductsGrid;

public class FragmentSearchProduct extends Fragment {
    public static final String TAG = "FragmentSearchProduct";

    private GridView gridView;
    private AdapterSpecialProductsGrid adapterSpecialProductsGrid;

    public static FragmentSearchProduct newInstance() {
        FragmentSearchProduct fragment = new FragmentSearchProduct();
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
        View view = inflater.inflate(R.layout.fragment_search_product_specials,
                container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        ArrayList<Post> fakePosts = new ArrayList<>();
        Post post1 = new Post();
        post1.title="عنوان";
        post1.pictureId = 6372;
        fakePosts.add(post1);
        Post post2 = new Post();
        post2.title="عنوان";
        post2.pictureId = 5410;
        fakePosts.add(post2);
        Post post3 = new Post();
        post3.title="عنوان 3212313131312";
        post3.pictureId = 6372;
        fakePosts.add(post3);
        Post post4 = new Post();
        post4.title="PRODUCT";
        post4.pictureId = 5410;
        fakePosts.add(post4);
        gridView = (GridView) view.findViewById(R.id.gridView);
        adapterSpecialProductsGrid = new AdapterSpecialProductsGrid(getActivity(), fakePosts);
        gridView.setAdapter(adapterSpecialProductsGrid);
    }

}
