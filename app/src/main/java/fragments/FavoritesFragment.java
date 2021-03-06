package fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

import adapters.NewsCursorAdapter;
import adapters.NewsRecyclerAdapter;
import data.NewsContract;
import models.Articles;
import models.Source;
import ravikiran.pathade.ravikiranpathade.newstrends.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,NewsCursorAdapter.checkEmpty {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Cursor mCursor;
    List<Articles> articleList;
    NewsRecyclerAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    View view;
    TextView textView;
    ListView listView;
    Cursor cursor1;
    NewsCursorAdapter cursorAdapter;
    Context context;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
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
        cursorAdapter = new NewsCursorAdapter(getContext(), null, null,this);
        getActivity().getSupportLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(context.getResources().getString(R.string.favorite_fragment_toolbar));


        view = inflater.inflate(R.layout.fragment_favorites, container, false);
        textView = view.findViewById(R.id.favoritesTextView);


        listView = view.findViewById(R.id.favoriteRecycler);
        listView.setAdapter(cursorAdapter);
        textView = view.findViewById(R.id.favoritesTextView);
        listView = view.findViewById(R.id.favoriteRecycler);
        if (getContext().getContentResolver().query(
                NewsContract.NewsFavoritesEntry.FINAL_URI,
                null,
                null,
                null,
                context.getResources().getString(R.string.ID_DESC)
        ).getCount() == 0) {


        }
        return view;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        return new CursorLoader(getContext(), NewsContract.NewsFavoritesEntry.FINAL_URI,
                null,
                null,
                null,
                getContext().getResources().getString(R.string.ID_DESC));

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor1 != null) {
            cursor1 = null;
        }
        cursor1 = getContext().getContentResolver().query(
                NewsContract.NewsFavoritesEntry.FINAL_URI,
                null,
                null,
                null,
                getContext().getResources().getString(R.string.ID_DESC)
        );


        cursorAdapter.swapCursor(cursor1);

        if (cursor1 == null || cursor1.getCount() == 0) {
            textView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();

        if (preferences.getBoolean(getContext().getResources().getString(R.string.favorites_changed), false)) {
            getActivity().getSupportLoaderManager().initLoader(0, null, this).forceLoad();
            editor.putBoolean(getContext().getResources().getString(R.string.favorites_changed), false);
        }
    }

    @Override
    public void onCheckEmpty(boolean checkBoolean) {
        if (checkBoolean == true) {
            textView.setVisibility(View.VISIBLE);
        }
    }
}
