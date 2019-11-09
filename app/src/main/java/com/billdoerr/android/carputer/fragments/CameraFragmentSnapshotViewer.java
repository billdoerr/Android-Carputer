package com.billdoerr.android.carputer.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.billdoerr.android.carputer.R;
import com.billdoerr.android.carputer.utils.FileStorageUtils;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

//import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;
//import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;
//import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 *  Child fragment which displays a list of saved images (snapshots).
 *  Created by the CameraFragmentImageArchive class.
 */
public class CameraFragmentSnapshotViewer extends Fragment {

    private RecyclerView mRecyclerView;
    private ImageView mImageView;
    private TextView mTextView;

    private List<String> mFileList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_file_explorer, container, false);

        mImageView = view.findViewById(R.id.image_view);

        mRecyclerView = view.findViewById(R.id.recycler_view);

        //  Horizontal scrolling
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

//        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mSpanCount));

//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                switch (newState) {
//                    case SCROLL_STATE_IDLE:
//                        break;
//                    // If scroll is caused by a touch (scroll touch, not any touch)
//                    case SCROLL_STATE_DRAGGING:
//                        // If scroll was initiated already, this is not a user scrolling, but probably a tap, else set userScrolling
//                        break;
//                    case SCROLL_STATE_SETTLING:
//                        // The user's finger is not touching the list anymore, no need
//                        // for any alpha animation then
//                        mScrolling = true;
//                        break;
//                }
//            }
//        });

        updateItems();
        setupAdapter();

        return view;
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if( isVisibleToUser && mViewCreated) {
//            update();
//        }
//    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    private void setupAdapter() {
        if (isAdded()) {
            mRecyclerView.setAdapter(new ListAdapter(mFileList));
        }
    }

    private void updateItems() {
        mFileList = FileStorageUtils.getSnapshotFileList(Objects.requireNonNull(getContext()));
        mySort(mFileList);
    }

    private void update() {
        mFileList.clear();  //  Clear all data
        List<String> newFileList = FileStorageUtils.getSnapshotFileList(Objects.requireNonNull(getContext()));
        mFileList.addAll(newFileList);
        mySort(mFileList);
        setupAdapter();
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

        private ListHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.text_view);
            mImageView = itemView.findViewById(R.id.image_view);
            itemView.setOnClickListener(this);
        }

        @SuppressWarnings({"EmptyMethod", "unused"})
        private void bindListItem(String listItem) {
//            mListItem = listItem;
        }

        @Override
        public void onClick(View view) {
            //  Required
        }

    }

    /**
     * RECYCLERVIEW.ADAPTER
     */
    private class ListAdapter extends RecyclerView.Adapter<ListHolder> {
        private static final String TAG = "ListAdapter";
        private final List<String> mListItems;

        private ListAdapter(List<String> listItems) {
            mListItems = listItems;
        }

        @NonNull
        @Override
        public ListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.fragment_camera_file_list_item, viewGroup, false);
            return new ListHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListHolder listHolder, int position) {
            Log.i(TAG, "onBindViewHolder Position: ------------> " + position);

            //  On-click listener
            listHolder.itemView.setOnClickListener(v -> {
                String path = Objects.requireNonNull(getActivity()).getFilesDir().toString();
                File imgFile = new  File( path + "/" + mListItems.get(position));
                if (imgFile.exists()) {
                    Log.d(TAG, "FILE --> " + imgFile + " <-- DOES EXIST!");
                    deleteFile(imgFile);
                }
            });

            String listItem = mListItems.get(position);
            listHolder.bindListItem(listItem);

            String path = Objects.requireNonNull(getActivity()).getFilesDir().toString();
            File imgFile = new  File( path + "/" + listItem);
            if (imgFile.exists()) {
                Log.d(TAG, "FILE -> " + imgFile + " <- DOES  EXIST!");

                //  Display filename
                mTextView.setText(listItem);

                //  Display image
                Bitmap bitmap = FileStorageUtils.getSnapshot(getActivity(), listItem);
                mImageView.setImageBitmap(bitmap);
            }
        }

        @Override
        public int getItemCount() {
            return mListItems.size();
        }

        /**
         * Delete file from image storage.
         * @param file File:  Object of type File that will deleted from internal storage.
         */
        private void deleteFile(File file) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

            // Setting Dialog Title
            alertDialog.setTitle(getResources().getString(R.string.alert_title_delete_files));

            // Setting Dialog Message
            alertDialog.setMessage(getResources().getString(R.string.alert_message_confirm_delete));

            // Setting Icon to Dialog
//            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setIconAttribute(android.R.attr.alertDialogIcon);

            // Setting Positive "Yes" Btn
            alertDialog.setPositiveButton(R.string.alert_positive_button,
                    (dialog, which) -> {
                        // Write your code here to execute after dialog
                        if ( file.exists() ) {
                            try {
                                boolean isDeleted = file.delete();
                                if (isDeleted) {
                                    Toast.makeText(getActivity(), R.string.toast_file_delete_ok, Toast.LENGTH_SHORT)
                                            .show();
                                    update();
                                }
                            } catch (SecurityException e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    });
            // Setting Negative "NO" Btn
            alertDialog.setNegativeButton(R.string.alert_negative_button,
                    (dialog, which) -> {
                        // Write your code here to execute after dialog
                        Toast.makeText(getActivity(), R.string.toast_file_delete_cancel, Toast.LENGTH_SHORT)
                                .show();
                        dialog.cancel();
                    });

            // Showing Alert Dialog
            alertDialog.show();
        }

    }

}
