package com.billdoerr.android.carputer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.billdoerr.android.carputer.utils.ImageStorage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * CameraFragmentSnapshotFileExplorer
 */
public class CameraFragmentSnapshotFileExplorer extends Fragment {

    private static final String TAG = "CameraFragSnapshotFileExplorer";

    private RecyclerView mRecyclerView;
    private ImageView mImageView;
    private TextView mTextView;

    private List<String> mFileList;
    private String mListItem;

    private int mSpanCount = 1;
    private boolean mScrolling = false;
    private boolean mUserScrolling = false;
    private boolean mViewCreated = false;

    public static CameraFragmentSnapshotFileExplorer newInstance() {
        return new CameraFragmentSnapshotFileExplorer();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera_file_explorer, container, false);

        mImageView = (ImageView) view.findViewById(R.id.image_view);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mSpanCount));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                switch (newState) {
                    case SCROLL_STATE_IDLE:
                        mUserScrolling = false;
                        break;
                    // If scroll is caused by a touch (scroll touch, not any touch)
                    case SCROLL_STATE_DRAGGING:
                        // If scroll was initiated already, this is not a user scrolling, but probably a tap, else set userScrolling
                        if (!mScrolling) {
                            mUserScrolling = true;
                        }
                        break;
                    case SCROLL_STATE_SETTLING:
                        // The user's finger is not touching the list anymore, no need
                        // for any alpha animation then
                        mScrolling = true;
                        break;
                }
            }
        });

        updateItems();
        setupAdapter();

        mViewCreated = true;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called!");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint: " + isVisibleToUser);
        if( isVisibleToUser && mViewCreated) {
            update();
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(TAG, "is Hidden: " + hidden);
    }

    private void updateItems() {
        mFileList = new ImageStorage().getSnapshotFileList(getContext());
        mySort(mFileList);
    }

    private void setupAdapter() {
        if (isAdded()) {
            mRecyclerView.setAdapter(new ListAdapter(mFileList));
        }
    }

    private void update() {
        Log.d(TAG, "Camera Fragment Snapshot File Explorer update called!");
        mFileList.clear();  //  Clear all data
//        updateItems();      //  Get new data
        List<String> newFileList = new ImageStorage().getSnapshotFileList(getContext());
        mFileList.addAll(newFileList);
        mySort(mFileList);
        mRecyclerView.getAdapter().notifyDataSetChanged();  //  Notify of change
    }

    class IgnoreCaseComparator implements Comparator<String> {
        public int compare(String strA, String strB) {
            //  Going for newest on top
            return strB.compareToIgnoreCase(strA);
        }
    }

    private void mySort(List<String> arrayList) {
        IgnoreCaseComparator icc = new IgnoreCaseComparator();
        java.util.Collections.sort(arrayList,icc);
    }

    /**
     * RECYCLERVIEW.VIEWHOLDER
     */
    private class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final String TAG = "ListAdapter";

        private TextView mItemTextView;
        private List<String> mListItems = new ArrayList<>();

        public ListHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text_view);
            mImageView = (ImageView) itemView.findViewById(R.id.image_view);
            itemView.setOnClickListener(this);
        }

        public void bindListItem(String listItem) {
            //  TODO:  This is a bug since the last item is the one that is always set and used in PhotoHolder.onClick()
            Log.d(TAG, "bindListItem");
            mListItem = listItem;
        }

        public void bindListItems(List<String> listItem) {
            //  TODO:  Resolves above bug
            mListItems = listItem;
        }

        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick: ----------------> " + mListItem);
            Log.i(TAG, "onClick.getLayoutPosition: ----------------> " + this.getLayoutPosition());
        }
    }

    /**
     * RECYCLERVIEW.ADAPTER
     */
    private class ListAdapter extends RecyclerView.Adapter<ListHolder> {
        private static final String TAG = "ListAdapter";
        private List<String> mListItems;

        public ListAdapter(List<String> listItems) {
            Log.d(TAG, "List Adapter called!");
            mListItems = listItems;
        }

        @NonNull
        @Override
        public ListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            Log.d(TAG, "onCreateViewHolder called!");
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.fragment_camera_file_list_item, viewGroup, false);
            return new ListHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListHolder listHolder, int position) {
            Log.i(TAG, "onBindViewHolder Position: ------------> " + position);
            String listItem = mListItems.get(position);
            //  TODO:  This is a bug since the last item is the one that is always set and used in PhotoHolder.onClick()
            listHolder.bindListItem(listItem);
            mTextView.setText(listItem);
//            Bitmap bitmap = new ImageStorage().getSnapshot(getActivity(), listItem);
//            mImageView.setImageBitmap(bitmap);
            String path = getActivity().getFilesDir().toString();
            File imgFile = new  File( path + "/" + listItem);
            if (imgFile.exists()) {
                Log.d(TAG, "FILE DOES EXIST!");
            }
            Picasso mPicasso = Picasso.with(getActivity());
            mPicasso.setIndicatorsEnabled(true);
            mPicasso.setLoggingEnabled(true);
            mPicasso.load(imgFile).into(mImageView);

            //  TODO:  Resolves above bug
//            listHolder.bindListItems(mListItems);
        }

        @Override
        public int getItemCount() {
            return mListItems.size();
        }
    }

}
