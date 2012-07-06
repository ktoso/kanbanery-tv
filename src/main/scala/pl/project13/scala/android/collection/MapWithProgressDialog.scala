package pl.project13.scala.android.collection

import android.app.ProgressDialog
import android.content.Context
import android.os.{Handler, AsyncTask}
import pl.project13.scala.android.thread.{ThreadingHelpers, VoidAsyncTask}
import android.util.Log

trait MapWithProgressDialog {

  val Tag = getClass.getSimpleName

  implicit def list2withProgressDialogList[T](l: List[T]) = new ListWithProgressDialogOnMap[T](l)

  class ListWithProgressDialogOnMap[T](list: List[T]) {

    def foreachWithProgressDialog[Y](title: String, msg: String = "Processing %d of %d items...")
                                    (block: (T) => Y)
                                    (implicit ctx: Context){ //: List[Y]= { // todo would need futures here

      val listSize = list.size
      val handler = new Handler()

      new AsyncTask[AnyRef, Seq[AnyRef], List[Y]] {

        var dialog: ProgressDialog = _

        override def onPreExecute() {
          dialog = ProgressDialog.show(ctx, title, msg.format(1, listSize), false)
          dialog.setMax(listSize)
        }

        override def doInBackground(p1: AnyRef*): List[Y] = {
          var i = 0
          val mapped = list map { item =>
            // publishProgress is broken with Scala
            handler.post(new Runnable { def run() {
              dialog.setMessage(msg.format(i, listSize))
              dialog.setProgress(i)
            }})
            i += 1

            block(item)
          }

          mapped
        }

//        override def onProgressUpdate(values: Seq[AnyRef]*) {
//          Log.d(Tag, "Updating progress: " + values(0))
//          dialog.setMessage(msg.format(values(0), listSize))
//        }

        override def onPostExecute(result: List[Y]) {
          dialog.dismiss()
        }
      }.execute()
    }
  }
}

object MapWithProgressDialog extends MapWithProgressDialog
