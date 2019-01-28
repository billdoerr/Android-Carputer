package com.billdoerr.android.carputer;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.billdoerr.android.carputer.utils.ImageStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * CameraFragmentSnapshotViewer
 */
public class CameraFragmentSnapshotViewer extends Fragment {

    private static final String TAG = "CameraFragmentSnapshotViewer";

    private RecyclerView mRecyclerView;
    private ImageView mImageView;
    private TextView mTextView;

    private List<String> mFileList;
    private String mListItem;

    private int mSpanCount = 1;
    private boolean mScrolling = false;
    private boolean mUserScrolling = false;
    private boolean mViewCreated = false;

    public static CameraFragmentSnapshotViewer newInstance() {
        return new CameraFragmentSnapshotViewer();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_file_explorer, container, false);

        mImageView = (ImageView) view.findViewById(R.id.image_view);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        //  TODO :  Think about horizontal scrolling
//        LinearLayoutManager layoutManager
//                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//
//        RecyclerView myList = (RecyclerView) findViewById(R.id.my_recycler_view);
//        myList.setLayoutManager(layoutManager);

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
    }

    private void setupAdapter() {
        if (isAdded()) {
            mRecyclerView.setAdapter(new ListAdapter(mFileList));
        }
    }

    private void updateItems() {
        mFileList = new ImageStorage().getSnapshotFileList(getContext());
        mySort(mFileList);
    }

    private void update() {
        mFileList.clear();  //  Clear all data
        List<String> newFileList = new ImageStorage().getSnapshotFileList(getContext());
        mFileList.addAll(newFileList);
        mySort(mFileList);
        //  TODO : This is not updating the list correctly even though the data is correct.  Calling setupAdapter() corrects the issue for now.
//        mRecyclerView.getAdapter().notifyDataSetChanged();  //  Notify of change
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
        private static final String TAG = "ListHolder";

        private ListHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text_view);
            mImageView = (ImageView) itemView.findViewById(R.id.image_view);
            itemView.setOnClickListener(this);
        }

        private void bindListItem(String listItem) {
            mListItem = listItem;
        }

        @Override
        public void onClick(View view) {
            //  TODO : Do I even need this?
        }

    }

    /**
     * RECYCLERVIEW.ADAPTER
     */
    private class ListAdapter extends RecyclerView.Adapter<ListHolder> {
        private static final String TAG = "ListAdapter";
        private List<String> mListItems;

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
            listHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String path = getActivity().getFilesDir().toString();
                    File imgFile = new  File( path + "/" + mListItems.get(position));
                    if (imgFile.exists()) {
                        Log.d(TAG, "FILE --> " + imgFile + " <-- DOES EXIST!");
                        deleteFile(imgFile);
                    }
                }
            });

            String listItem = mListItems.get(position);
            listHolder.bindListItem(listItem);

            String path = getActivity().getFilesDir().toString();
            File imgFile = new  File( path + "/" + listItem);
            if (imgFile.exists()) {
                Log.d(TAG, "FILE -> " + imgFile + " <- DOES  EXIST!");

                //  Display filename
                mTextView.setText(listItem);

                //  Display image
                Bitmap bitmap = new ImageStorage().getSnapshot(getActivity(), listItem);
                mImageView.setImageBitmap(bitmap);
            }
        }

        @Override
        public int getItemCount() {
            return mListItems.size();
        }

        //  Delete file
        private void deleteFile(File file) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

            // Setting Dialog Title
            alertDialog.setTitle(getResources().getString(R.string.alert_title_delete_files));

            // Setting Dialog Message
            alertDialog.setMessage(getResources().getString(R.string.alert_message_confirm_delete));

            // Setting Icon to Dialog
//            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setIconAttribute(android.R.attr.alertDialogIcon);

            // Setting Positive "Yes" Btn
            alertDialog.setPositiveButton(R.string.alert_positive_button,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            if ( file.exists() ) {
                                try {
                                    file.delete();
                                    Toast.makeText(getActivity(), R.string.toast_file_delete_ok, Toast.LENGTH_SHORT)
                                            .show();
                                    update();
                                } catch (SecurityException e) {
                                    Log.e(TAG, e.getMessage());
                                }
                            }
                        }
                    });
            // Setting Negative "NO" Btn
            alertDialog.setNegativeButton(R.string.alert_negative_button,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            Toast.makeText(getActivity(), R.string.toast_file_delete_cancel, Toast.LENGTH_SHORT)
                                    .show();
                            dialog.cancel();
                        }
                    });

            // Showing Alert Dialog
            alertDialog.show();
        }

    }

}
