package com.dicoding.escore.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dicoding.escore.data.local.entity.HistoryEntity
import com.dicoding.escore.data.local.entity.RemoteEntity
import com.dicoding.escore.data.local.room.HistoryDatabase
import com.dicoding.escore.data.remote.retrofit.ApiService


@OptIn(ExperimentalPagingApi::class)
class HistoryRemoteMediator(
    private val database: HistoryDatabase,
    private val apiService: ApiService,
) : RemoteMediator<Int, HistoryEntity>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, HistoryEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val response =
                apiService.history(page, state.config.pageSize)

            val endOfPaginationReached = response.predictions?.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.historyDao().deleteAll()
                    database.remoteDao().deleteRemoteKeys()
                }

                val keys = response.predictions?.map { history ->
                    RemoteEntity(
                        id = history?.id,
                        prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                        nextKey = if (endOfPaginationReached == true) null else page + 1
                    )
                }
                database.remoteDao().insertAll(keys)

                database.historyDao().insertStory(response.predictions?.map {
                    HistoryEntity(
                        createdAt = it?.createdAt,
                        title = it?.title,
                        score = it?.predictedResult?.score,
                        id = it?.id
                    )
                } ?: emptyList())
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached == true)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, HistoryEntity>): RemoteEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { history ->
            database.remoteDao().getRemoteEntityId(history.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, HistoryEntity>): RemoteEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { history ->
            database.remoteDao().getRemoteEntityId(history.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, HistoryEntity>): RemoteEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteDao().getRemoteEntityId(id)
            }
        }
    }
}