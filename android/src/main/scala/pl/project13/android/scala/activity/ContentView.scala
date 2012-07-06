package pl.project13.android.scala.activity

import android.os.Bundle
import android.app.Activity
import pl.project13.smsser.android.{TypedActivity, TypedLayout}

trait ContentView extends Activity with TypedActivity {
  def ContentView: TypedLayout

  override def onCreate(bundle: Bundle) {
    setContentView(ContentView.id)
    super.onCreate(bundle)
  }
}
