package pl.project13.kanbanery.adapter

import android.support.v4.view.PagerAdapter
import android.view.{ViewGroup, View}
import android.widget.TextView
import android.content.Context

class DemoPagerAdapter(ctx: Context) extends PagerAdapter {
  override def getCount = 3

  override def isViewFromObject(view: View, `object`: Any): Boolean = true

  override def instantiateItem(container: ViewGroup, position: Int): Object = {
    super.instantiateItem(container, position)
    val view = new TextView(ctx)
    view.setText("PAGE " + position)
    view
  }

  override def destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    super.destroyItem(container, position, `object`)
  }
}