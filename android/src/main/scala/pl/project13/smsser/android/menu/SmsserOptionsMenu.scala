package pl.project13.smsser.android.menu

import pl.project13.android.scala.activity.ScalaActivity
import android.view.{MenuItem, MenuInflater, Menu}
import android.R

trait SmsserOptionsMenu {
  this: ScalaActivity =>


  override def onCreateOptionsMenu(menu: Menu): Boolean = {
      val inflater = getMenuInflater
      inflater.inflate(R.menu.main, menu)
      true
  }

  override def onOptionsItemSelected(item: MenuItem ): Boolean= {
      item.getItemId match {
          case R.id.settings =>
              Intents
              return true;
          case R.id.help:
              showHelp();
              return true;
          default:
              return super.onOptionsItemSelected(item);
      }
  }

}
