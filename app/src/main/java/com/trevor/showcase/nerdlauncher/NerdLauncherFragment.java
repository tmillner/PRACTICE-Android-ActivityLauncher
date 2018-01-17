package com.trevor.showcase.nerdlauncher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by trevormillner on 1/17/18.
 */

public class NerdLauncherFragment extends Fragment {

    public static final String TAG = NerdLauncherFragment.class.getName();

    RecyclerView mRecyclerView;

    public static NerdLauncherFragment newInstance() {
        return new NerdLauncherFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nerd_launcher, container, false);
        mRecyclerView = v.findViewById(R.id.nerd_launcher_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupAdapter();
        return v;
    }

    public void setupAdapter() {
        Intent startAppIntent = new Intent(Intent.ACTION_MAIN);
        startAppIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final PackageManager packageManager = getActivity().getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(startAppIntent, 0);

        // If this is a bad sort, likely need to create a new packageManager inside this
        //  sort function
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo a, ResolveInfo b) {
                return String.CASE_INSENSITIVE_ORDER.compare(
                        a.loadLabel(packageManager).toString(),
                        b.loadLabel(packageManager).toString());
            }
        });
        Log.i(TAG, "Found " + activities.size() + " implicit activities.");
        mRecyclerView.setAdapter(new ActivityAdapter(activities));

    }


    public class ActivityHolder extends RecyclerView.ViewHolder {

        private ResolveInfo mActivityInfo;
        private TextView mTextView;

        public ActivityHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
        }

        public void bindActivity(ResolveInfo activity) {
            mActivityInfo = activity;
            PackageManager pm = getActivity().getPackageManager();
            String appName = mActivityInfo.loadLabel(pm).toString();
            mTextView.setText(appName);
        }
    }

    public class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder> {

        private List<ResolveInfo> mActivities;

        public ActivityAdapter(List<ResolveInfo> activities) {
            mActivities = activities;
        }

        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

            return new ActivityHolder(view);
        }

        @Override
        public void onBindViewHolder(ActivityHolder holder, int position) {
            ResolveInfo activity = mActivities.get(position);
            holder.bindActivity(activity);
        }

        @Override
        public int getItemCount() {
            return mActivities.size();
        }
    }
}
