package com.aragon.apiapplication.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.ListenableFuturePagingSource;
import androidx.paging.PagingSource;
import androidx.paging.PagingState;

import com.aragon.apiapplication.models.Product;
import com.aragon.apiapplication.network.ApiService;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import kotlin.coroutines.Continuation;
import retrofit2.Call;
import retrofit2.Response;

public class ProductsPagingSource extends ListenableFuturePagingSource<Integer, Product> {
    private final ApiService api;
    private final int pageSize;
    private final String search;
    private final String sort;
    private final Executor backgroundExecutor;

    public ProductsPagingSource(ApiService api, int pageSize, String search, String sort) {
        this.api = api;
        this.pageSize = pageSize;
        this.search = search;
        this.sort = sort;
        this.backgroundExecutor = Executors.newSingleThreadExecutor();
    }

    @NonNull
    @Override
    public ListenableFuture<LoadResult<Integer, Product>> loadFuture(@NonNull LoadParams<Integer> loadParams) {
        int page = loadParams.getKey() == null ? 0 : loadParams.getKey();
        int pageSize = loadParams.getLoadSize();

        return Futures.submit(() -> {
            try {
                Response<PageResponse<Product>> resp = api.getProducts(page, pageSize, search, sort).execute();
                if (!resp.isSuccessful()) {
                    return new LoadResult.Error<>(new Exception("HTTP " + resp.code()));
                }
                PageResponse<Product> body = resp.body();
                if (body == null) {
                    return new LoadResult.Error<>(new Exception("Empty body"));
                }

                List<Product> items = body.getContent();
                Integer prevKey = page == 0 ? null : page - 1;
                Integer nextKey = body.isLast() ? null : page + 1;

                return new LoadResult.Page<>(items, prevKey, nextKey);

            } catch (Exception e) {
                return new LoadResult.Error<>(e);
            }
        }, backgroundExecutor);
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Product> pagingState) {
        Integer anchorPos = pagingState.getAnchorPosition();
        if (anchorPos == null) return null;
        LoadResult.Page<Integer, Product> closest = pagingState.closestPageToPosition(anchorPos);
        if (closest == null) return null;
        if (closest.getPrevKey() != null) return closest.getPrevKey() + 1;
        if (closest.getNextKey() != null) return closest.getNextKey() - 1;
        return null;
    }
}