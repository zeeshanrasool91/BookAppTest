package ae.android.test.base

import android.widget.CompoundButton

fun CompoundButton.like() {
    isChecked =true
}
fun CompoundButton.dislike() {
    isChecked = false
}