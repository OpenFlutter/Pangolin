package view;

import android.content.Context;

import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


/**
 * Create by hanweiwei on 11/07/2018
 */
@SuppressWarnings({"SpellCheckingInspection", "unused"})
public class LoadMoreRecyclerView extends RecyclerView {

    private boolean mIsLoading = false;
    private ILoadMoreListener mLoadMoreListener;

    public LoadMoreRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (recyclerView == null || recyclerView.getLayoutManager() == null) {
                    return;
                }

                if (SCROLL_STATE_IDLE == newState && !mIsLoading) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    int lastCompletePosition = -1;
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                        lastCompletePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
                        int[] lastPos = manager.findLastCompletelyVisibleItemPositions(new int[manager.getSpanCount()]);
                        int maxVal = Integer.MIN_VALUE;
                        for (int pos : lastPos) {
                            if (pos > maxVal) {
                                maxVal = pos;
                            }
                        }
                        lastCompletePosition = maxVal;
                    }

                    if (lastCompletePosition == layoutManager.getItemCount() - 1) {
                        if (mLoadMoreListener != null) {
                            mIsLoading = true;
                            mLoadMoreListener.onLoadMore();
                        }
                    }
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }
        });
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);

        if (layout != null && getAdapter() != null) {
            getAdapter().onAttachedToRecyclerView(this);//手动调用下，否则加载更多异常
        }
    }

    public ILoadMoreListener getLoadMoreListener() {
        return mLoadMoreListener;
    }

    public void setLoadMoreListener(ILoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    public void setLoadingFinish() {
        mIsLoading = false;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

}
