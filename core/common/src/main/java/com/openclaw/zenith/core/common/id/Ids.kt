package com.openclaw.zenith.core.common.id

import java.util.UUID

/** Generate a UUID v4 string. Primary key strategy for every entity. */
fun newId(): String = UUID.randomUUID().toString()
