package com.example.demo.common

data class PageResponse<T>(
    val items: List<T>,
    val page: Int,
    val lastPage: Boolean,
    val totalPage: Int,
    val totalItems: Long,
)
