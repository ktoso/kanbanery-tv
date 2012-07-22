package pl.project13.kanbanery.adapter

import pl.project13.janbanery.resources.Column
import android.support.v4.app.{Fragment, FragmentPagerAdapter, FragmentManager}
import pl.project13.kanbanery.fragment.ColumnFragment
import pl.project13.janbanery.core.Janbanery
import pl.project13.janbanery.util.JanbaneryConversions

class ColumnFragmentPagerAdapter(janbanery: Janbanery, fragmentManager: FragmentManager, columns: List[Column])
  extends FragmentPagerAdapter(fragmentManager)
  with JanbaneryConversions {

  val getCount = columns.size

  def getItem(position: Int): Fragment = {
    val column: Column = columns(position)
    ColumnFragment.newInstance(janbanery, column, columns.size)
  }

  override def getPageTitle(position: Int) = {
    columns(position).getName
  }
}
