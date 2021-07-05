package com.shash.simpleslider.`interface`

import com.shash.simpleslider.constants.ActionTypes

interface TouchListener {
    /**
     * Click listener touched item function.
     *
     * @param  touched  slider boolean
     */
    fun onTouched(touched: ActionTypes)
}