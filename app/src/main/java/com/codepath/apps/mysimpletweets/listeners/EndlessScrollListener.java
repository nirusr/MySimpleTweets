package com.codepath.apps.mysimpletweets.listeners;

import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;

/**
 * Created by sgovind on 10/24/15.
 */
public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private int startingPageIndex = 0;
    private int currentPage = 0;


    @Override
    public void onScrolled(RecyclerView mRecyclerView, int dx, int dy) {
        super.onScrolled(mRecyclerView, dx, dy);

        LinearLayoutManager mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        visibleItemCount = mLayoutManager.getChildCount();
        totalItemCount = mLayoutManager.getItemCount();
        firstVisibleItem = mLayoutManager.findFirstCompletelyVisibleItemPosition();
        onScroll(firstVisibleItem, visibleItemCount, totalItemCount);



    }

    public void onScroll(int firstVisibleItem, int visibleItemCount, int totalItemCount ) {

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount)
        {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0)
            {
                this.loading = true;
            }
        }

        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount))
        {
            loading = false;
            previousTotalItemCount = totalItemCount;
            currentPage++;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem +
                visibleThreshold))
        {
            onLoadMore(currentPage + 1, totalItemCount);
            loading = true;
        }

    }

    // Defines the process for actually loading more data based on page
    public abstract boolean onLoadMore(int page, int totalItemsCount);


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

    }
}