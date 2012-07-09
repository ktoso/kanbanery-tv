package pl.project13.scala.android.activity

import android.os.Bundle
import android.app.Activity
import pl.project13.kanbanery.{TypedLayout, TypedActivity}

trait ContentView extends Activity with TypedActivity {
  def ContentView: TypedLayout

  override def onCreate(bundle: Bundle) {
    setContentView(ContentView.id)
    super.onCreate(bundle)
  }
}
