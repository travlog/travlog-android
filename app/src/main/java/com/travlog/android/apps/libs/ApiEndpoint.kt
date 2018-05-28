package com.travlog.android.apps.libs

enum class ApiEndpoint(private val type: String, private var url: String) {
    PRODUCTION("Production", ""),
    STAGING("Staging", "http://fobid.synology.me:3000/api/"),
    LOCAL("Local", "http://192.168.0.5:3000/api/"),
    CUSTOM("Custom", "");

    fun url(): String {
        return this.url
    }

    override fun toString(): String {
        return this.type
    }

    companion object {

        fun from(url: String): ApiEndpoint {
            for (value in values()) {
                if (value.url == url) {
                    return value
                }
            }
            val endpoint = CUSTOM
            endpoint.url = url
            return endpoint
        }
    }
}