package pl.project13.kanbanery.view

import android.content.Context
import android.widget.{TextView, LinearLayout}
import pl.project13.scala.android.util.ViewConversions
import android.util.AttributeSet
import pl.project13.janbanery.resources.Column

class VisibleColumnsIndicator(ctx: Context, attrs: AttributeSet) extends LinearLayout(ctx, attrs) with ViewConversions {
  setOrientation(LinearLayout.HORIZONTAL)

  def setColumns(columns: List[Column]) {
    columns foreach {
      c => addColumn(c.getName)
    }
  }

  def addColumn(text: String) {
    val todo = new TextView(ctx)
    todo.setPadding(5, 5, 5, 5)
    todo := text
    addView(todo)
  }

}
