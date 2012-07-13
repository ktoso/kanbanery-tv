package pl.project13.kanbanery.activity

import pl.project13.janbanery.resources.Column
import android.support.v4.app.{Fragment, FragmentPagerAdapter, FragmentManager}
import pl.project13.kanbanery.fragment.ColumnFragment
import pl.project13.janbanery.core.Janbanery

class ColumnAdapter(janbanery: Janbanery, fragmentManager: FragmentManager, columns: List[Column])
  extends FragmentPagerAdapter(fragmentManager) {

  val getCount = columns.size

  def getItem(position: Int): Fragment = {
    val column: Column = columns(position)
    ColumnFragment.newInstance(janbanery, column, columns.size)
  }
}
