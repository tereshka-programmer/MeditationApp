package com.example.testlearning

import com.example.tranyapp.resourcemanage.Consumer

class TestConsumer : Consumer<String>{

    private val _resources = mutableListOf<String>()
    val resources: List<String> = _resources
    val lastResource: String? get() = resources.lastOrNull()
    val invokeCount: Int get() = resources.size

    override fun invoke(resource: String) {
        _resources.add(resource)
    }

}