package com.s63d.simulator.utils

import java.util.*

fun <E> List<E>.random(): E = if (size == 0) throw UnsupportedOperationException() else get(Random().nextInt(size))
